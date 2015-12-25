/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dav.daf.main.config.TimeAttributeType;

/**
 * Fabrik zum Erzeugen von Zeitstempelattributtypen.
 *
 * @author Falko Schumann
 * @since 3.2
 */
class ZeitstempelAttributtypFabrik extends AttributtypFabrik<ZeitstempelAttributtyp> {

    ZeitstempelAttributtypFabrik(Metamodell metamodell) {
        super(metamodell);
    }

    static boolean istAttributTyp(SystemObject systemObject) {
        return systemObject.isOfType("typ.zeitstempelAttributTyp");
    }

    @Override
    protected ZeitstempelAttributtyp erzeugeObjekt(SystemObject objekt) {
        return ZeitstempelAttributtyp.erzeugeMitPid(objekt.getPid());
    }

    @Override
    protected void initialisiereObjekt(SystemObject object, ZeitstempelAttributtyp result) {
        super.initialisiereObjekt(object, result);

        TimeAttributeType type = (TimeAttributeType) object;
        result.setRelativ(type.isRelative());
        switch (type.getAccuracy()) {
            case TimeAttributeType.SECONDS:
                result.setGenauigkeit(Zeitaufloesung.SEKUNDEN);
                break;
            case TimeAttributeType.MILLISECONDS:
                result.setGenauigkeit(Zeitaufloesung.MILLISEKUNDEN);
                break;
            default:
                throw new IllegalStateException("Unbekante Zeitauflösung: " + type.getAccuracy());
        }
    }

}
