/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.ObjectSetType;
import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Fabrik zum Erzeugen von Mengentypen.
 *
 * @author Falko Schumann
 * @since 3.2
 */
class MengentypFabrik extends SystemobjektFabrik<Mengentyp> {

    MengentypFabrik(Metamodell metamodell) {
        super(metamodell);
    }

    static boolean istMengenTyp(SystemObject systemObject) {
        return systemObject.isOfType("typ.mengenTyp");
    }

    @Override
    protected Mengentyp erzeugeObjekt(SystemObject objekt) {
        return Mengentyp.erzeugeMitPid(objekt.getPid());
    }

    @Override
    protected void initialisiereObjekt(SystemObject object, Mengentyp result) {
        super.initialisiereObjekt(object, result);

        ObjectSetType objectSetType = (ObjectSetType) object;
        objectSetType.getObjectTypes().forEach(t -> result.getObjektTypen().add(getMetamodell().getTyp(t.getPid())));
        AttributeGroup atg = getMetamodell().getModel().getAttributeGroup("atg.mengenTypEigenschaften");
        Data data = object.getConfigurationData(atg);
        result.setMinimaleAnzahl(data.getUnscaledValue("minimaleAnzahl").intValue());
        result.setMaximaleAnzahl(data.getUnscaledValue("maximaleAnzahl").intValue());
        result.setAenderbar(data.getUnscaledValue("änderbar").byteValue() != 0);
        switch (data.getUnscaledValue("referenzierungsart").byteValue()) {
            case 0:
                result.setReferenzierungsart(Referenzierungsart.ASSOZIATION);
                break;
            case 1:
                result.setReferenzierungsart(Referenzierungsart.AGGREGATION);
                break;
            case 2:
                result.setReferenzierungsart(Referenzierungsart.KOMPOSITION);
                break;
            default:
                throw new IllegalStateException("Unbekannte Referenzierungsart: " + data.getUnscaledValue("referenzierungsart").getText());
        }
    }

}
