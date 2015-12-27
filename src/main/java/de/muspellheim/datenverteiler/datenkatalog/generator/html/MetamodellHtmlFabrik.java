/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.generator.html;

import de.muspellheim.datenverteiler.datenkatalog.metamodell.Konfigurationsverantwortlicher;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.MetamodellFabrik;

/**
 * HTML-Proxy für einen Konfigurationsverantwortlichen.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class MetamodellHtmlFabrik extends MetamodellFabrik {

    @Override
    public Konfigurationsverantwortlicher erzeugeKonfigurationsverantwortlicher(String pid) {
        return new KonfigurationsverantwortlicherHtmlProxy(pid);
    }

}
