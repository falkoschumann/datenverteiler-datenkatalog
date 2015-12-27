/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.generator.html;

import de.muspellheim.datenverteiler.datenkatalog.metamodell.Attributgruppe;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.Mengenverwendung;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.Systemobjekt;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.Typ;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * HTML-Proxy für einen Typ.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class TypHtmlProxy extends Typ {

    private final SortedSet<Attributgruppe> attributgruppen = new TreeSet<>(Systemobjekt::compareToNameOderPid);
    private final SortedSet<Mengenverwendung> mengen = new TreeSet<>(Mengenverwendung::compareToMengenname);
    private final SortedSet<Typ> supertypen = new TreeSet<>(Systemobjekt::compareToNameOderPid);
    private final SortedSet<Typ> subtypen = new TreeSet<>(Systemobjekt::compareToNameOderPid);

    public TypHtmlProxy(String pid) {
        super(pid);
    }

    public SortedSet<Attributgruppe> getAttributgruppen() {
        return attributgruppen;
    }

    public SortedSet<Mengenverwendung> getMengen() {
        return mengen;
    }

    public SortedSet<Typ> getSupertypen() {
        return supertypen;
    }

    public SortedSet<Typ> getSubtypen() {
        return subtypen;
    }

}
