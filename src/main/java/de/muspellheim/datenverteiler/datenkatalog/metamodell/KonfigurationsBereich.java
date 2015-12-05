/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Zur Gruppierung von Konfigurationsobjekten und Konfigurationsdaten nach modellspezifischen, inhaltlichen und
 * organisatorischen Gesichtspunkten.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class KonfigurationsBereich extends SystemObjekt {

    private KonfigurationsVerantwortlicher zustaender;
    private Set<Typ> typen = new LinkedHashSet<>();

    public KonfigurationsVerantwortlicher getZustaender() {
        return zustaender;
    }

    public void setZustaender(KonfigurationsVerantwortlicher zustaender) {
        this.zustaender = zustaender;
    }

    public Set<Typ> getTypen() {
        return typen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KonfigurationsBereich)) return false;
        if (!super.equals(o)) return false;
        KonfigurationsBereich that = (KonfigurationsBereich) o;
        return Objects.equals(getZustaender(), that.getZustaender()) && Objects.equals(getTypen(), that.getTypen());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getZustaender(), getTypen());
    }

    @Override
    public String toString() {
        return "KonfigurationsBereich{" +
                "name='" + getName() + '\'' +
                ", pid='" + getPid() + '\'' +
                ", kurzinfo='" + getKurzinfo() + '\'' +
                ", beschreibung='" + getBeschreibung() + '\'' +
                ", zustaender=" + zustaender +
                ", typen=" + typen +
                "}";
    }

}
