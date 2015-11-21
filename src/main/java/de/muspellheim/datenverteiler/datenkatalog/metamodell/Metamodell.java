/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.ConfigurationArea;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Fabrik zum Erzeugen von Objekten des Metamodells.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class Metamodell {

    private final DataModel model;

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
        return systemObject.isOfType("typ.typ");
    }

    public Typ getTyp(String pid) {
        return getTyp(model.getType(pid));
    }

    private Typ getTyp(SystemObject type) {
        Typ result = new Typ();
        result.setName(type.getName());
        ConfigurationObject cType = (ConfigurationObject) type;
        cType.getNonMutableSet("SuperTypen").getElements().forEach(t -> result.getSuperTypen().add(getTyp(t)));
        cType.getNonMutableSet("Mengen").getElements().forEach(m -> result.getMengen().add(getMengenVerwendung(m)));
        return result;
    }

    private MengenVerwendung getMengenVerwendung(SystemObject mengenVerwendung) {
        Data eigenschaften = mengenVerwendung.getConfigurationData(model.getAttributeGroup("atg.mengenVerwendungsEigenschaften"));
        MengenVerwendung result = new MengenVerwendung();
        result.setMengenName(eigenschaften.getTextValue("mengenName").getText());
        result.setMengenTyp(getMengenTyp(eigenschaften.getReferenceValue("mengenTyp").getSystemObject()));
        return result;
    }

    private MengenTyp getMengenTyp(SystemObject mengenTyp) {
        MengenTyp result = new MengenTyp();
        ConfigurationObject cMengenTyp = (ConfigurationObject) mengenTyp;
        cMengenTyp.getNonMutableSet("ObjektTypen").getElements().forEach(t -> result.getObjektTypen().add(getTyp(t)));
        return result;
    }

}
