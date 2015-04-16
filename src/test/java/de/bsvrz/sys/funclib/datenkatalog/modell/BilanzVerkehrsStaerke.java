/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.modell;

import de.bsvrz.sys.funclib.datenkatalog.bind.AttributgruppenDefinition;

import java.util.Objects;

@AttributgruppenDefinition
public class BilanzVerkehrsStaerke {

    private int qLkw;
    private int qPkw;

    public int getQLkw() {
        return qLkw;
    }

    public void setQLkw(int qLkw) {
        this.qLkw = qLkw;
    }

    public int getQPkw() {
        return qPkw;
    }

    public void setQPkw(int qPkw) {
        this.qPkw = qPkw;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BilanzVerkehrsStaerke that = (BilanzVerkehrsStaerke) o;
        return Objects.equals(qLkw, that.qLkw) &&
                Objects.equals(qPkw, that.qPkw);
    }

    @Override
    public int hashCode() {
        return Objects.hash(qLkw, qPkw);
    }

    @Override
    public String toString() {
        return "BilanzVerkehrsStaerke{" +
                "qLkw=" + qLkw +
                ", qPkw=" + qPkw +
                '}';
    }

}
