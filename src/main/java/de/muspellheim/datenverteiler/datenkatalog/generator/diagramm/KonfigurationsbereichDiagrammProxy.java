/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.generator.diagramm;

import de.muspellheim.datenverteiler.datenkatalog.metamodell.Konfigurationsbereich;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.Mengentyp;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.Systemobjekt;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.Typ;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Diagramm-Proxy für einen Konfigurationsbereich.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class KonfigurationsbereichDiagrammProxy extends Konfigurationsbereich {

    public KonfigurationsbereichDiagrammProxy(String pid) {
        super(pid);
    }

    public Set<Typ> getTypen() {
        Set<Typ> result = new LinkedHashSet<>();
        getModell().stream().filter(this::istTyp).forEach(t -> result.add((Typ) t));
        return result;
    }

    private boolean istTyp(Systemobjekt so) {
        return so instanceof Typ && !(so instanceof Mengentyp);
    }

}
