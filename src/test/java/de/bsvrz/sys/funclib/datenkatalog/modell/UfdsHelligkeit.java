/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.modell;

import de.bsvrz.sys.funclib.datenkatalog.bind.AttributgruppenDefinition;
import de.bsvrz.sys.funclib.datenkatalog.bind.Zeitstempel;

import java.text.MessageFormat;
import java.util.Objects;

@AttributgruppenDefinition(pid = "atg.ufdsHelligkeit")
public class UfdsHelligkeit {

    private final Helligkeit helligkeit = new Helligkeit();
    private long t;

    @Zeitstempel
    public long getT() {
        return t;
    }

    public void setT(long t) {
        this.t = t;
    }

    public Helligkeit getHelligkeit() {
        return helligkeit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UfdsHelligkeit that = (UfdsHelligkeit) o;
        return Objects.equals(t, that.t) &&
                Objects.equals(helligkeit, that.helligkeit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(t, helligkeit);
    }

    @Override
    public String toString() {
        return MessageFormat.format("UfdsHelligkeit'{'t={0}, helligkeit={1}'}'", t, helligkeit);
    }

}
