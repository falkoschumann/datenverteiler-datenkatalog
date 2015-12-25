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

    private final KonfigurationsverantwortlicherFabrik konfigurationsverantwortlicherFabrik = new KonfigurationsverantwortlicherFabrik(this);
    private final KonfigurationsbereichFabrik konfigurationsbereichFabrik = new KonfigurationsbereichFabrik(this);
    private final TypFabrik typFabrik = new TypFabrik(this);
    private final MengentypFabrik mengentypFabrik = new MengentypFabrik(this);
    private final AttributgruppeFabrik attributgruppeFabrik = new AttributgruppeFabrik(this);
    private final AttributlisteFabrik attributlisteFabrik = new AttributlisteFabrik(this);
    private final ZeichenkettenAttributtypFabrik zeichenkettenAttributtypFabrik = new ZeichenkettenAttributtypFabrik(this);
    private final ZeitstempelAttributtypFabrik zeitstempelAttributtypFabrik = new ZeitstempelAttributtypFabrik(this);
    private final KommazahlAttributtypFabrik kommazahlAttributTypFabrik = new KommazahlAttributtypFabrik(this);
    private final ObjektreferenzAttributtypFabrik objektreferenAttributtypFabrik = new ObjektreferenzAttributtypFabrik(this);
    private final GanzzahlAttributtypFabrik ganzzahlAttributtypFabrik = new GanzzahlAttributtypFabrik(this);
    private final AspektFabrik aspektFabrik = new AspektFabrik(this);
    private final AttributgruppenverwendungFabrik attributgruppenverwendungFabrik = new AttributgruppenverwendungFabrik(this);

    public Metamodell(DataModel model) {
        this.model = model;
    }

    DataModel getModel() {
        return model;
    }

    public Set<Konfigurationsverantwortlicher> getKonfigurationsverantwortliche() {
        return konfigurationsverantwortlicherFabrik.getKonfigurationsverantwortliche();
    }

    public Konfigurationsverantwortlicher getKonfigurationsverantwortlicher(String pid) {
        return konfigurationsverantwortlicherFabrik.getObjekt(pid);
    }

    public Set<Konfigurationsbereich> getKonfigurationsbereiche() {
        return konfigurationsbereichFabrik.getKonfigurationsbereiche();
    }

    public Konfigurationsbereich getKonfigurationsbereich(String pid) {
        return konfigurationsbereichFabrik.getObjekt(pid);
    }

    public Typ getTyp(String pid) {
        return typFabrik.getObjekt(pid);
    }

    public Mengentyp getMengentyp(String pid) {
        return mengentypFabrik.getObjekt(pid);
    }

    Mengenverwendung getMengenverwendung(long id) {
        ObjectSetUse objectSetUse = (ObjectSetUse) model.getObject(id);
        return Mengenverwendung.erzeuge(objectSetUse.getObjectSetName(), getMengentyp(objectSetUse.getObjectSetType().getPid()), objectSetUse.isRequired());
    }

    public Attributgruppe getAttributgruppe(String pid) {
        return attributgruppeFabrik.getObjekt(pid);
    }

    public Attributliste getAttributliste(String pid) {
        return attributlisteFabrik.getObjekt(pid);
    }

    public Aspekt getAspekt(String pid) {
        return aspektFabrik.getObjekt(pid);
    }

    Attributgruppenverwendung getAttributgruppenverwendung(String pid) {
        return attributgruppenverwendungFabrik.getObjekt(pid);
    }

    Attributtyp getAttributtyp(String pid) {
        AttributeType attributeType = model.getAttributeType(pid);
        if (AttributlisteFabrik.istAttributliste(attributeType))
            return attributlisteFabrik.getObjekt(pid);
        if (ZeichenkettenAttributtypFabrik.istAttributTyp(attributeType))
            return zeichenkettenAttributtypFabrik.getObjekt(pid);
        if (ZeitstempelAttributtypFabrik.istAttributTyp(attributeType))
            return zeitstempelAttributtypFabrik.getObjekt(pid);
        if (KommazahlAttributtypFabrik.istAttributTyp(attributeType))
            return kommazahlAttributTypFabrik.getObjekt(pid);
        if (ObjektreferenzAttributtypFabrik.istAttributTyp(attributeType))
            return objektreferenAttributtypFabrik.getObjekt(pid);
        if (GanzzahlAttributtypFabrik.istAttributTyp(attributeType))
            return ganzzahlAttributtypFabrik.getObjekt(pid);
        throw new IllegalStateException("Unbekannter Attributtyp: " + attributeType);
    }

    Attribut getAttribut(long id) {
        Attribute attribute = (Attribute) model.getObject(id);
        return Attribut.erzeuge(attribute.getName(), attribute.getPosition(), attribute.getMaxCount(), attribute.isCountVariable(), getAttributtyp(attribute.getAttributeType().getPid()));
    }

}
