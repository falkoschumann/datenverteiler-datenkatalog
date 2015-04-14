/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.modell;

import de.bsvrz.sys.funclib.datenkatalog.bind.AttributDefinition;
import de.bsvrz.sys.funclib.datenkatalog.bind.AttributfeldDefinition;
import de.bsvrz.sys.funclib.datenkatalog.bind.Zeitstempel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class StauVerlauf {

    private long schrittweite;
    private long dauer;
    private Date aufloesungsZeit;
    private long maxLaenge;
    private Date maxLaengeZeit;
    private List<StauVerlaufPrognoseSchritt> prognoseverlauf = new ArrayList<>();

    @Zeitstempel
    public long getSchrittweite() {
        return schrittweite;
    }

    public void setSchrittweite(long schrittweite) {
        this.schrittweite = schrittweite;
    }

    @Zeitstempel
    public long getDauer() {
        return dauer;
    }

    public void setDauer(long dauer) {
        this.dauer = dauer;
    }

    @AttributDefinition(name = "AuflösungsZeit")
    public Date getAufloesungsZeit() {
        return aufloesungsZeit;
    }

    public void setAufloesungsZeit(Date aufloesungsZeit) {
        this.aufloesungsZeit = aufloesungsZeit;
    }

    @AttributDefinition(name = "MaxLänge")
    public long getMaxLaenge() {
        return maxLaenge;
    }

    public void setMaxLaenge(long maxLaenge) {
        this.maxLaenge = maxLaenge;
    }

    @AttributDefinition(name = "MaxLängeZeit")
    public Date getMaxLaengeZeit() {
        return maxLaengeZeit;
    }

    public void setMaxLaengeZeit(Date maxLaengeZeit) {
        this.maxLaengeZeit = maxLaengeZeit;
    }

    @AttributfeldDefinition(elementtyp = StauVerlaufPrognoseSchritt.class)
    public List<StauVerlaufPrognoseSchritt> getPrognoseverlauf() {
        return prognoseverlauf;
    }

    public void setPrognoseverlauf(List<StauVerlaufPrognoseSchritt> prognoseverlauf) {
        this.prognoseverlauf = prognoseverlauf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StauVerlauf that = (StauVerlauf) o;
        return Objects.equals(schrittweite, that.schrittweite) &&
                Objects.equals(dauer, that.dauer) &&
                Objects.equals(maxLaenge, that.maxLaenge) &&
                Objects.equals(aufloesungsZeit, that.aufloesungsZeit) &&
                Objects.equals(maxLaengeZeit, that.maxLaengeZeit) &&
                Objects.equals(prognoseverlauf, that.prognoseverlauf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schrittweite, dauer, aufloesungsZeit, maxLaenge, maxLaengeZeit, prognoseverlauf);
    }

    @Override
    public String toString() {
        return "StauVerlauf{" +
                "schrittweite=" + schrittweite +
                ", dauer=" + dauer +
                ", aufloesungsZeit=" + aufloesungsZeit +
                ", maxLaenge=" + maxLaenge +
                ", maxLaengeZeit=" + maxLaengeZeit +
                ", prognoseverlauf=" + prognoseverlauf +
                '}';
    }

}
