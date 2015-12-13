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
    private Set<Typ> typen = new LinkedHashSet<>();
    private Set<MengenTyp> mengen = new LinkedHashSet<>();

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

    public Set<SystemObjekt> getAlleObjekte() {
        Set<SystemObjekt> result = new LinkedHashSet<>();
        result.addAll(typen);
        result.addAll(mengen);
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
