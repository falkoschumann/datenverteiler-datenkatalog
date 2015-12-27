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
public class Konfigurationsbereich extends Systemobjekt {

    private Konfigurationsverantwortlicher zustaendiger;
    private final Set<Systemobjekt> modell = new LinkedHashSet<>();

    public Konfigurationsbereich(String pid) {
        super(pid);
    }

    /**
     * Spezifiziert den Konfigurationsverantwortlichen für diesen Bereich.
     */
    public Konfigurationsverantwortlicher getZustaendiger() {
        return zustaendiger;
    }

    /**
     * Spezifiziert den Konfigurationsverantwortlichen für diesen Bereich.
     */
    public void setZustaendiger(Konfigurationsverantwortlicher zustaendiger) {
        this.zustaendiger = zustaendiger;
    }

    public Set<Systemobjekt> getModell() {
        return modell;
    }

}
