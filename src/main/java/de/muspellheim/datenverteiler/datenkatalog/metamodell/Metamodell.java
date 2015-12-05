/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Fabrik zum Erzeugen von Objekten des Metamodells.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class Metamodell {

    private final DataModel model;
    private final Map<String, KonfigurationsBereich> konfigurationsbereiche = new LinkedHashMap<>();
    private final Map<String, Typ> typen = new LinkedHashMap<>();
    private final Map<String, MengenVerwendung> mengenVerwendungen = new LinkedHashMap<>();

    public Metamodell(DataModel model) {
        this.model = model;
    }

    public Set<KonfigurationsBereich> getKonfigurationsbereiche() {
        return model.getType("typ.konfigurationsBereich").getElements().stream().
                map(k -> getKonfigurationsbereich(k.getPid())).
                collect(Collectors.toSet());
    }

    public KonfigurationsBereich getKonfigurationsbereich(String pid) {
        return getKonfigurationsbereich(model.getConfigurationArea(pid));
    }

    private KonfigurationsBereich getKonfigurationsbereich(ConfigurationArea area) {
        if (konfigurationsbereiche.containsKey(area.getPid()))
            return konfigurationsbereiche.get(area.getPid());

        KonfigurationsBereich result = new KonfigurationsBereich();
        bestimmeSystemObjekt(area, result);
        konfigurationsbereiche.put(area.getPid(), result);
        area.getCurrentObjects().stream().filter(Metamodell::istTyp).forEach(t -> result.getTypen().add(getTyp(t)));
        return result;
    }

    private void bestimmeSystemObjekt(SystemObject object, SystemObjekt result) {
        result.setName(object.getName());
        result.setPid(object.getPid());
        result.setKurzinfo(object.getInfo().getShortInfo().trim());
        result.setBeschreibung(object.getInfo().getDescription().trim());
    }

    private static boolean istTyp(SystemObject systemObject) {
        return systemObject.isOfType("typ.typ") && !systemObject.isOfType("typ.mengenTyp");
    }

    public Typ getTyp(String pid) {
        return getTyp(model.getType(pid));
    }

    private Typ getTyp(SystemObject type) {
        if (type instanceof SystemObjectType)
            return getTyp((SystemObjectType) type);

        throw new IllegalArgumentException("Kein Typ: " + type);
    }

    private Typ getTyp(SystemObjectType type) {
        if (typen.containsKey(type.getPid()))
            return typen.get(type.getPid());

        Typ result = new Typ();
        bestimmeSystemObjekt(type, result);
        result.setDynamisch(type instanceof DynamicObjectType);
        typen.put(type.getPid(), result);
        type.getSuperTypes().stream().filter(t -> !t.isBaseType()).forEach(t -> result.getSuperTypen().add(getTyp(t)));
        if (type.getObjectSet("Mengen") != null)
            type.getObjectSet("Mengen").getElements().forEach(m -> result.getMengen().add(getMengenVerwendung(m)));
        return result;
    }

    private MengenVerwendung getMengenVerwendung(SystemObject mengenVerwendung) {
        if (mengenVerwendungen.containsKey(mengenVerwendung.getPid()))
            return mengenVerwendungen.get(mengenVerwendung.getPid());

        Data eigenschaften = mengenVerwendung.getConfigurationData(model.getAttributeGroup("atg.mengenVerwendungsEigenschaften"));
        MengenVerwendung result = new MengenVerwendung();
        bestimmeSystemObjekt(mengenVerwendung, result);
        result.setMengenName(eigenschaften.getTextValue("mengenName").getText());
        mengenVerwendungen.put(mengenVerwendung.getPid(), result);
        SystemObject mengenTyp = eigenschaften.getReferenceValue("mengenTyp").getSystemObject();
        ConfigurationObject cMengenTyp = (ConfigurationObject) mengenTyp;
        cMengenTyp.getObjectSet("ObjektTypen").getElements().forEach(t -> result.getObjektTypen().add(getTyp(t)));
        return result;
    }

}
