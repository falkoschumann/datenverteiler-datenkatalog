/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Der Typ aller Typobjekte.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class Typ extends SystemObjekt {

    private boolean dynamisch;
    private Set<MengenVerwendung> mengen = new LinkedHashSet<>();
    private Set<Typ> superTypen = new LinkedHashSet<>();

    public boolean isDynamisch() {
        return dynamisch;
    }

    public void setDynamisch(boolean dynamisch) {
        this.dynamisch = dynamisch;
    }

    /**
     * Führt die Mengen auf, die mit Objekten dieses Typs verwendet werden können oder müssen.
     */
    public Set<MengenVerwendung> getMengen() {
        return mengen;
    }

    /**
     * Jedem Typ ist eine Menge von Supertypen zugeordnet.
     * <p>
     * Supertypen sind die Typen, von dem der jeweilige Typ abgeleitet ist. Ein Typ erbt die Eigenschaften bezüglich der
     * verwendbaren Attributgruppen und Mengen von all seinen Supertypen.
     * </p>
     */
    public Set<Typ> getSuperTypen() {
        return superTypen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Typ)) return false;
        if (!super.equals(o)) return false;
        Typ typ = (Typ) o;
        return isDynamisch() == typ.isDynamisch() &&
                Objects.equals(getMengen(), typ.getMengen()) &&
                Objects.equals(getSuperTypen(), typ.getSuperTypen());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isDynamisch(), getMengen(), getSuperTypen());
    }

    @Override
    public String toString() {
        return "Typ{" +
                "name='" + getName() + '\'' +
                ", pid='" + getPid() + '\'' +
                ", kurzinfo='" + getKurzinfo() + '\'' +
                ", beschreibung='" + getBeschreibung() + '\'' +
                ", dynamisch=" + isDynamisch() +
                ", mengen=" + getMengen() +
                ", superTypen=" + getSuperTypen() +
                "}";
    }

}
