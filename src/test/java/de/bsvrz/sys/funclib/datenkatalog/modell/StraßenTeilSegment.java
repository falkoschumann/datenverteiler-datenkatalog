/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.modell;

import de.bsvrz.sys.funclib.datenkatalog.bind.AttributDefinition;
import de.bsvrz.sys.funclib.datenkatalog.bind.AttributgruppenDefinition;

import java.util.Objects;

@AttributgruppenDefinition(pid = "atg.straßenTeilSegment")
public class StraßenTeilSegment {

    private long laenge;
    private byte anzahlFahrStreifen;
    private short steigungGefaelle;

    @AttributDefinition(name = "Länge")
    public long getLaenge() {
        return laenge;
    }

    public void setLaenge(long laenge) {
        this.laenge = laenge;
    }

    public byte getAnzahlFahrStreifen() {
        return anzahlFahrStreifen;
    }

    public void setAnzahlFahrStreifen(byte anzahlFahrStreifen) {
        this.anzahlFahrStreifen = anzahlFahrStreifen;
    }

    @AttributDefinition(name = "SteigungGefälle")
    public short getSteigungGefaelle() {
        return steigungGefaelle;
    }

    public void setSteigungGefaelle(short steigungGefaelle) {
        this.steigungGefaelle = steigungGefaelle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StraßenTeilSegment that = (StraßenTeilSegment) o;
        return Objects.equals(laenge, that.laenge) &&
                Objects.equals(anzahlFahrStreifen, that.anzahlFahrStreifen) &&
                Objects.equals(steigungGefaelle, that.steigungGefaelle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(laenge, anzahlFahrStreifen, steigungGefaelle);
    }

    @Override
    public String toString() {
        return "StraßenTeilSegment{" +
                "laenge=" + laenge +
                ", anzahlFahrStreifen=" + anzahlFahrStreifen +
                ", steigungGefaelle=" + steigungGefaelle +
                '}';
    }

}
