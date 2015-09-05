/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.modell;

import de.bsvrz.sys.funclib.datenkatalog.bind.AttributgruppenDefinition;
import de.bsvrz.sys.funclib.datenkatalog.util.Doubles;

import java.util.Objects;

@AttributgruppenDefinition(pid = "atg.werteBereichsEigenschaften")
public class WerteBereichsEigenschaften {

    private long minimum;
    private long maximum;
    private double skalierung;
    private String einheit;

    public long getMinimum() {
        return minimum;
    }

    public void setMinimum(long minimum) {
        this.minimum = minimum;
    }

    public long getMaximum() {
        return maximum;
    }

    public void setMaximum(long maximum) {
        this.maximum = maximum;
    }

    public double getSkalierung() {
        return skalierung;
    }

    public void setSkalierung(double skalierung) {
        this.skalierung = skalierung;
    }

    public String getEinheit() {
        return einheit;
    }

    public void setEinheit(String einheit) {
        this.einheit = einheit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WerteBereichsEigenschaften that = (WerteBereichsEigenschaften) o;
        return Objects.equals(minimum, that.minimum) &&
                Objects.equals(maximum, that.maximum) &&
                Doubles.equals(skalierung, that.skalierung, 0.00001) &&
                Objects.equals(einheit, that.einheit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(minimum, maximum, skalierung, einheit);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WerteBereichsEigenschaften{");
        sb.append("minimum=").append(minimum);
        sb.append(", maximum=").append(maximum);
        sb.append(", skalierung=").append(skalierung);
        sb.append(", einheit='").append(einheit).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
