/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import java.util.Objects;

/**
 * Typ eines Wertebereichs.
 * <p><strong>Hinweis: </strong> Ein Wertebereich ist ein Wertobjekt und wird über seine Eigenschaften identifiziert.</p>
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class WerteBereich extends SystemObjekt {

    // TODO Soll SystemObjekt für Kurzinfo und Beschreibung abgeleitet werden?

    private long minimum;
    private long maximum;
    private double skalierung;
    private String einheit;

    public static WerteBereich erzeuge(long minimum, long maximum, double skalierung, String einheit) {
        WerteBereich result = new WerteBereich();
        result.minimum = minimum;
        result.maximum = maximum;
        result.skalierung = skalierung;
        result.einheit = einheit;
        return result;
    }

    private WerteBereich() {
        // value object
    }

    public long getMinimum() {
        return minimum;
    }

    public long getMaximum() {
        return maximum;
    }

    public double getSkalierung() {
        return skalierung;
    }

    public String getEinheit() {
        return einheit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WerteBereich that = (WerteBereich) o;
        return minimum == that.minimum &&
                maximum == that.maximum &&
                Double.compare(that.skalierung, skalierung) == 0 &&
                Objects.equals(einheit, that.einheit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(minimum, maximum, skalierung, einheit);
    }

}
