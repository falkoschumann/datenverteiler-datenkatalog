/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.modell;

import de.bsvrz.sys.funclib.datenkatalog.bind.AttributlistenDefinition;
import de.bsvrz.sys.funclib.datenkatalog.util.Doubles;

import java.util.Objects;

@AttributlistenDefinition
public class Guete {

    private double index;
    private GueteVerfahren verfahren;

    public double getIndex() {
        return index;
    }

    public void setIndex(double index) {
        this.index = index;
    }

    public GueteVerfahren getVerfahren() {
        return verfahren;
    }

    public void setVerfahren(GueteVerfahren verfahren) {
        this.verfahren = verfahren;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guete guete = (Guete) o;
        return Doubles.equals(index, guete.index, 0.00001) &&
                Objects.equals(verfahren, guete.verfahren);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, verfahren);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Guete{");
        sb.append("index=").append(index);
        sb.append(", verfahren=").append(verfahren);
        sb.append('}');
        return sb.toString();
    }

}
