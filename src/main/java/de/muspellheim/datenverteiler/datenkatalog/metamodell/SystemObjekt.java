/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import java.util.Objects;

/**
 * Basisklasse für alle Objekte des Metamodells.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public abstract class SystemObjekt implements Comparable<SystemObjekt> {

    private String name;
    private String pid;
    private String kurzinfo;
    private String beschreibung;
    private KonfigurationsBereich bereich;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getNameOderPid() {
        if (name != null && !name.isEmpty())
            return name;

        return pid;
    }

    public String getKurzinfo() {
        return kurzinfo;
    }

    public void setKurzinfo(String kurzinfo) {
        this.kurzinfo = kurzinfo;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public KonfigurationsBereich getBereich() {
        return bereich;
    }

    public void setBereich(KonfigurationsBereich bereich) {
        this.bereich = bereich;
    }

    @Override
    public int compareTo(SystemObjekt s) {
        return name.compareToIgnoreCase(s.name);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SystemObjekt)) return false;
        SystemObjekt that = (SystemObjekt) o;
        return Objects.equals(pid, that.pid);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(pid);
    }

    @Override
    public String toString() {
        return "SystemObjekt{" +
                "name='" + name + '\'' +
                ", pid='" + pid + '\'' +
                ", kurzinfo='" + kurzinfo + '\'' +
                ", beschreibung='" + beschreibung + '\'' +
                '}';
    }

}
