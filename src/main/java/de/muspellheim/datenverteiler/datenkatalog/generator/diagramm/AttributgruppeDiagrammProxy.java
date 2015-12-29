/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.generator.diagramm;

import de.muspellheim.datenverteiler.datenkatalog.metamodell.*;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Diagramm-Proxy für eine Attributgruppe.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class AttributgruppeDiagrammProxy extends Attributgruppe {

    public AttributgruppeDiagrammProxy(String pid) {
        super(pid);
    }

    public Set<Attribut> getReferenzen() {
        return bestimmeObjektreferenzen(this);
    }

    private Set<Attribut> bestimmeObjektreferenzen(Attributmenge attributmenge) {
        Set<Attribut> result = new LinkedHashSet<>();
        for (Attribut e : attributmenge.getAttribute()) {
            if (e.getAttributtyp() instanceof ObjektreferenzAttributtyp) {
                result.add(e);
            } else if (e.getAttributtyp() instanceof Attributliste && !"atl.urlasser".equals(e.getAttributtyp().getPid()))
                result.addAll(bestimmeObjektreferenzen((Attributmenge) e.getAttributtyp()));
        }
        return result;
    }

}
