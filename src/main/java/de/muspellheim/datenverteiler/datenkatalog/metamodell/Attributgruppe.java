/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Objekte von diesem Typ repräsentieren Attributgruppen.
 * <p>Ein Datensatz ist immer einer Attributgruppe und einem Aspekt zugeordnet. Die Attributgruppe beschreibt den Aufbau
 * des Datensatzes und der Aspekt legt die Verwendung und damit die Bedeutung (z.B."soll" und"ist") des Datensatzes
 * fest. Welche Attributgruppen welche Aspekte verwenden können, ist als Menge bei der entsprechenden Attributgruppe
 * versorgt.</p>
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class Attributgruppe extends Systemobjekt implements Attributmenge {

    private final Set<Attribut> attribute = new LinkedHashSet<>();

    private Set<Aspekt> aspekte = new LinkedHashSet<>();
    private Set<Attributgruppenverwendung> attributgruppenverwendungen = new LinkedHashSet<>();

    public static Attributgruppe erzeugeMitPid(String pid) {
        Attributgruppe result = new Attributgruppe();
        result.setPid(pid);
        return result;
    }

    /**
     * Menge der Attribute.
     */
    public Set<Attribut> getAttribute() {
        return attribute;
    }

    /**
     * Mögliche Aspekte, unter denen die Attributgruppe verwendet werden kann.
     */
    public Set<Aspekt> getAspekte() {
        return aspekte;
    }

    /**
     * Attributgruppenverwendungen dieser Attributgruppe.
     */
    public Set<Attributgruppenverwendung> getAttributgruppenverwendungen() {
        return attributgruppenverwendungen;
    }

}
