/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.bsvrz.dav.daf.main.config.AttributeGroupUsage;
import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Fabrik zum Erzeugen von Attributgruppenverwendungen.
 *
 * @author Falko Schumann
 * @since 3.2
 */
class AttributgruppenVerwendungFabrik extends SystemobjektFabrik<AttributgruppenVerwendung> {

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

    @Override
    protected void initialisiereObjekt(SystemObject object, AttributgruppenVerwendung result) {
        super.initialisiereObjekt(object, result);

        AttributeGroupUsage usage = (AttributeGroupUsage) object;
        result.setAttributgruppe(getMetamodell().getAttributgruppe(usage.getAttributeGroup().getPid()));
        result.setAspekt(getMetamodell().getAspekt(usage.getAspect().getPid()));
        result.setVerwendungExplizitVorgegeben(usage.isExplicitDefined());
        result.setDatensatzVerwendung(datensatzVerwendung(usage.getUsage()));
    }

    private Datensatzverwendung datensatzVerwendung(AttributeGroupUsage.Usage usage) {
        switch (usage.getId()) {
            case 1:
                return Datensatzverwendung.KONFIGURIERENDER_DATENSATZ_NOTWENDIG;
            case 2:
                return Datensatzverwendung.KONFIGURIERENDER_DATENSATZ_NOTWENDIG_UND_AENDERBAR;
            case 3:
                return Datensatzverwendung.KONFIGURIERENDER_DATENSATZ_OPTIONAL;
            case 4:
                return Datensatzverwendung.KONFIGURIERENDER_DATENSATZ_OPTIONALUND_AENDERBAR;
            case 5:
                return Datensatzverwendung.ONLINE_DATENSATZ_QUELLE;
            case 6:
                return Datensatzverwendung.ONLINE_DATENSATZ_SENKE;
            case 7:
                return Datensatzverwendung.ONLINE_DATENSATZ_QUELLE_UND_SENKE;
            default:
                throw new IllegalStateException("Unbekannte Datensatzverwendung: " + usage);
        }
    }

}
