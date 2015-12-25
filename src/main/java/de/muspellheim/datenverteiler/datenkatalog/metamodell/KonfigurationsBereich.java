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
public class KonfigurationsBereich extends SystemObjekt {

    private KonfigurationsVerantwortlicher zustaendiger;
    private final Set<Typ> typen = new LinkedHashSet<>();
    private final Set<MengenTyp> mengen = new LinkedHashSet<>();
    private final Set<Attributgruppe> attributgruppen = new LinkedHashSet<>();
    private final Set<AttributListenDefinition> attributlisten = new LinkedHashSet<>();
    private final Set attributtypen = new LinkedHashSet<>();
    private final Set aspekte = new LinkedHashSet<>();

    public static KonfigurationsBereich erzeugeMitPid(String pid) {
        KonfigurationsBereich result = new KonfigurationsBereich();
        result.setPid(pid);
        return result;
    }

    /**
     * Spezifiziert den Konfigurationsverantwortlichen für diesen Bereich.
     */
    public KonfigurationsVerantwortlicher getZustaendiger() {
        return zustaendiger;
    }

    /**
     * Spezifiziert den Konfigurationsverantwortlichen für diesen Bereich.
     */
    public void setZustaendiger(KonfigurationsVerantwortlicher zustaendiger) {
        this.zustaendiger = zustaendiger;
    }

    public Set<Typ> getTypen() {
        return typen;
    }

    public Set<MengenTyp> getMengen() {
        return mengen;
    }

    public Set<Attributgruppe> getAttributgruppen() {
        return attributgruppen;
    }

    public Set<AttributListenDefinition> getAttributlisten() {
        return attributlisten;
    }

    public Set<AttributTyp> getAttributtypen() {
        return attributtypen;
    }

    public Set getAspekte() {
        return aspekte;
    }

    public Set<SystemObjekt> getAlleObjekte() {
        Set<SystemObjekt> result = new LinkedHashSet<>();
        result.addAll(typen);
        result.addAll(mengen);
        result.addAll(attributgruppen);
        result.addAll(attributlisten);
        result.addAll(attributtypen);
        result.addAll(aspekte);
        return result;
    }

}
