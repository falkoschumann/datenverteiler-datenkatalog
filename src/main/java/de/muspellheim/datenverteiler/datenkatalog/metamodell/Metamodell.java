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

    // TODO Set von TreeSet auf LinkedHashSet umstellen, damit Reihenfolge beibehalten wird

    private final DataModel model;

    private final KonfigurationsVerantwortlicherFabrik konfigurationsVerantwortlicherFabrik = new KonfigurationsVerantwortlicherFabrik(this);
    private final KonfigurationsBereichFabrik konfigurationsBereichFabrik = new KonfigurationsBereichFabrik(this);
    private final TypFabrik typFabrik = new TypFabrik(this);
    private final MengenTypFabrik mengenTypFabrik = new MengenTypFabrik(this);
    private final AttributgruppeFabrik attributgruppeFabrik = new AttributgruppeFabrik(this);
    private final AttributListenDefinitionFabrik attributListenDefinitionFabrik = new AttributListenDefinitionFabrik(this);
    private final ZeichenkettenAttributTypFabrik zeichenkettenAttributTypFabrik = new ZeichenkettenAttributTypFabrik(this);
    private final ZeitstempelAttributTypFabrik zeitstempelAttributTypFabrik = new ZeitstempelAttributTypFabrik(this);
    private final KommazahlAttributTypFabrik kommazahlAttributTypFabrik = new KommazahlAttributTypFabrik(this);
    private final ObjektReferenzAttributTypFabrik objektReferenAttributTypFabrik = new ObjektReferenzAttributTypFabrik(this);
    private final GanzzahlAttributTypFabrik ganzzahlAttributTypFabrik = new GanzzahlAttributTypFabrik(this);

    public Metamodell(DataModel model) {
        this.model = model;
    }

    DataModel getModel() {
        return model;
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
        ObjectSetUse objectSetUse = (ObjectSetUse) model.getObject(id);
        return MengenVerwendung.erzeuge(objectSetUse.getObjectSetName(), getMengenTyp(objectSetUse.getObjectSetType().getPid()), objectSetUse.isRequired());
    }

    public Attributgruppe getAttributgruppe(String pid) {
        return attributgruppeFabrik.getObjekt(pid);
    }

    public AttributListenDefinition getAttributliste(String pid) {
        return attributListenDefinitionFabrik.getObjekt(pid);
    }

    AttributTyp getAttributTyp(String pid) {
        AttributeType attributeType = model.getAttributeType(pid);
        if (AttributListenDefinitionFabrik.istAttributliste(attributeType))
            return attributListenDefinitionFabrik.getObjekt(pid);
        if (ZeichenkettenAttributTypFabrik.istAttributTyp(attributeType))
            return zeichenkettenAttributTypFabrik.getObjekt(pid);
        if (ZeitstempelAttributTypFabrik.istAttributTyp(attributeType))
            return zeitstempelAttributTypFabrik.getObjekt(pid);
        if (KommazahlAttributTypFabrik.istAttributTyp(attributeType))
            return kommazahlAttributTypFabrik.getObjekt(pid);
        if (ObjektReferenzAttributTypFabrik.istAttributTyp(attributeType))
            return objektReferenAttributTypFabrik.getObjekt(pid);
        if (GanzzahlAttributTypFabrik.istAttributTyp(attributeType))
            return ganzzahlAttributTypFabrik.getObjekt(pid);
        throw new IllegalStateException("Unbekannter Attributtyp: " + attributeType);
    }

    Attribut getAttribut(long id) {
        Attribute attribute = (Attribute) model.getObject(id);
        return Attribut.erzeuge(attribute.getName(), attribute.getPosition(), attribute.getMaxCount(), attribute.isCountVariable(), getAttributTyp(attribute.getAttributeType().getPid()));
    }

}
