/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import java.util.LinkedHashSet;
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

}
