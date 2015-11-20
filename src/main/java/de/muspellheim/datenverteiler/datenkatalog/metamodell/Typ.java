/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Der Typ aller Typobjekte.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class Typ {

    private String name;
    private Set<MengenVerwendung> mengen = new LinkedHashSet<>();
    private Set<Typ> superTypen = new LinkedHashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

}
