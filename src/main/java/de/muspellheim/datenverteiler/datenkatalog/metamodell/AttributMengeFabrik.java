/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Abstrate Fabrik zum Erzeugen von Attributmengen.
 *
 * @author Falko Schumann
 * @since 3.2
 */
abstract class AttributMengeFabrik<T extends AttributMenge> extends SystemObjektFabrik<T> {

    AttributMengeFabrik(Metamodell metamodell) {
        super(metamodell);
    }

    @Override
    protected void initialisiereObjekt(SystemObject object, T result) {
        super.initialisiereObjekt(object, result);

        ConfigurationObject co = (ConfigurationObject) object;
        co.getObjectSet("Attribute").getElements().stream().forEach(e -> result.getAttribute().add(getMetamodell().getAttribut(e.getId())));
    }

}
