/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.bsvrz.dav.daf.main.config.StringAttributeType;
import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Fabrik zum Erzeugen von Zeichenkettenattributtypen.
 *
 * @author Falko Schumann
 * @since 3.2
 */
class ZeichenkettenAttributtypFabrik extends AttributtypFabrik<ZeichenkettenAttributtyp> {

    ZeichenkettenAttributtypFabrik(Metamodell metamodell) {
        super(metamodell);
    }

    static boolean istAttributTyp(SystemObject systemObject) {
        return systemObject.isOfType("typ.zeichenketteAttributTyp");
    }

    @Override
    protected ZeichenkettenAttributtyp erzeugeObjekt(SystemObject objekt) {
        return ZeichenkettenAttributtyp.erzeugeMitPid(objekt.getPid());
    }

    @Override
    protected void initialisiereObjekt(SystemObject object, ZeichenkettenAttributtyp result) {
        super.initialisiereObjekt(object, result);

        StringAttributeType type = (StringAttributeType) object;
        result.setLaenge(type.getMaxLength());
        result.setKodierung(Zeichenkodierung.of(type.getEncodingName()));
    }

}
