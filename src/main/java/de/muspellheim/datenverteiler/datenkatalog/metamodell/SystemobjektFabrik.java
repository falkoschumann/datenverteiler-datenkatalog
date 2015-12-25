/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.bsvrz.dav.daf.main.config.ConfigurationArea;
import de.bsvrz.dav.daf.main.config.SystemObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Fabrikmethode für Systemobjekte.
 *
 * @author Falko Schumann
 * @since 3.2
 */
abstract class SystemobjektFabrik<T extends Systemobjekt> {

    private final Map<String, T> objekte = new LinkedHashMap<>();

    private final Metamodell metamodell;

    protected SystemobjektFabrik(Metamodell metamodell) {
        this.metamodell = metamodell;
    }

    protected Metamodell getMetamodell() {
        return metamodell;
    }

    final T getObjekt(String pid) {
        if (objekte.containsKey(pid))
            return objekte.get(pid);

        SystemObject objekt = metamodell.getModel().getObject(pid);
        T result = erzeugeObjekt(objekt);
        objekte.put(pid, result);
        initialisiereObjekt(objekt, result);
        return result;
    }

    protected abstract T erzeugeObjekt(SystemObject objekt);

    protected void initialisiereObjekt(SystemObject object, T result) {
        result.setName(object.getName());
        result.setKurzinfo(object.getInfo().getShortInfo().trim());
        result.setBeschreibung(object.getInfo().getDescription().trim());
        if (!(object instanceof ConfigurationArea))
            result.setKonfigurationsbereich(metamodell.getKonfigurationsbereich(object.getConfigurationArea().getPid()));
    }

}
