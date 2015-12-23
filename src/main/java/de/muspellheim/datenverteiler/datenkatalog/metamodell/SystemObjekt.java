/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import java.util.Objects;

/**
 * Basisklasse für alle Objekte des Metamodells.
 * <p>Ein Systemobjekt ist ein Referenzobjekt, und wird über seine PID identifiziert.</p>
 *
 * @author Falko Schumann
 * @since 3.2
 */
public abstract class SystemObjekt {

    private String name;
    private String pid;
    private String kurzinfo;
    private String beschreibung;
    private KonfigurationsBereich bereich;

    public static int compareToNameOderPid(SystemObjekt so1, SystemObjekt so2) {
        if (Objects.equals(so1, so2)) return 0;

        int result = so1.getNameOderPid().compareToIgnoreCase(so2.getNameOderPid());
        if (result != 0) return result;

        return so1.getPid().compareToIgnoreCase(so2.getPid());
    }

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
        if (name != null && !name.isEmpty()) return name;
        if (pid != null && !pid.isEmpty()) return pid;
        return "";
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SystemObjekt)) return false;
        SystemObjekt that = (SystemObjekt) o;
        return Objects.equals(pid, that.pid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pid);
    }

    @Override
    public String toString() {
        String result = getClass().getSimpleName();
        result += name != null && !name.isEmpty() ? " " + name : "";
        result += pid != null && !pid.isEmpty() ? " (" + pid + ")" : "";
        return result;
    }

}
