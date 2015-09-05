/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.modell;

import de.bsvrz.sys.funclib.datenkatalog.bind.AttributDefinition;
import de.bsvrz.sys.funclib.datenkatalog.bind.AttributlistenDefinition;

import java.util.Objects;

@AttributlistenDefinition
public class Helligkeit {

    private int wert;
    private Status status = new Status();
    private Guete guete = new Guete();

    public int getWert() {
        return wert;
    }

    public void setWert(int wert) {
        this.wert = wert;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @AttributDefinition(name = "Güte")
    public Guete getGuete() {
        return guete;
    }

    public void setGuete(Guete guete) {
        this.guete = guete;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Helligkeit that = (Helligkeit) o;
        return Objects.equals(wert, that.wert) &&
                Objects.equals(status, that.status) &&
                Objects.equals(guete, that.guete);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wert, status, guete);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Helligkeit{");
        sb.append("wert=").append(wert);
        sb.append(", status=").append(status);
        sb.append(", guete=").append(guete);
        sb.append('}');
        return sb.toString();
    }

}
