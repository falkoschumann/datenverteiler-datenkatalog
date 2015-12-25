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
class KonfigurationsbereichFabrik extends SystemobjektFabrik<Konfigurationsbereich> {

    KonfigurationsbereichFabrik(Metamodell metamodell) {
        super(metamodell);
    }

    Set<Konfigurationsbereich> getKonfigurationsbereiche() {
        return getMetamodell().getModel().getType("typ.konfigurationsBereich").getElements().stream().
                map(k -> getObjekt(k.getPid())).
                collect(Collectors.toSet());
    }

    @Override
    protected Konfigurationsbereich erzeugeObjekt(SystemObject objekt) {
        return Konfigurationsbereich.erzeugeMitPid(objekt.getPid());
    }

    @Override
    protected void initialisiereObjekt(SystemObject object, Konfigurationsbereich result) {
        super.initialisiereObjekt(object, result);

        ConfigurationArea area = (ConfigurationArea) object;
        result.setZustaendiger(getMetamodell().getKonfigurationsverantwortlicher(area.getConfigurationAuthority().getPid()));
        area.getCurrentObjects().stream().filter(TypFabrik::istTyp).forEach(e -> result.getModell().add(getMetamodell().getTyp(e.getPid())));
        area.getCurrentObjects().stream().filter(MengentypFabrik::istMengenTyp).forEach(e -> result.getModell().add(getMetamodell().getMengenTyp(e.getPid())));
        area.getCurrentObjects().stream().filter(AttributgruppeFabrik::istAttributgruppe).forEach(e -> result.getModell().add(getMetamodell().getAttributgruppe(e.getPid())));
        area.getCurrentObjects().stream().filter(AttributlisteFabrik::istAttributliste).forEach(e -> result.getModell().add(getMetamodell().getAttributliste(e.getPid())));
        area.getCurrentObjects().stream().filter(AttributtypFabrik::istAttributTyp).forEach(e -> result.getModell().add(getMetamodell().getAttributTyp(e.getPid())));
        area.getCurrentObjects().stream().filter(AspektFabrik::istAspekt).forEach(e -> result.getModell().add(getMetamodell().getAspekt(e.getPid())));
    }

}
