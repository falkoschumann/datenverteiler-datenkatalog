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
public class Typ extends Systemobjekt {

    private final Set<Attributgruppe> attributgruppen = new LinkedHashSet<>();
    private final Set<Mengenverwendung> mengen = new LinkedHashSet<>();
    private final Set<Typ> supertypen = new LinkedHashSet<>();
    private final Set<Typ> subtypen = new LinkedHashSet<>();

    public Typ(String pid) {
        super(pid);
    }

    /**
     * Jedem Typ ist eine Menge von Attributgruppen zugeordnet.
     */
    public Set<Attributgruppe> getAttributgruppen() {
        return attributgruppen;
    }

    /**
     * Führt die Mengen auf, die mit Objekten dieses Typs verwendet werden können oder müssen.
     */
    public Set<Mengenverwendung> getMengen() {
        return mengen;
    }

    /**
     * Jedem Typ ist eine Menge von Supertypen zugeordnet.
     * <p>
     * Supertypen sind die Typen, von dem der jeweilige Typ abgeleitet ist. Ein Typ erbt die Eigenschaften bezüglich der
     * verwendbaren Attributgruppen und Mengen von all seinen Supertypen.
     * </p>
     */
    public Set<Typ> getSupertypen() {
        return supertypen;
    }

    public Set<Typ> getSubtypen() {
        return subtypen;
    }

}