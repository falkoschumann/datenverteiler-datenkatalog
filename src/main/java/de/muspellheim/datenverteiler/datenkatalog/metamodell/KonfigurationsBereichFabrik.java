/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.bsvrz.dav.daf.main.config.ConfigurationArea;
import de.bsvrz.dav.daf.main.config.SystemObject;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Fabrik zum Erzeugen von Konfigurationsbereichen.
 *
 * @author Falko Schumann
 * @since 3.2
 */
class KonfigurationsBereichFabrik extends SystemObjektFabrik<KonfigurationsBereich> {

    KonfigurationsBereichFabrik(Metamodell metamodell) {
        super(metamodell);
    }

    Set<KonfigurationsBereich> getKonfigurationsbereiche() {
        return getMetamodell().getModel().getType("typ.konfigurationsBereich").getElements().stream().
                map(k -> getObjekt(k.getPid())).
                collect(Collectors.toSet());
    }

    @Override
    protected KonfigurationsBereich erzeugeObjekt(SystemObject objekt) {
        return KonfigurationsBereich.erzeugeMitPid(objekt.getPid());
    }

    @Override
    protected void initialisiereObjekt(SystemObject object, KonfigurationsBereich result) {
        super.initialisiereObjekt(object, result);

        ConfigurationArea area = (ConfigurationArea) object;
        result.setZustaendiger(getMetamodell().getKonfigurationsverantwortlicher(area.getConfigurationAuthority().getPid()));
        area.getCurrentObjects().stream().filter(TypFabrik::istTyp).forEach(e -> result.getTypen().add(getMetamodell().getTyp(e.getPid())));
        area.getCurrentObjects().stream().filter(MengenTypFabrik::istMengenTyp).forEach(e -> result.getMengen().add(getMetamodell().getMengenTyp(e.getPid())));
        area.getCurrentObjects().stream().filter(AttributgruppeFabrik::istAttributgruppe).forEach(e -> result.getAttributgruppen().add(getMetamodell().getAttributgruppe(e.getPid())));
        area.getCurrentObjects().stream().filter(AttributListenDefinitionFabrik::istAttributliste).forEach(e -> result.getAttributlisten().add(getMetamodell().getAttributliste(e.getPid())));
        area.getCurrentObjects().stream().filter(AttributTypFabrik::istAttributTyp).forEach(e -> result.getAttributtypen().add(getMetamodell().getAttributTyp(e.getPid())));
    }

}
