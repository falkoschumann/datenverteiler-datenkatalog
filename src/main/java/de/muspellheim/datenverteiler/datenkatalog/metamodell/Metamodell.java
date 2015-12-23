/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.bsvrz.dav.daf.main.config.ConfigurationArea;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;

import java.util.Set;

/**
 * Fabrik zum Erzeugen von Objekten des Metamodells.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class Metamodell {

    // TODO Set von TreeSet auf LinkedHashSet umstellen, damit Reihenfolge beibehalten wird

    private final DataModel model;

    private final KonfigurationsVerantwortlicherFabrik konfigurationsVerantwortlicherFabrik;
    private final KonfigurationsBereichFabrik konfigurationsBereichFabrik;
    private final TypFabrik typFabrik;
    private final MengenTypFabrik mengenTypFabrik;
    private final AttributgruppeFabrik attributgruppeFabrik;
    private final AttributListenDefinitionFabrik attributListenDefinitionFabrik;
    private final AttributTypFabrik attributTypFabrik;

    public Metamodell(DataModel model) {
        this.model = model;

        konfigurationsVerantwortlicherFabrik = new KonfigurationsVerantwortlicherFabrik(this);
        konfigurationsBereichFabrik = new KonfigurationsBereichFabrik(this);
        typFabrik = new TypFabrik(this);
        mengenTypFabrik = new MengenTypFabrik(this);
        attributgruppeFabrik = new AttributgruppeFabrik(this);
        attributListenDefinitionFabrik = new AttributListenDefinitionFabrik(this);
        attributTypFabrik = new AttributTypFabrik(this);
    }

    DataModel getModel() {
        return model;
    }

    void bestimmeSystemObjekt(SystemObject object, SystemObjekt result) {
        result.setName(object.getName());
        result.setKurzinfo(object.getInfo().getShortInfo().trim());
        result.setBeschreibung(object.getInfo().getDescription().trim());
        if (!(object instanceof ConfigurationArea))
            result.setBereich(konfigurationsBereichFabrik.getObjekt(object.getConfigurationArea().getPid()));
    }

    public Set<KonfigurationsVerantwortlicher> getKonfigurationsverantwortliche() {
        return konfigurationsVerantwortlicherFabrik.getKonfigurationsverantwortliche();
    }

    public KonfigurationsVerantwortlicher getKonfigurationsverantwortlicher(String pid) {
        return konfigurationsVerantwortlicherFabrik.getObjekt(pid);
    }

    public Set<KonfigurationsBereich> getKonfigurationsbereiche() {
        return konfigurationsBereichFabrik.getKonfigurationsbereiche();
    }

    public KonfigurationsBereich getKonfigurationsbereich(String pid) {
        return konfigurationsBereichFabrik.getObjekt(pid);
    }

    public Typ getTyp(String pid) {
        return typFabrik.getObjekt(pid);
    }

    public MengenTyp getMengenTyp(String pid) {
        return mengenTypFabrik.getObjekt(pid);
    }

    MengenVerwendung getMengenVerwendung(long id) {
        return mengenTypFabrik.getMengenVerwendung(id);
    }

    public Attributgruppe getAttributgruppe(String pid) {
        return attributgruppeFabrik.getObjekt(pid);
    }

    public AttributListenDefinition getAttributliste(String pid) {
        return attributListenDefinitionFabrik.getObjekt(pid);
    }

    AttributTyp getAttributTyp(String pid) {
        return attributTypFabrik.getObjekt(pid);
    }

    Attribut getAttribut(long id) {
        return attributTypFabrik.getAttribut(id);
    }

}
