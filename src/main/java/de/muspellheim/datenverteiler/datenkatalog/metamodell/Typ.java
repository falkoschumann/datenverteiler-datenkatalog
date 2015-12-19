/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Der Typ aller Typobjekte.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class Typ extends SystemObjekt {

    private final Set<Attributgruppe> attributgruppen = new LinkedHashSet<>();
    private final Set<MengenVerwendung> mengen = new LinkedHashSet<>();
    private final Set<Typ> superTypen = new TreeSet<>();
    private final Set<Typ> subTypen = new TreeSet<>();

    public static Typ erzeugeMitPid(String pid) {
        Typ result = new Typ();
        result.setPid(pid);
        return result;
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

    public Set<Typ> getSubTypen() {
        return subTypen;
    }

}
