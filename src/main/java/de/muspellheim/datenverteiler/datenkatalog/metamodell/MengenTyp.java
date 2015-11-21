/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;


import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Typ der MengenDefinition.
 * <p>
 * Jede MengenDefinition wird als Objekt dieses Typs umgesetzt. Hier werden die Eigenschaften der MengenDefinitionen zusammengefasst.
 * </p>
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class MengenTyp {

    private Set<Typ> objektTypen = new LinkedHashSet<>();

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
        MengenTyp mengenTyp = (MengenTyp) o;
        return Objects.equals(objektTypen, mengenTyp.objektTypen);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objektTypen);
    }

    @Override
    public String toString() {
        return "MengenTyp{" +
                "objektTypen=" + objektTypen +
                '}';
    }
    
}
