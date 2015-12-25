/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Fabrik zum Erzeugen von Attributgruppen.
 *
 * @author Falko Schumann
 * @since 3.2
 */
class AttributgruppeFabrik extends AttributMengeFabrik<Attributgruppe> {

    AttributgruppeFabrik(Metamodell metamodell) {
        super(metamodell);
    }

    static boolean istAttributgruppe(SystemObject systemObject) {
        return systemObject.isOfType("typ.attributgruppe");
    }

    @Override
    protected Attributgruppe erzeugeObjekt(SystemObject objekt) {
        return Attributgruppe.erzeugeMitPid(objekt.getPid());
    }

    @Override
    protected void initialisiereObjekt(SystemObject object, Attributgruppe result) {
        super.initialisiereObjekt(object, result);

        AttributeGroup atg = (AttributeGroup) object;
        atg.getAspects().forEach(e -> result.getAspekte().add(getMetamodell().getAspekt(e.getPid())));
        atg.getAttributeGroupUsages().forEach(e -> result.getAttributgruppenVerwendungen().add(getMetamodell().getAttributgruppenVerwendung(e.getPid())));
    }

}
