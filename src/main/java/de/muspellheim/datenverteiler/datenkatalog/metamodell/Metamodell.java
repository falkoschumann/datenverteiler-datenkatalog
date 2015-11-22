/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Fabrik zum Erzeugen von Objekten des Metamodells.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class Metamodell {

    private final DataModel model;
    private final Map<SystemObjectType, Typ> zwischenspeicher = new LinkedHashMap<>();

    public Metamodell(DataModel model) {
        this.model = model;
    }

    public KonfigurationsBereich getKonfigurationsbereich(String pid) {
        ConfigurationArea area = model.getConfigurationArea(pid);
        KonfigurationsBereich result = new KonfigurationsBereich();
        result.setName(area.getName());
        area.getCurrentObjects().stream().filter(Metamodell::istTyp).forEach(t -> result.getTypen().add(getTyp(t)));
        return result;
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
        if (zwischenspeicher.containsKey(type))
            return zwischenspeicher.get(type);

        Typ result = new Typ();
        result.setName(type.getName());
        type.getSuperTypes().stream().filter(t -> !t.isBaseType()).forEach(t -> result.getSuperTypen().add(getTyp(t)));
        if (type.getObjectSet("Mengen") != null)
            type.getObjectSet("Mengen").getElements().forEach(m -> result.getMengen().add(getMengenVerwendung(m)));

        zwischenspeicher.put(type, result);
        return result;
    }

    private MengenVerwendung getMengenVerwendung(SystemObject mengenVerwendung) {
        Data eigenschaften = mengenVerwendung.getConfigurationData(model.getAttributeGroup("atg.mengenVerwendungsEigenschaften"));
        MengenVerwendung result = new MengenVerwendung();
        result.setMengenName(eigenschaften.getTextValue("mengenName").getText());
        SystemObject mengenTyp = eigenschaften.getReferenceValue("mengenTyp").getSystemObject();
        ConfigurationObject cMengenTyp = (ConfigurationObject) mengenTyp;
        cMengenTyp.getObjectSet("ObjektTypen").getElements().forEach(t -> result.getObjektTypen().add(getTyp(t)));
        return result;
    }

}
