/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.modell;

import de.bsvrz.sys.funclib.datenkatalog.bind.AttributDefinition;
import de.bsvrz.sys.funclib.datenkatalog.bind.AttributlistenDefinition;
import de.bsvrz.sys.funclib.datenkatalog.bind.Zeitstempel;

import java.util.Objects;

@AttributlistenDefinition
public class StauVerlaufPrognoseSchritt {

    private int zufluss;
    private int kapazitaet;
    private long laenge;
    private long verlustZeit;
    private short vKfz;

    public int getZufluss() {
        return zufluss;
    }

    public void setZufluss(int zufluss) {
        this.zufluss = zufluss;
    }

    @AttributDefinition(name = "Kapazität")
    public int getKapazitaet() {
        return kapazitaet;
    }

    public void setKapazitaet(int kapazitaet) {
        this.kapazitaet = kapazitaet;
    }

    @AttributDefinition(name = "Länge")
    public long getLaenge() {
        return laenge;
    }

    public void setLaenge(long laenge) {
        this.laenge = laenge;
    }

    @Zeitstempel
    public long getVerlustZeit() {
        return verlustZeit;
    }

    public void setVerlustZeit(long verlustZeit) {
        this.verlustZeit = verlustZeit;
    }

    public short getVKfz() {
        return vKfz;
    }

    public void setVKfz(short vKfz) {
        this.vKfz = vKfz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StauVerlaufPrognoseSchritt that = (StauVerlaufPrognoseSchritt) o;
        return Objects.equals(zufluss, that.zufluss) &&
                Objects.equals(kapazitaet, that.kapazitaet) &&
                Objects.equals(laenge, that.laenge) &&
                Objects.equals(verlustZeit, that.verlustZeit) &&
                Objects.equals(vKfz, that.vKfz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zufluss, kapazitaet, laenge, verlustZeit, vKfz);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StauVerlaufPrognoseSchritt{");
        sb.append("zufluss=").append(zufluss);
        sb.append(", kapazitaet=").append(kapazitaet);
        sb.append(", laenge=").append(laenge);
        sb.append(", verlustZeit=").append(verlustZeit);
        sb.append(", vKfz=").append(vKfz);
        sb.append('}');
        return sb.toString();
    }

}
