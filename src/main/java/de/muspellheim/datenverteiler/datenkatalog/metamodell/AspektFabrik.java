/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Fabrik zum Erzeugen von Aspekten.
 *
 * @author Falko Schumann
 * @since 3.2
 */
class AspektFabrik extends SystemobjektFabrik<Aspekt> {

    AspektFabrik(Metamodell metamodell) {
        super(metamodell);
    }

    static boolean istAspekt(SystemObject systemObject) {
        return systemObject.isOfType("typ.aspekt");
    }

    @Override
    protected Aspekt erzeugeObjekt(SystemObject objekt) {
        return Aspekt.erzeugeMitPid(objekt.getPid());
    }

}
