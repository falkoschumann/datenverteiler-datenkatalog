/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.bsvrz.dav.daf.main.config.ObjectSetType;
import de.bsvrz.dav.daf.main.config.ObjectSetUse;
import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Fabrik zum Erzeugen von Mengentypen.
 *
 * @author Falko Schumann
 * @since 3.2
 */
class MengenTypFabrik extends SystemObjektFabrik<MengenTyp> {

    MengenTypFabrik(Metamodell metamodell) {
        super(metamodell);
    }

    static boolean istMengenTyp(SystemObject systemObject) {
        return systemObject.isOfType("typ.mengenTyp");
    }

    @Override
    protected MengenTyp erzeugeObjekt(SystemObject objekt) {
        return MengenTyp.erzeugeMitPid(objekt.getPid());
    }

    @Override
    protected void initialisiereObjekt(SystemObject object, MengenTyp result) {
        super.initialisiereObjekt(object, result);

        ObjectSetType objectSetType = (ObjectSetType) object;
        objectSetType.getObjectTypes().forEach(t -> result.getObjektTypen().add(getMetamodell().getTyp(t.getPid())));
    }

    MengenVerwendung getMengenVerwendung(long id) {
        return getMengenVerwendung((ObjectSetUse) getMetamodell().getModel().getObject(id));
    }

    private MengenVerwendung getMengenVerwendung(ObjectSetUse objectSetUse) {
        MengenVerwendung result = MengenVerwendung.erzeuge(objectSetUse.getObjectSetName(), getObjekt(objectSetUse.getObjectSetType().getPid()), objectSetUse.isRequired());
        getMetamodell().bestimmeSystemObjekt(objectSetUse, result);
        return result;
    }

}
