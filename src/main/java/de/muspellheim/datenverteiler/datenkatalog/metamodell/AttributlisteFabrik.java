/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Fabrik zum Erzeugen von Attributlistendefinitionen.
 *
 * @author Falko Schumann
 * @since 3.2
 */
class AttributlisteFabrik extends AttributmengeFabrik<Attributliste> {

    AttributlisteFabrik(Metamodell metamodell) {
        super(metamodell);
    }

    static boolean istAttributliste(SystemObject systemObject) {
        return systemObject.isOfType("typ.attributListenDefinition");
    }

    @Override
    protected Attributliste erzeugeObjekt(SystemObject objekt) {
        return Attributliste.erzeugeMitPid(objekt.getPid());
    }

}
