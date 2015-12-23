/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.bsvrz.dav.daf.main.config.Attribute;
import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Fabrik zum Erzeugen von Attributtypen.
 *
 * @author Falko Schumann
 * @since 3.2
 */
class AttributTypFabrik extends SystemObjektFabrik<AttributTyp> {

    AttributTypFabrik(Metamodell metamodell) {
        super(metamodell);
    }

    static boolean istAttributTyp(SystemObject systemObject) {
        return systemObject.isOfType("typ.attributTyp");
    }

    @Override
    protected AttributTyp erzeugeObjekt(SystemObject objekt) {
        return AttributTyp.erzeugeMitPid(objekt.getPid());
    }

    Attribut getAttribut(long id) {
        return getAttribut((Attribute) getMetamodell().getModel().getObject(id));
    }

    private Attribut getAttribut(Attribute attribute) {
        Attribut result = Attribut.erzeugeMitNameUndTyp(attribute.getName(), getMetamodell().getAttributTyp(attribute.getAttributeType().getPid()));
        getMetamodell().bestimmeSystemObjekt(attribute, result);
        return result;
    }

}
