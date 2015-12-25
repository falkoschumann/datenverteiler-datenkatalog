/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Fabrik zum Erzeugen von Attributgruppenverwendungen.
 *
 * @author Falko Schumann
 * @since 3.2
 */
class AttributgruppenVerwendungFabrik extends SystemObjektFabrik<AttributgruppenVerwendung> {

    AttributgruppenVerwendungFabrik(Metamodell metamodell) {
        super(metamodell);
    }

    static boolean istAttributgruppenVerwendung(SystemObject systemObject) {
        return systemObject.isOfType("typ.attributgruppenVerwendung");
    }

    @Override
    protected AttributgruppenVerwendung erzeugeObjekt(SystemObject objekt) {
        return AttributgruppenVerwendung.erzeugeMitPid(objekt.getPid());
    }

}
