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

    private final Map<String, KonfigurationsVerantwortlicher> konfigurationsVerantwortliche = new LinkedHashMap<>();
    private final Map<String, KonfigurationsBereich> konfigurationsbereiche = new LinkedHashMap<>();
    private final Map<String, Typ> typen = new LinkedHashMap<>();
    private final Map<Long, MengenVerwendung> mengenVerwendungen = new LinkedHashMap<>();

    private final DataModel model;

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

        KonfigurationsBereich result = KonfigurationsBereich.erzeugeMitPid(area.getPid());
        konfigurationsbereiche.put(area.getPid(), result);
        bestimmeSystemObjekt(area, result);
        result.setZustaendiger(getKonfiguratonsVerantwortlicher(area.getConfigurationAuthority()));
        area.getCurrentObjects().stream().filter(Metamodell::istTyp).forEach(t -> result.getTypen().add(getTyp((SystemObjectType) t)));
        return result;
    }

    private KonfigurationsVerantwortlicher getKonfiguratonsVerantwortlicher(ConfigurationAuthority authority) {
        if (konfigurationsVerantwortliche.containsKey(authority.getPid()))
            return konfigurationsVerantwortliche.get(authority.getPid());

        KonfigurationsVerantwortlicher result = KonfigurationsVerantwortlicher.erzeugeMitPid(authority.getPid());
        konfigurationsVerantwortliche.put(authority.getPid(), result);
        bestimmeSystemObjekt(authority, result);
        return result;
    }

    private void bestimmeSystemObjekt(SystemObject object, SystemObjekt result) {
        result.setName(object.getName());
        result.setKurzinfo(object.getInfo().getShortInfo().trim());
        result.setBeschreibung(object.getInfo().getDescription().trim());
        if (!(object instanceof ConfigurationArea))
            result.setBereich(getKonfigurationsbereich(object.getConfigurationArea()));
    }

    private static boolean istTyp(SystemObject systemObject) {
        return systemObject.isOfType("typ.typ") && !systemObject.isOfType("typ.mengenTyp");
    }

    public Typ getTyp(String pid) {
        return getTyp(model.getType(pid));
    }

    private Typ getTyp(SystemObjectType type) {
        if (typen.containsKey(type.getPid()))
            return typen.get(type.getPid());

        Typ result = Typ.erzeugeMitPid(type.getPid());
        typen.put(type.getPid(), result);
        bestimmeSystemObjekt(type, result);
        type.getSuperTypes().stream().forEach(t -> result.getSuperTypen().add(getTyp(t)));
        if (type.getObjectSet("Mengen") != null)
            type.getObjectSet("Mengen").getElements().forEach(m -> result.getMengen().add(getMengenVerwendung(m)));
        return result;
    }

    private MengenVerwendung getMengenVerwendung(SystemObject mengenVerwendung) {
        if (mengenVerwendungen.containsKey(mengenVerwendung.getId()))
            return mengenVerwendungen.get(mengenVerwendung.getId());

        MengenVerwendung result = new MengenVerwendung();
        mengenVerwendungen.put(mengenVerwendung.getId(), result);
        bestimmeSystemObjekt(mengenVerwendung, result);
        Data eigenschaften = mengenVerwendung.getConfigurationData(model.getAttributeGroup("atg.mengenVerwendungsEigenschaften"));
        result.setMengenName(eigenschaften.getTextValue("mengenName").getText());
        SystemObject mengenTyp = eigenschaften.getReferenceValue("mengenTyp").getSystemObject();
        ConfigurationObject cMengenTyp = (ConfigurationObject) mengenTyp;
        cMengenTyp.getObjectSet("ObjektTypen").getElements().forEach(t -> result.getObjektTypen().add(getTyp((SystemObjectType) t)));
        return result;
    }

}
