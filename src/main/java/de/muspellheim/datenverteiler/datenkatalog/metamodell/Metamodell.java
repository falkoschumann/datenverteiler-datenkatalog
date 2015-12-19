/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.bsvrz.dav.daf.main.config.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Fabrik zum Erzeugen von Objekten des Metamodells.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class Metamodell {

    private final Map<String, KonfigurationsVerantwortlicher> konfigurationsVerantwortliche = new LinkedHashMap<>();
    private final Map<String, KonfigurationsBereich> konfigurationsbereiche = new LinkedHashMap<>();
    private final Map<String, Typ> typen = new LinkedHashMap<>();
    private final Map<String, MengenTyp> mengenTypen = new LinkedHashMap<>();
    private final Map<String, Attributgruppe> attributgruppen = new LinkedHashMap<>();

    private final DataModel model;

    public Metamodell(DataModel model) {
        this.model = model;
    }

    public Set<KonfigurationsVerantwortlicher> getKonfigurationsverantwortliche() {
        return model.getType("typ.konfigurationsVerantwortlicher").getElements().stream().
                map(k -> getKonfigurationsverantwortlicher(k.getPid())).
                collect(Collectors.toSet());
    }

    public KonfigurationsVerantwortlicher getKonfigurationsverantwortlicher(String pid) {
        return getKonfigurationsverantwortlicher((ConfigurationAuthority) model.getObject(pid));
    }

    private KonfigurationsVerantwortlicher getKonfigurationsverantwortlicher(ConfigurationAuthority authority) {
        if (konfigurationsVerantwortliche.containsKey(authority.getPid()))
            return konfigurationsVerantwortliche.get(authority.getPid());

        KonfigurationsVerantwortlicher result = KonfigurationsVerantwortlicher.erzeugeMitPid(authority.getPid());
        konfigurationsVerantwortliche.put(authority.getPid(), result);
        bestimmeSystemObjekt(authority, result);
        return result;
    }

    public Set<KonfigurationsBereich> getKonfigurationsbereiche() {
        return model.getType("typ.konfigurationsBereich").getElements().stream().
                map(k -> getKonfigurationsbereich(k.getPid())).
                collect(Collectors.toSet());
    }

    public KonfigurationsBereich getKonfigurationsbereich(String pid) {
        return getKonfigurationsbereich(model.getConfigurationArea(pid));
    }

    private KonfigurationsBereich getKonfigurationsbereich(ConfigurationArea area) {
        if (konfigurationsbereiche.containsKey(area.getPid()))
            return konfigurationsbereiche.get(area.getPid());

        KonfigurationsBereich result = KonfigurationsBereich.erzeugeMitPid(area.getPid());
        konfigurationsbereiche.put(area.getPid(), result);
        bestimmeSystemObjekt(area, result);
        result.setZustaendiger(getKonfigurationsverantwortlicher(area.getConfigurationAuthority()));
        area.getCurrentObjects().stream().filter(Metamodell::istTyp).forEach(e -> result.getTypen().add(getTyp((SystemObjectType) e)));
        area.getCurrentObjects().stream().filter(Metamodell::istMengenTyp).forEach(e -> result.getMengen().add(getMengenTyp((ObjectSetType) e)));
        area.getCurrentObjects().stream().filter(Metamodell::istAttributgruppe).forEach(e -> result.getAttributgruppen().add(getAttributgruppe((AttributeGroup) e)));
        return result;
    }

    private void bestimmeSystemObjekt(SystemObject object, SystemObjekt result) {
        result.setName(object.getName());
        result.setKurzinfo(object.getInfo().getShortInfo().trim());
        result.setBeschreibung(object.getInfo().getDescription().trim());
        if (!(object instanceof ConfigurationArea))
            result.setBereich(getKonfigurationsbereich(object.getConfigurationArea()));
    }

    private static boolean istTyp(SystemObject systemObject) {
        return systemObject.isOfType("typ.typ") && !systemObject.isOfType("typ.mengenTyp");
    }

    public Typ getTyp(String pid) {
        return getTyp(model.getType(pid));
    }

    private Typ getTyp(SystemObjectType type) {
        if (typen.containsKey(type.getPid()))
            return typen.get(type.getPid());

        Typ result;
        if (type instanceof DynamicObjectType)
            result = DynamischerTyp.erzeugeMitPid(type.getPid());
        else
            result = Typ.erzeugeMitPid(type.getPid());
        typen.put(type.getPid(), result);
        bestimmeSystemObjekt(type, result);
        type.getSuperTypes().stream().forEach(e -> result.getSuperTypen().add(getTyp(e)));
        type.getSubTypes().stream().forEach(e -> result.getSubTypen().add(getTyp(e)));
        type.getDirectObjectSetUses().forEach(e -> result.getMengen().add(getMengenVerwendung(e)));
        type.getDirectAttributeGroups().forEach(e -> result.getAttributgruppen().add(getAttributgruppe(e)));
        return result;
    }

    private static boolean istMengenTyp(SystemObject systemObject) {
        return systemObject.isOfType("typ.mengenTyp");
    }

    public MengenTyp getMengenTyp(String pid) {
        return getMengenTyp(model.getObjectSetType(pid));
    }

    private MengenTyp getMengenTyp(ObjectSetType objectSetType) {
        if (mengenTypen.containsKey(objectSetType.getPid()))
            return mengenTypen.get(objectSetType.getPid());

        MengenTyp result = MengenTyp.erzeugeMitPid(objectSetType.getPid());
        mengenTypen.put(objectSetType.getPid(), result);
        bestimmeSystemObjekt(objectSetType, result);
        objectSetType.getObjectTypes().forEach(t -> result.getObjektTypen().add(getTyp(t)));
        return result;
    }

    private MengenVerwendung getMengenVerwendung(ObjectSetUse objectSetUse) {
        return MengenVerwendung.erzeugeMitNameUndTyp(objectSetUse.getObjectSetName(), getMengenTyp(objectSetUse.getObjectSetType()));
    }

    private static boolean istAttributgruppe(SystemObject systemObject) {
        return systemObject.isOfType("typ.attributgruppe");
    }

    public Attributgruppe getAttributgruppe(String pid) {
        return getAttributgruppe(model.getAttributeGroup(pid));
    }

    private Attributgruppe getAttributgruppe(AttributeGroup atg) {
        if (attributgruppen.containsKey(atg.getPid()))
            return attributgruppen.get(atg.getPid());

        Attributgruppe result;
        result = Attributgruppe.erzeugeMitPid(atg.getPid());
        attributgruppen.put(atg.getPid(), result);
        bestimmeSystemObjekt(atg, result);
        return result;
    }

}
