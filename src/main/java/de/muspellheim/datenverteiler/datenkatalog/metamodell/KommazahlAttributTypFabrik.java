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
class KommazahlAttributTypFabrik extends AttributTypFabrik<KommazahlAttributTyp> {

    KommazahlAttributTypFabrik(Metamodell metamodell) {
        super(metamodell);
    }

    static boolean istAttributTyp(SystemObject systemObject) {
        return systemObject.isOfType("typ.kommazahlAttributTyp");
    }

    @Override
    protected KommazahlAttributTyp erzeugeObjekt(SystemObject objekt) {
        return KommazahlAttributTyp.erzeugeMitPid(objekt.getPid());
    }

    @Override
    protected void initialisiereObjekt(SystemObject object, KommazahlAttributTyp result) {
        super.initialisiereObjekt(object, result);

        DoubleAttributeType type = (DoubleAttributeType) object;
        result.setEinheit(type.getUnit());
        switch (type.getAccuracy()) {
            case DoubleAttributeType.DOUBLE:
                result.setGenauigkeit(FliesskommaAufloesung.DOUBLE);
                break;
            case DoubleAttributeType.FLOAT:
                result.setGenauigkeit(FliesskommaAufloesung.FLOAT);
                break;
            default:
                throw new IllegalStateException("Unbekannte Fließkommaauflösung: " + type.getAccuracy());
        }

    }

}
