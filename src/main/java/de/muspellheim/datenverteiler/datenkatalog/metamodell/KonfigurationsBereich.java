/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import java.util.Set;
import java.util.TreeSet;

/**
 * Zur Gruppierung von Konfigurationsobjekten und Konfigurationsdaten nach modellspezifischen, inhaltlichen und
 * organisatorischen Gesichtspunkten.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class KonfigurationsBereich extends SystemObjekt {

    // TODO Attributgruppen ergänzen

    private KonfigurationsVerantwortlicher zustaendiger;
    private final Set<Typ> typen = new TreeSet<>();
    private final Set<MengenTyp> mengen = new TreeSet<>();
    private final Set<Attributgruppe> attributgruppen = new TreeSet<>();

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

    public Set<SystemObjekt> getAlleObjekte() {
        Set<SystemObjekt> result = new TreeSet<>();
        result.addAll(typen);
        result.addAll(mengen);
        result.addAll(attributgruppen);
        return result;
    }

    @Override
    public String toString() {
        return "KonfigurationsBereich{" +
                "name='" + getName() + '\'' +
                ", pid='" + getPid() + '\'' +
                ", kurzinfo='" + getKurzinfo() + '\'' +
                ", beschreibung='" + getBeschreibung() + '\'' +
                ", zustaendiger=" + zustaendiger +
                ", typen=" + typen +
                "}";
    }

}
