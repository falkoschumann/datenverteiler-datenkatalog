/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.bsvrz.dav.daf.main.config.Attribute;
import de.bsvrz.dav.daf.main.config.AttributeType;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.ObjectSetUse;

import java.util.Set;

/**
 * Fabrik zum Erzeugen von Objekten des Metamodells.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class Metamodell {

    // TODO Defaultwerte abbilden
    // TODO Defaultparameter abbilden
    // TODO Konfigurationsobjekte mit Mengen und Konfigurationsdaten abbilden

    private final DataModel model;

    private final KonfigurationsverantwortlicherFabrik konfigurationsVerantwortlicherFabrik = new KonfigurationsverantwortlicherFabrik(this);
    private final KonfigurationsbereichFabrik konfigurationsBereichFabrik = new KonfigurationsbereichFabrik(this);
    private final TypFabrik typFabrik = new TypFabrik(this);
    private final MengentypFabrik mengenTypFabrik = new MengentypFabrik(this);
    private final AttributgruppeFabrik attributgruppeFabrik = new AttributgruppeFabrik(this);
    private final AttributlisteFabrik attributListenDefinitionFabrik = new AttributlisteFabrik(this);
    private final ZeichenkettenAttributtypFabrik zeichenkettenAttributTypFabrik = new ZeichenkettenAttributtypFabrik(this);
    private final ZeitstempelAttributtypFabrik zeitstempelAttributTypFabrik = new ZeitstempelAttributtypFabrik(this);
    private final KommazahlAttributtypFabrik kommazahlAttributTypFabrik = new KommazahlAttributtypFabrik(this);
    private final ObjektreferenzAttributtypFabrik objektReferenAttributTypFabrik = new ObjektreferenzAttributtypFabrik(this);
    private final GanzzahlAttributtypFabrik ganzzahlAttributTypFabrik = new GanzzahlAttributtypFabrik(this);
    private final AspektFabrik aspektFabrik = new AspektFabrik(this);
    private final AttributgruppenVerwendungFabrik attributgruppenVerwendungFabrik = new AttributgruppenVerwendungFabrik(this);

    public Metamodell(DataModel model) {
        this.model = model;
    }

    DataModel getModel() {
        return model;
    }

    public Set<Konfigurationsverantwortlicher> getKonfigurationsverantwortliche() {
        return konfigurationsVerantwortlicherFabrik.getKonfigurationsverantwortliche();
    }

    public Konfigurationsverantwortlicher getKonfigurationsverantwortlicher(String pid) {
        return konfigurationsVerantwortlicherFabrik.getObjekt(pid);
    }

    public Set<Konfigurationsbereich> getKonfigurationsbereiche() {
        return konfigurationsBereichFabrik.getKonfigurationsbereiche();
    }

    public Konfigurationsbereich getKonfigurationsbereich(String pid) {
        return konfigurationsBereichFabrik.getObjekt(pid);
    }

    public Typ getTyp(String pid) {
        return typFabrik.getObjekt(pid);
    }

    public Mengentyp getMengenTyp(String pid) {
        return mengenTypFabrik.getObjekt(pid);
    }

    Mengenverwendung getMengenVerwendung(long id) {
        ObjectSetUse objectSetUse = (ObjectSetUse) model.getObject(id);
        return Mengenverwendung.erzeuge(objectSetUse.getObjectSetName(), getMengenTyp(objectSetUse.getObjectSetType().getPid()), objectSetUse.isRequired());
    }

    public Attributgruppe getAttributgruppe(String pid) {
        return attributgruppeFabrik.getObjekt(pid);
    }

    public Attributliste getAttributliste(String pid) {
        return attributListenDefinitionFabrik.getObjekt(pid);
    }

    public Aspekt getAspekt(String pid) {
        return aspektFabrik.getObjekt(pid);
    }

    AttributgruppenVerwendung getAttributgruppenVerwendung(String pid) {
        return attributgruppenVerwendungFabrik.getObjekt(pid);
    }

    Attributtyp getAttributTyp(String pid) {
        AttributeType attributeType = model.getAttributeType(pid);
        if (AttributlisteFabrik.istAttributliste(attributeType))
            return attributListenDefinitionFabrik.getObjekt(pid);
        if (ZeichenkettenAttributtypFabrik.istAttributTyp(attributeType))
            return zeichenkettenAttributTypFabrik.getObjekt(pid);
        if (ZeitstempelAttributtypFabrik.istAttributTyp(attributeType))
            return zeitstempelAttributTypFabrik.getObjekt(pid);
        if (KommazahlAttributtypFabrik.istAttributTyp(attributeType))
            return kommazahlAttributTypFabrik.getObjekt(pid);
        if (ObjektreferenzAttributtypFabrik.istAttributTyp(attributeType))
            return objektReferenAttributTypFabrik.getObjekt(pid);
        if (GanzzahlAttributtypFabrik.istAttributTyp(attributeType))
            return ganzzahlAttributTypFabrik.getObjekt(pid);
        throw new IllegalStateException("Unbekannter Attributtyp: " + attributeType);
    }

    Attribut getAttribut(long id) {
        Attribute attribute = (Attribute) model.getObject(id);
        return Attribut.erzeuge(attribute.getName(), attribute.getPosition(), attribute.getMaxCount(), attribute.isCountVariable(), getAttributTyp(attribute.getAttributeType().getPid()));
    }

}
