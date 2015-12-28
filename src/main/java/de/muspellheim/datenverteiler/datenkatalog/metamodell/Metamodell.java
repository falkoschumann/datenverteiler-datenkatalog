/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Fabrik für Objekte des Metamodells.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class Metamodell {

    // TODO Defaultwerte abbilden
    // TODO Defaultparameter abbilden
    // TODO Konfigurationsobjekte mit Mengen und Konfigurationsdaten abbilden

    private final Map<SystemObject, Systemobjekt> objekte = new LinkedHashMap<>();

    private final DataModel model;


    public Metamodell(DataModel model) {
        this.model = model;
    }

    protected DataModel getModel() {
        return model;
    }

    protected boolean istObjekt(SystemObject so) {
        return istKonfigurationsbereich(so) ||
                istKonfigurationsverantwortlicher(so) ||
                istTyp(so) ||
                istAttributgruppe(so) ||
                istAspekt(so) ||
                istAttributliste(so) ||
                istAttributTyp(so);
    }

    public Systemobjekt gibObjekt(String pid) {
        SystemObject so = model.getObject(pid);
        if (istKonfigurationsbereich(so)) return gibKonfigurationsbereich(pid);
        if (istKonfigurationsverantwortlicher(so)) return gibKonfigurationsverantwortlicher(pid);
        if (istTyp(so)) return gibTyp(pid);
        if (istAttributgruppe(so)) return gibAttributgruppe(pid);
        if (istAspekt(so)) return gibAspekt(pid);
        if (istAttributliste(so)) return gibAttributliste(pid);
        if (istAttributTyp(so)) return gibAttributtyp(pid);
        throw new NoSuchElementException("Es gibt kein Objekt mit der PID " + pid + ".");
    }

    protected <T extends Systemobjekt> T gibObjekt(SystemObject so, Fabrikmethode<T> fabrikmethode, Initialisierungsstrategie<T> initalisierungsstrategie) {
        if (objekte.containsKey(so))
            return (T) objekte.get(so);

        T result = fabrikmethode.erzeuge(so);
        objekte.put(so, result);
        initalisierungsstrategie.initialisiere(so, result);
        return result;
    }

    protected void initialisiereObjekt(SystemObject so, Systemobjekt objekt) {
        objekt.setName(so.getName());
        objekt.setKurzinfo(so.getInfo().getShortInfo().trim());
        objekt.setBeschreibung(so.getInfo().getDescription().trim());
        if (!(so instanceof ConfigurationArea))
            objekt.setKonfigurationsbereich(gibKonfigurationsbereich(so.getConfigurationArea().getPid()));
    }

    public Set<Konfigurationsbereich> getKonfigurationsbereiche() {
        return model.getType("typ.konfigurationsBereich").getElements().stream().
                map(k -> gibKonfigurationsbereich(k.getPid())).
                collect(Collectors.toSet());
    }

    protected boolean istKonfigurationsbereich(SystemObject systemObject) {
        return systemObject.isOfType("typ.konfigurationsBereich");
    }

    public Konfigurationsbereich gibKonfigurationsbereich(String pid) {
        return gibObjekt(model.getObject(pid), this::erzeugeKonfigurationsbereich, this::initialisiereKonfigurationsbereich);
    }

    protected Konfigurationsbereich erzeugeKonfigurationsbereich(SystemObject so) {
        return new Konfigurationsbereich(so.getPid());
    }

    protected void initialisiereKonfigurationsbereich(SystemObject object, Konfigurationsbereich result) {
        initialisiereObjekt(object, result);

        ConfigurationArea area = (ConfigurationArea) object;
        result.setZustaendiger(gibKonfigurationsverantwortlicher(area.getConfigurationAuthority().getPid()));
        area.getCurrentObjects().stream().
                filter(this::istObjekt).
                forEach(e -> result.getModell().add(gibObjekt(e.getPid())));
    }

    protected boolean istKonfigurationsverantwortlicher(SystemObject systemObject) {
        return systemObject.isOfType("typ.konfigurationsVerantwortlicher");
    }

    public Konfigurationsverantwortlicher gibKonfigurationsverantwortlicher(String pid) {
        return gibObjekt(model.getObject(pid), this::erzeugeKonfigurationsverantwortlicher, this::initialisiereKonfigurationsverantwortlicher);
    }

    protected Konfigurationsverantwortlicher erzeugeKonfigurationsverantwortlicher(SystemObject so) {
        return new Konfigurationsverantwortlicher(so.getPid());
    }

    protected void initialisiereKonfigurationsverantwortlicher(SystemObject so, Konfigurationsverantwortlicher objekt) {
        initialisiereObjekt(so, objekt);
    }

    protected boolean istTyp(SystemObject systemObject) {
        return systemObject.isOfType("typ.typ");
    }

    public Typ gibTyp(String pid) {
        SystemObject so = model.getObject(pid);
        if (istDynamischerTyp(so))
            return gibDynamischerTyp(pid);
        if (istMengenTyp(so))
            return gibMengentyp(pid);

        return gibObjekt(so, this::erzeugeTyp, this::initialisiereTyp);
    }

    protected Typ erzeugeTyp(SystemObject so) {
        return new Typ(so.getPid());
    }

    protected void initialisiereTyp(SystemObject so, Typ objekt) {
        initialisiereObjekt(so, objekt);

        SystemObjectType type = (SystemObjectType) so;
        type.getSuperTypes().stream().forEach(e -> objekt.getSupertypen().add(gibTyp(e.getPid())));
        type.getSubTypes().stream().forEach(e -> objekt.getSubtypen().add(gibTyp(e.getPid())));
        type.getDirectObjectSetUses().forEach(e -> objekt.getMengen().add(gibMengenverwendung(e.getId())));
        type.getDirectAttributeGroups().forEach(e -> objekt.getAttributgruppen().add(gibAttributgruppe(e.getPid())));
    }

    protected Mengenverwendung gibMengenverwendung(long id) {
        ObjectSetUse objectSetUse = (ObjectSetUse) model.getObject(id);
        Mengenverwendung result = erzeugeMengenverwendung(objectSetUse);
        initialisiereObjekt(objectSetUse, result);
        return result;
    }

    protected Mengenverwendung erzeugeMengenverwendung(ObjectSetUse objectSetUse) {
        return Mengenverwendung.erzeuge(objectSetUse.getObjectSetName(), gibMengentyp(objectSetUse.getObjectSetType().getPid()), objectSetUse.isRequired());
    }

    protected boolean istDynamischerTyp(SystemObject systemObject) {
        return systemObject.isOfType("typ.dynamischerTyp");
    }

    protected DynamischerTyp gibDynamischerTyp(String pid) {
        return gibObjekt(model.getObject(pid), this::erzeugeDynamischerTyp, this::initialisiereDynamischerTyp);
    }

    protected DynamischerTyp erzeugeDynamischerTyp(SystemObject so) {
        return new DynamischerTyp(so.getPid());
    }

    protected void initialisiereDynamischerTyp(SystemObject so, DynamischerTyp objekt) {
        initialisiereTyp(so, objekt);
    }

    protected boolean istMengenTyp(SystemObject systemObject) {
        return systemObject.isOfType("typ.mengenTyp");
    }

    protected Mengentyp gibMengentyp(String pid) {
        return gibObjekt(model.getObject(pid), this::erzeugeMengentyp, this::initialisiereMengentyp);
    }

    protected Mengentyp erzeugeMengentyp(SystemObject so) {
        return new Mengentyp(so.getPid());
    }

    protected void initialisiereMengentyp(SystemObject so, Mengentyp objekt) {
        initialisiereTyp(so, objekt);

        ObjectSetType objectSetType = (ObjectSetType) so;
        objectSetType.getObjectTypes().forEach(t -> objekt.getObjektTypen().add(gibTyp(t.getPid())));
        AttributeGroup atg = model.getAttributeGroup("atg.mengenTypEigenschaften");
        Data data = so.getConfigurationData(atg);
        objekt.setMinimaleAnzahl(data.getUnscaledValue("minimaleAnzahl").intValue());
        objekt.setMaximaleAnzahl(data.getUnscaledValue("maximaleAnzahl").intValue());
        objekt.setAenderbar(data.getUnscaledValue("änderbar").byteValue() != 0);
        switch (data.getUnscaledValue("referenzierungsart").byteValue()) {
            case 0:
                objekt.setReferenzierungsart(Referenzierungsart.ASSOZIATION);
                break;
            case 1:
                objekt.setReferenzierungsart(Referenzierungsart.AGGREGATION);
                break;
            case 2:
                objekt.setReferenzierungsart(Referenzierungsart.KOMPOSITION);
                break;
            default:
                throw new IllegalStateException("Unbekannte Referenzierungsart: " + data.getUnscaledValue("referenzierungsart").getText());
        }
    }

    protected boolean istAttributgruppe(SystemObject systemObject) {
        return systemObject.isOfType("typ.attributgruppe");
    }

    public Attributgruppe gibAttributgruppe(String pid) {
        return gibObjekt(model.getObject(pid), this::erzeugeAttributgruppe, this::initialisiereAttributgruppe);
    }

    protected Attributgruppe erzeugeAttributgruppe(SystemObject so) {
        return new Attributgruppe(so.getPid());
    }

    protected void initialisiereAttributgruppe(SystemObject so, Attributgruppe objekt) {
        initialisiereAttributmenge(so, objekt);

        AttributeGroup atg = (AttributeGroup) so;
        atg.getAspects().forEach(e -> objekt.getAspekte().add(gibAspekt(e.getPid())));
        atg.getAttributeGroupUsages().forEach(e -> objekt.getAttributgruppenverwendungen().add(gibAttributgruppenverwendung(e.getPid())));
    }

    protected <T extends Systemobjekt & Attributmenge> void initialisiereAttributmenge(SystemObject so, T objekt) {
        initialisiereObjekt(so, objekt);

        ConfigurationObject co = (ConfigurationObject) so;
        co.getObjectSet("Attribute").getElements().stream().forEach(e -> objekt.getAttribute().add(gibAttribut(e.getId())));
    }

    protected Attributgruppenverwendung gibAttributgruppenverwendung(String pid) {
        return gibObjekt(model.getObject(pid), this::erzeugeAttributgruppenverwendung, this::initialisiereAttributgruppenverwendung);
    }

    protected Attributgruppenverwendung erzeugeAttributgruppenverwendung(SystemObject so) {
        return new Attributgruppenverwendung(so.getPid());
    }

    protected void initialisiereAttributgruppenverwendung(SystemObject so, Attributgruppenverwendung objekt) {
        initialisiereObjekt(so, objekt);

        AttributeGroupUsage usage = (AttributeGroupUsage) so;
        objekt.setAttributgruppe(gibAttributgruppe(usage.getAttributeGroup().getPid()));
        objekt.setAspekt(gibAspekt(usage.getAspect().getPid()));
        objekt.setVerwendungExplizitVorgegeben(usage.isExplicitDefined());
        objekt.setDatensatzverwendung(datensatzVerwendung(usage.getUsage()));
    }

    protected Datensatzverwendung datensatzVerwendung(AttributeGroupUsage.Usage usage) {
        switch (usage.getId()) {
            case 1:
                return Datensatzverwendung.KONFIGURIERENDER_DATENSATZ_NOTWENDIG;
            case 2:
                return Datensatzverwendung.KONFIGURIERENDER_DATENSATZ_NOTWENDIG_UND_AENDERBAR;
            case 3:
                return Datensatzverwendung.KONFIGURIERENDER_DATENSATZ_OPTIONAL;
            case 4:
                return Datensatzverwendung.KONFIGURIERENDER_DATENSATZ_OPTIONALUND_AENDERBAR;
            case 5:
                return Datensatzverwendung.ONLINE_DATENSATZ_QUELLE;
            case 6:
                return Datensatzverwendung.ONLINE_DATENSATZ_SENKE;
            case 7:
                return Datensatzverwendung.ONLINE_DATENSATZ_QUELLE_UND_SENKE;
            default:
                throw new IllegalStateException("Unbekannte Datensatzverwendung: " + usage);
        }
    }

    protected Attribut gibAttribut(long id) {
        Attribute attribute = (Attribute) model.getObject(id);
        return gibObjekt(attribute, this::erzeugeAttribut, this::initialisiereAttribut);
    }

    protected Attribut erzeugeAttribut(SystemObject so) {
        Attribute attribute = (Attribute) so;
        return Attribut.erzeuge(attribute.getName(), attribute.getPosition(), attribute.getMaxCount(), attribute.isCountVariable(), gibAttributtyp(attribute.getAttributeType().getPid()));
    }

    protected void initialisiereAttribut(SystemObject so, Attribut objekt) {
        initialisiereObjekt(so, objekt);
    }

    protected boolean istAspekt(SystemObject systemObject) {
        return systemObject.isOfType("typ.aspekt");
    }

    public Aspekt gibAspekt(String pid) {
        return gibObjekt(model.getObject(pid), this::erzeugeAspekt, this::initialisiereAspekt);
    }

    protected Aspekt erzeugeAspekt(SystemObject so) {
        return new Aspekt(so.getPid());
    }

    protected void initialisiereAspekt(SystemObject so, Systemobjekt objekt) {
        initialisiereObjekt(so, objekt);
    }

    protected boolean istAttributTyp(SystemObject systemObject) {
        return systemObject.isOfType("typ.attributTyp");
    }

    public Attributtyp gibAttributtyp(String pid) {
        AttributeType attributeType = model.getAttributeType(pid);
        if (istAttributliste(attributeType))
            return gibAttributliste(pid);
        if (istZeichenketteAttributtyp(attributeType))
            return gibZeichenkettenAttributtyp(pid);
        if (istZeitstempelAttributtyp(attributeType))
            return gibZeitstempelAttributtyp(pid);
        if (istKommazahlAttributtyp(attributeType))
            return gibKommazahlAttributtyp(pid);
        if (istObjektreferenzAttributtyp(attributeType))
            return gibObjektreferenzAttributtyp(pid);
        if (istGanzzahlAttributtyp(attributeType))
            return gibGanzzahlAttributtyp(pid);
        throw new IllegalStateException("Unbekannter Attributtyp: " + attributeType);
    }

    protected void initialisiereAttributtyp(SystemObject so, Systemobjekt objekt) {
        initialisiereObjekt(so, objekt);
    }

    protected boolean istAttributliste(SystemObject systemObject) {
        return systemObject.isOfType("typ.attributListenDefinition");
    }

    public Attributliste gibAttributliste(String pid) {
        return gibObjekt(model.getObject(pid), this::erzeugeAttributliste, this::initialisiereAttributliste);
    }

    protected Attributliste erzeugeAttributliste(SystemObject so) {
        return new Attributliste(so.getPid());
    }

    protected void initialisiereAttributliste(SystemObject so, Attributliste objekt) {
        initialisiereAttributmenge(so, objekt);
        initialisiereAttributtyp(so, objekt);
    }

    protected boolean istZeichenketteAttributtyp(SystemObject systemObject) {
        return systemObject.isOfType("typ.zeichenketteAttributTyp");
    }

    protected ZeichenkettenAttributtyp gibZeichenkettenAttributtyp(String pid) {
        return gibObjekt(model.getObject(pid), this::erzeugeZeichenkettenAttributtyp, this::initialisiereZeichenkettenAttributtyp);
    }

    protected ZeichenkettenAttributtyp erzeugeZeichenkettenAttributtyp(SystemObject so) {
        return new ZeichenkettenAttributtyp(so.getPid());
    }

    protected void initialisiereZeichenkettenAttributtyp(SystemObject so, ZeichenkettenAttributtyp objekt) {
        initialisiereAttributtyp(so, objekt);

        StringAttributeType type = (StringAttributeType) so;
        objekt.setLaenge(type.getMaxLength());
        objekt.setKodierung(Zeichenkodierung.of(type.getEncodingName()));
    }

    protected boolean istZeitstempelAttributtyp(SystemObject systemObject) {
        return systemObject.isOfType("typ.zeitstempelAttributTyp");
    }

    protected ZeitstempelAttributtyp gibZeitstempelAttributtyp(String pid) {
        return gibObjekt(model.getObject(pid), this::erzeugeZeitstempelAttributtyp, this::initialisiereZeitstempelAttributtyp);
    }

    protected ZeitstempelAttributtyp erzeugeZeitstempelAttributtyp(SystemObject so) {
        return new ZeitstempelAttributtyp(so.getPid());
    }

    protected void initialisiereZeitstempelAttributtyp(SystemObject so, ZeitstempelAttributtyp objekt) {
        initialisiereAttributtyp(so, objekt);

        TimeAttributeType type = (TimeAttributeType) so;
        objekt.setRelativ(type.isRelative());
        switch (type.getAccuracy()) {
            case TimeAttributeType.SECONDS:
                objekt.setGenauigkeit(Zeitaufloesung.SEKUNDEN);
                break;
            case TimeAttributeType.MILLISECONDS:
                objekt.setGenauigkeit(Zeitaufloesung.MILLISEKUNDEN);
                break;
            default:
                throw new IllegalStateException("Unbekante Zeitauflösung: " + type.getAccuracy());
        }
    }

    protected boolean istKommazahlAttributtyp(SystemObject systemObject) {
        return systemObject.isOfType("typ.kommazahlAttributTyp");
    }

    protected KommazahlAttributtyp gibKommazahlAttributtyp(String pid) {
        return gibObjekt(model.getObject(pid), this::erzeugeKommazahlAttributtyp, this::initialisiereKommazahlAttributtyp);
    }

    protected KommazahlAttributtyp erzeugeKommazahlAttributtyp(SystemObject so) {
        return new KommazahlAttributtyp(so.getPid());
    }

    protected void initialisiereKommazahlAttributtyp(SystemObject so, KommazahlAttributtyp objekt) {
        initialisiereAttributtyp(so, objekt);

        DoubleAttributeType type = (DoubleAttributeType) so;
        objekt.setEinheit(type.getUnit());
        switch (type.getAccuracy()) {
            case DoubleAttributeType.DOUBLE:
                objekt.setGenauigkeit(Fliesskommaaufloesung.DOUBLE);
                break;
            case DoubleAttributeType.FLOAT:
                objekt.setGenauigkeit(Fliesskommaaufloesung.FLOAT);
                break;
            default:
                throw new IllegalStateException("Unbekannte Fließkommaauflösung: " + type.getAccuracy());
        }

    }

    protected boolean istObjektreferenzAttributtyp(SystemObject systemObject) {
        return systemObject.isOfType("typ.objektReferenzAttributTyp");
    }

    protected ObjektreferenzAttributtyp gibObjektreferenzAttributtyp(String pid) {
        return gibObjekt(model.getObject(pid), this::erzeugeObjektreferenzAttributtyp, this::initialisiereObjektreferenzAttributtyp);
    }

    protected ObjektreferenzAttributtyp erzeugeObjektreferenzAttributtyp(SystemObject so) {
        return new ObjektreferenzAttributtyp(so.getPid());
    }

    protected void initialisiereObjektreferenzAttributtyp(SystemObject so, ObjektreferenzAttributtyp objekt) {
        initialisiereAttributtyp(so, objekt);

        ReferenceAttributeType type = (ReferenceAttributeType) so;
        if (type.getReferencedObjectType() != null)
            objekt.setTyp(gibTyp(type.getReferencedObjectType().getPid()));
        objekt.setUndefiniertErlaubt(type.isUndefinedAllowed());
        switch (type.getReferenceType()) {
            case ASSOCIATION:
                objekt.setReferenzierungsart(Referenzierungsart.ASSOZIATION);
                break;
            case AGGREGATION:
                objekt.setReferenzierungsart(Referenzierungsart.AGGREGATION);
                break;
            case COMPOSITION:
                objekt.setReferenzierungsart(Referenzierungsart.KOMPOSITION);
                break;
            default:
                throw new IllegalStateException("Unbekannte Referenzierungsart: " + type.getReferenceType());
        }
    }

    protected boolean istGanzzahlAttributtyp(SystemObject systemObject) {
        return systemObject.isOfType("typ.ganzzahlAttributTyp");
    }

    protected GanzzahlAttributtyp gibGanzzahlAttributtyp(String pid) {
        return gibObjekt(model.getObject(pid), this::erzeugeGanzzahlAttributtyp, this::initialisiereGanzzahlAttributtyp);
    }

    protected GanzzahlAttributtyp erzeugeGanzzahlAttributtyp(SystemObject so) {
        return new GanzzahlAttributtyp(so.getPid());
    }

    protected void initialisiereGanzzahlAttributtyp(SystemObject so, GanzzahlAttributtyp objekt) {
        initialisiereAttributtyp(so, objekt);

        IntegerAttributeType type = (IntegerAttributeType) so;
        switch (type.getByteCount()) {
            case 1:
                objekt.setAnzahlBytes(Datentypgroesse.BYTE);
                break;
            case 2:
                objekt.setAnzahlBytes(Datentypgroesse.SHORT);
                break;
            case 4:
                objekt.setAnzahlBytes(Datentypgroesse.INT);
                break;
            case 8:
                objekt.setAnzahlBytes(Datentypgroesse.LONG);
                break;
            default:
                throw new IllegalStateException("Unbekannte Datentypgröße: " + type.getByteCount());
        }
        IntegerValueRange range = type.getRange();
        if (range != null) {
            objekt.setBereich(gibWertebereich(range));
        }
        type.getStates().forEach(e -> objekt.getZustaende().add(gibZustand(e)));
    }

    protected Wertebereich gibWertebereich(IntegerValueRange range) {
        return gibObjekt(range, this::erzeugeWertebereich, this::initialisiereWertebereich);
    }

    private Wertebereich erzeugeWertebereich(SystemObject so) {
        IntegerValueRange range = (IntegerValueRange) so;
        return Wertebereich.erzeuge(range.getMinimum(), range.getMaximum(), range.getConversionFactor(), range.getUnit());
    }

    private void initialisiereWertebereich(SystemObject so, Wertebereich objekt) {
        initialisiereObjekt(so, objekt);
    }

    protected Wertezustand gibZustand(IntegerValueState state) {
        return gibObjekt(state, this::erzeugeWertezustand, this::initialisiereWertezustand);
    }

    protected Wertezustand erzeugeWertezustand(SystemObject so) {
        IntegerValueState state = (IntegerValueState) so;
        return Wertezustand.erzeuge(state.getName(), state.getValue());
    }

    protected void initialisiereWertezustand(SystemObject so, Wertezustand objekt) {
        initialisiereObjekt(so, objekt);
    }

    @FunctionalInterface
    protected interface Fabrikmethode<T extends Systemobjekt> {

        T erzeuge(SystemObject so);

    }

    @FunctionalInterface
    protected interface Initialisierungsstrategie<T extends Systemobjekt> {

        void initialisiere(SystemObject so, T objekt);

    }

}
