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
public final class Wertebereich extends Systemobjekt {

    private long minimum;
    private long maximum;
    private double skalierung;
    private String einheit;

    private Wertebereich() {
        super(null);
    }

    public static Wertebereich erzeuge(long minimum, long maximum, double skalierung, String einheit) {
        Wertebereich result = new Wertebereich();
        result.minimum = minimum;
        result.maximum = maximum;
        result.skalierung = skalierung;
        result.einheit = einheit;
        return result;
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
        Wertebereich that = (Wertebereich) o;
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
