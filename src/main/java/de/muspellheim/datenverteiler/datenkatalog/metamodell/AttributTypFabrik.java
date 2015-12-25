/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Fabrik zum Erzeugen von Attributtypen.
 *
 * @author Falko Schumann
 * @since 3.2
 */
abstract class AttributtypFabrik<T extends Systemobjekt> extends SystemobjektFabrik<T> {

    AttributtypFabrik(Metamodell metamodell) {
        super(metamodell);
    }

    static boolean istAttributTyp(SystemObject systemObject) {
        return systemObject.isOfType("typ.attributTyp");
    }

}
