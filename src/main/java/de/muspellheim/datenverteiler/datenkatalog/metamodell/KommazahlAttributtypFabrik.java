/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.bsvrz.dav.daf.main.config.DoubleAttributeType;
import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Fabrik zum Erzeugen von Kommazahlattributtypen.
 *
 * @author Falko Schumann
 * @since 3.2
 */
class KommazahlAttributtypFabrik extends AttributtypFabrik<KommazahlAttributtyp> {

    KommazahlAttributtypFabrik(Metamodell metamodell) {
        super(metamodell);
    }

    static boolean istAttributTyp(SystemObject systemObject) {
        return systemObject.isOfType("typ.kommazahlAttributTyp");
    }

    @Override
    protected KommazahlAttributtyp erzeugeObjekt(SystemObject objekt) {
        return KommazahlAttributtyp.erzeugeMitPid(objekt.getPid());
    }

    @Override
    protected void initialisiereObjekt(SystemObject object, KommazahlAttributtyp result) {
        super.initialisiereObjekt(object, result);

        DoubleAttributeType type = (DoubleAttributeType) object;
        result.setEinheit(type.getUnit());
        switch (type.getAccuracy()) {
            case DoubleAttributeType.DOUBLE:
                result.setGenauigkeit(Fliesskommaaufloesung.DOUBLE);
                break;
            case DoubleAttributeType.FLOAT:
                result.setGenauigkeit(Fliesskommaaufloesung.FLOAT);
                break;
            default:
                throw new IllegalStateException("Unbekannte Fließkommaauflösung: " + type.getAccuracy());
        }

    }

}
