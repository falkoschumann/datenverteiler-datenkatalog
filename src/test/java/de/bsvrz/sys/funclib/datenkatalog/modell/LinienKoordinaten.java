/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.modell;

import de.bsvrz.sys.funclib.datenkatalog.bind.AttributgruppenDefinition;
import de.bsvrz.sys.funclib.datenkatalog.util.Doubles;

import java.util.Arrays;
import java.util.Objects;

@AttributgruppenDefinition(pid = "atg.linienKoordinaten")
public class LinienKoordinaten {

    private double[] x;
    private double[] y;

    public double[] getX() {
        return x;
    }

    public void setX(double... x) {
        this.x = x;
    }

    public double[] getY() {
        return y;
    }

    public void setY(double... y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinienKoordinaten that = (LinienKoordinaten) o;
        return Doubles.equals(x, that.x, 0.0001) && Doubles.equals(y, that.y, 0.0001);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LinienKoordinaten{");
        sb.append("x=").append(Arrays.toString(x));
        sb.append(", y=").append(Arrays.toString(y));
        sb.append('}');
        return sb.toString();
    }

}
