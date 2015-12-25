/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.bsvrz.dav.daf.main.config.IntegerAttributeType;
import de.bsvrz.dav.daf.main.config.IntegerValueRange;
import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Fabrik zum Erzeugen von Ganzzahlattributtypen.
 *
 * @author Falko Schumann
 * @since 3.2
 */
class GanzzahlAttributTypFabrik extends AttributTypFabrik<GanzzahlAttributTyp> {

    GanzzahlAttributTypFabrik(Metamodell metamodell) {
        super(metamodell);
    }

    static boolean istAttributTyp(SystemObject systemObject) {
        return systemObject.isOfType("typ.ganzzahlAttributTyp");
    }

    @Override
    protected GanzzahlAttributTyp erzeugeObjekt(SystemObject objekt) {
        return GanzzahlAttributTyp.erzeugeMitPid(objekt.getPid());
    }

    @Override
    protected void initialisiereObjekt(SystemObject object, GanzzahlAttributTyp result) {
        super.initialisiereObjekt(object, result);

        IntegerAttributeType type = (IntegerAttributeType) object;
        switch (type.getByteCount()) {
            case 1:
                result.setAnzahlBytes(DatentypGroesse.BYTE);
                break;
            case 2:
                result.setAnzahlBytes(DatentypGroesse.SHORT);
                break;
            case 4:
                result.setAnzahlBytes(DatentypGroesse.INT);
                break;
            case 8:
                result.setAnzahlBytes(DatentypGroesse.LONG);
                break;
            default:
                throw new IllegalStateException("Unbekannte Datentypgröße: " + type.getByteCount());
        }
        IntegerValueRange range = type.getRange();
        if (range != null)
            result.setBereich(WerteBereich.erzeuge(range.getMinimum(), range.getMaximum(), range.getConversionFactor(), range.getUnit()));
        type.getStates().forEach(e -> result.getZustaende().add(WerteZustand.erzeuge(e.getName(), e.getValue())));
    }

}
