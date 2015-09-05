/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.modell;

import de.muspellheim.datenverteiler.datenkatalog.bind.AttributDefinition;
import de.muspellheim.datenverteiler.datenkatalog.bind.AttributgruppenDefinition;

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
        final StringBuilder sb = new StringBuilder("StraßenTeilSegment{");
        sb.append("laenge=").append(laenge);
        sb.append(", anzahlFahrStreifen=").append(anzahlFahrStreifen);
        sb.append(", steigungGefaelle=").append(steigungGefaelle);
        sb.append('}');
        return sb.toString();
    }

}
