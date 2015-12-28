/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.generator.diagramm;

import de.bsvrz.dav.daf.main.config.DataModel;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.Konfigurationsbereich;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.Metamodell;

/**
 * Diagramm-Proxy für das Metamodell.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class MetamodellDiagrammProxy extends Metamodell {

    public MetamodellDiagrammProxy(DataModel model) {
        super(model);
    }

    @Override
    protected Konfigurationsbereich erzeugeKonfigurationsbereich(String pid) {
        return new KonfigurationsbereichDiagrammProxy(pid);
    }

}
