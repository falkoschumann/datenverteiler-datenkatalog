/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.generator.html;

import de.muspellheim.datenverteiler.datenkatalog.metamodell.*;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * HTML-Proxy für einen Konfigurationsbereich.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class KonfigurationsbereichHtmlProxy extends Konfigurationsbereich {

    public KonfigurationsbereichHtmlProxy(String pid) {
        super(pid);
    }

    public SortedSet<Typ> getTypen() {
        SortedSet<Typ> result = new TreeSet<>(Systemobjekt::compareToNameOderPid);
        getModell().stream().filter(this::istTyp).forEach(t -> result.add((Typ) t));
        return result;
    }

    private boolean istTyp(Systemobjekt so) {
        return so instanceof Typ && !(so instanceof Mengentyp);
    }

    public SortedSet<Mengentyp> getMengen() {
        SortedSet<Mengentyp> result = new TreeSet<>(Systemobjekt::compareToNameOderPid);
        getModell().stream().filter(this::istMenge).forEach(t -> result.add((Mengentyp) t));
        return result;
    }

    private boolean istMenge(Systemobjekt so) {
        return so instanceof Mengentyp;
    }

    public SortedSet<Attributgruppe> getAttributgruppen() {
        SortedSet<Attributgruppe> result = new TreeSet<>(Systemobjekt::compareToNameOderPid);
        getModell().stream().filter(this::istAttributgruppe).forEach(atg -> result.add((Attributgruppe) atg));
        return result;
    }

    private boolean istAttributgruppe(Systemobjekt so) {
        return so instanceof Attributgruppe;
    }

    public SortedSet<Attributliste> getAttributlisten() {
        SortedSet<Attributliste> result = new TreeSet<>(Systemobjekt::compareToNameOderPid);
        getModell().stream().filter(this::istAttributliste).forEach(atl -> result.add((Attributliste) atl));
        return result;
    }

    private boolean istAttributliste(Systemobjekt so) {
        return so instanceof Attributliste;
    }

    public SortedSet<Attributtyp> getAttributtypen() {
        SortedSet<Attributtyp> result = new TreeSet<>(Systemobjekt::compareToNameOderPid);
        getModell().stream().filter(this::istAttributtyp).forEach(att -> result.add((Attributtyp) att));
        return result;
    }

    private boolean istAttributtyp(Systemobjekt so) {
        return so instanceof Attributtyp;
    }

}
