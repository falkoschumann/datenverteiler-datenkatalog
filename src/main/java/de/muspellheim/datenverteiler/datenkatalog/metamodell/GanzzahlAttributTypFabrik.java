/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.bsvrz.dav.daf.main.config.IntegerAttributeType;
import de.bsvrz.dav.daf.main.config.IntegerValueRange;
import de.bsvrz.dav.daf.main.config.IntegerValueState;
import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Fabrik zum Erzeugen von Ganzzahlattributtypen.
 *
 * @author Falko Schumann
 * @since 3.2
 */
class GanzzahlAttributtypFabrik extends AttributtypFabrik<GanzzahlAttributtyp> {

    GanzzahlAttributtypFabrik(Metamodell metamodell) {
        super(metamodell);
    }

    static boolean istAttributTyp(SystemObject systemObject) {
        return systemObject.isOfType("typ.ganzzahlAttributTyp");
    }

    @Override
    protected GanzzahlAttributtyp erzeugeObjekt(SystemObject objekt) {
        return GanzzahlAttributtyp.erzeugeMitPid(objekt.getPid());
    }

    @Override
    protected void initialisiereObjekt(SystemObject object, GanzzahlAttributtyp result) {
        super.initialisiereObjekt(object, result);

        IntegerAttributeType type = (IntegerAttributeType) object;
        switch (type.getByteCount()) {
            case 1:
                result.setAnzahlBytes(Datentypgroesse.BYTE);
                break;
            case 2:
                result.setAnzahlBytes(Datentypgroesse.SHORT);
                break;
            case 4:
                result.setAnzahlBytes(Datentypgroesse.INT);
                break;
            case 8:
                result.setAnzahlBytes(Datentypgroesse.LONG);
                break;
            default:
                throw new IllegalStateException("Unbekannte Datentypgröße: " + type.getByteCount());
        }
        IntegerValueRange range = type.getRange();
        if (range != null)
            result.setBereich(Wertebereich.erzeuge(range.getMinimum(), range.getMaximum(), range.getConversionFactor(), range.getUnit()));
        type.getStates().forEach(e -> result.getZustaende().add(erzeugeZustand(e)));
    }

    private Wertezustand erzeugeZustand(IntegerValueState state) {
        Wertezustand result = Wertezustand.erzeuge(state.getName(), state.getValue());
        result.setKurzinfo(state.getInfo().getShortInfo());
        result.setBeschreibung(state.getInfo().getDescription());
        return result;
    }

}
