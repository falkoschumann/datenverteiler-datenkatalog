/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.bsvrz.dav.daf.main.config.ReferenceAttributeType;
import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Fabrik zum Erzeugen von Objektreferenzattributtypen.
 *
 * @author Falko Schumann
 * @since 3.2
 */
class ObjektReferenzAttributTypFabrik extends AttributTypFabrik<ObjektReferenzAttributTyp> {

    ObjektReferenzAttributTypFabrik(Metamodell metamodell) {
        super(metamodell);
    }

    static boolean istAttributTyp(SystemObject systemObject) {
        return systemObject.isOfType("typ.objektReferenzAttributTyp");
    }

    @Override
    protected ObjektReferenzAttributTyp erzeugeObjekt(SystemObject objekt) {
        return ObjektReferenzAttributTyp.erzeugeMitPid(objekt.getPid());
    }

    @Override
    protected void initialisiereObjekt(SystemObject object, ObjektReferenzAttributTyp result) {
        super.initialisiereObjekt(object, result);

        ReferenceAttributeType type = (ReferenceAttributeType) object;
        if (type.getReferencedObjectType() != null)
            result.setTyp(getMetamodell().getTyp(type.getReferencedObjectType().getPid()));
        result.setUndefiniertErlaubt(type.isUndefinedAllowed());
        switch (type.getReferenceType()) {
            case ASSOCIATION:
                result.setReferenzierungsart(Referenzierungsart.ASSOZIATION);
                break;
            case AGGREGATION:
                result.setReferenzierungsart(Referenzierungsart.AGGREGATION);
                break;
            case COMPOSITION:
                result.setReferenzierungsart(Referenzierungsart.KOMPOSITION);
                break;
            default:
                throw new IllegalStateException("Unbekannte Referenzierungsart: " + type.getReferenceType());
        }

    }

}
