/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.generator.html;

import de.muspellheim.datenverteiler.datenkatalog.metamodell.Konfigurationsbereich;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.Konfigurationsverantwortlicher;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.Systemobjekt;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * HTML-Proxy für einen Konfigurationsverantwortlichen.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class KonfigurationsverantwortlicherHtmlProxy extends Konfigurationsverantwortlicher {

    private SortedSet<Konfigurationsbereich> konfigurationsbereiche = new TreeSet<>(Systemobjekt::compareToNameOderPid);

    public KonfigurationsverantwortlicherHtmlProxy(String pid) {
        super(pid);
    }

    public SortedSet<Konfigurationsbereich> getKonfigurationsbereiche() {
        return konfigurationsbereiche;
    }

}
