/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.bsvrz.dav.daf.main.config.SystemObject;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Fabrik zum Erzeugen von Konfigurationsverantwortlichen.
 *
 * @author Falko Schumann
 * @since 3.2
 */
class KonfigurationsVerantwortlicherFabrik extends SystemObjektFabrik<KonfigurationsVerantwortlicher> {

    KonfigurationsVerantwortlicherFabrik(Metamodell metamodell) {
        super(metamodell);
    }

    Set<KonfigurationsVerantwortlicher> getKonfigurationsverantwortliche() {
        return getMetamodell().getModel().getType("typ.konfigurationsVerantwortlicher").getElements().stream().
                map(k -> getObjekt(k.getPid())).
                collect(Collectors.toSet());
    }

    @Override
    protected KonfigurationsVerantwortlicher erzeugeObjekt(SystemObject objekt) {
        return KonfigurationsVerantwortlicher.erzeugeMitPid(objekt.getPid());
    }

}
