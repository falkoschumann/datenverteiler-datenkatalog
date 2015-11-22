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
public class MengenVerwendung {

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
        if (o == null || getClass() != o.getClass()) return false;
        MengenVerwendung that = (MengenVerwendung) o;
        return Objects.equals(mengenName, that.mengenName) &&
                Objects.equals(objektTypen, that.objektTypen);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mengenName, objektTypen);
    }

    @Override
    public String toString() {
        return "MengenVerwendung{" +
                "mengenName='" + mengenName + '\'' +
                ", objektTypen=" + objektTypen +
                '}';
    }

}
