/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Beschreibt die Verwendung einer MengenDefinition bei einer TypDefinition.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class MengenVerwendung extends SystemObjekt {

    private String mengenName;
    private Set<Typ> objektTypen = new LinkedHashSet<>();

    /**
     * Persistenter Name der Menge.
     * <p>
     * Der Name unter dem die Menge ausgehend von einem Objekt des jeweiligen Typs referenzierbar ist.
     * </p>
     */
    public String getMengenName() {
        return mengenName;
    }

    /**
     * Persistenter Name der Menge.
     * <p>
     * Der Name unter dem die Menge ausgehend von einem Objekt des jeweiligen Typs referenzierbar ist.
     * </p>
     */
    public void setMengenName(String mengenName) {
        this.mengenName = mengenName;
    }


    /**
     * Enthält die möglichen Typen von Objekten, die in Mengen des jeweiligen MengenTyps verwendet werden können.
     */
    public Set<Typ> getObjektTypen() {
        return objektTypen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MengenVerwendung)) return false;
        if (!super.equals(o)) return false;
        MengenVerwendung that = (MengenVerwendung) o;
        return Objects.equals(getMengenName(), that.getMengenName()) &&
                Objects.equals(getObjektTypen(), that.getObjektTypen());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getMengenName(), getObjektTypen());
    }

    @Override
    public String toString() {
        return "MengenVerwendung{" +
                "name='" + getName() + '\'' +
                ", pid='" + getPid() + '\'' +
                ", kurzinfo='" + getKurzinfo() + '\'' +
                ", beschreibung='" + getBeschreibung() + '\'' +
                ", mengenName='" + mengenName + '\'' +
                ", objektTypen=" + objektTypen +
                "} " + super.toString();
    }

}
