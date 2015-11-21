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
public class KonfigurationsBereich {

    private String name;
    private Set<Typ> typen = new LinkedHashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Typ> getTypen() {
        return typen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KonfigurationsBereich that = (KonfigurationsBereich) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(typen, that.typen);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, typen);
    }

    @Override
    public String toString() {
        return "KonfigurationsBereich{" +
                "name='" + name + '\'' +
                ", typen=" + typen +
                '}';
    }

}
