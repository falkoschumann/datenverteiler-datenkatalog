/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.modell;

import de.bsvrz.sys.funclib.datenkatalog.bind.AttributlistenDefinition;

import java.util.Objects;

@AttributlistenDefinition
public class StatusPlausibilitaetsPruefungFormal {

    private boolean wertMax;
    private boolean wertMin;

    public boolean isWertMax() {
        return wertMax;
    }

    public void setWertMax(boolean wertMax) {
        this.wertMax = wertMax;
    }

    public boolean isWertMin() {
        return wertMin;
    }

    public void setWertMin(boolean wertMin) {
        this.wertMin = wertMin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatusPlausibilitaetsPruefungFormal that = (StatusPlausibilitaetsPruefungFormal) o;
        return Objects.equals(wertMax, that.wertMax) &&
                Objects.equals(wertMin, that.wertMin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wertMax, wertMin);
    }

    @Override
    public String toString() {
        return "StatusPlausibilitaetsPruefungFormal{" +
                "wertMax=" + wertMax +
                ", wertMin=" + wertMin +
                '}';
    }

}
