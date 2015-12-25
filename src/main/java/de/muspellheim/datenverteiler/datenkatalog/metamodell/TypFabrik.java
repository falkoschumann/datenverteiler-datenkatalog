/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.bsvrz.dav.daf.main.config.DynamicObjectType;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dav.daf.main.config.SystemObjectType;

/**
 * Fabrik zum Erzeugen von Typen.
 *
 * @author Falko Schumann
 * @since 3.2
 */
class TypFabrik extends SystemobjektFabrik<Typ> {

    TypFabrik(Metamodell metamodell) {
        super(metamodell);
    }

    static boolean istTyp(SystemObject systemObject) {
        return systemObject.isOfType("typ.typ") && !systemObject.isOfType("typ.mengenTyp");
    }

    @Override
    protected Typ erzeugeObjekt(SystemObject objekt) {
        if (objekt instanceof DynamicObjectType)
            return DynamischerTyp.erzeugeMitPid(objekt.getPid());

        return Typ.erzeugeMitPid(objekt.getPid());
    }

    @Override
    protected void initialisiereObjekt(SystemObject object, Typ result) {
        super.initialisiereObjekt(object, result);

        SystemObjectType type = (SystemObjectType) object;
        type.getSuperTypes().stream().forEach(e -> result.getSupertypen().add(getMetamodell().getTyp(e.getPid())));
        type.getSubTypes().stream().forEach(e -> result.getSubtypen().add(getMetamodell().getTyp(e.getPid())));
        type.getDirectObjectSetUses().forEach(e -> result.getMengen().add(getMetamodell().getMengenverwendung(e.getId())));
        type.getDirectAttributeGroups().forEach(e -> result.getAttributgruppen().add(getMetamodell().getAttributgruppe(e.getPid())));
    }

}
