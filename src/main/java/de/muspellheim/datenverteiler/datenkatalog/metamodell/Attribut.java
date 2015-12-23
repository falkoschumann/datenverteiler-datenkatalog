/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import java.util.Objects;

/**
 * Attribut im Kontext einer Attributmenge.
 * <p><strong>Hinweis: </strong> Ein Attribut ist ein Wertobjekt und wird über seine Eigenschaften identifiziert.</p>
 *
 * @author Falko Schumann
 * @since 3.2
 */
public final class Attribut extends SystemObjekt {

    // TODO Soll SystemObjekt für Kurzinfo und Beschreibung abgeleitet werden?

    private String name;
    private int position;
    private int anzahl;
    private boolean anzahlVariabel;
    private AttributTyp attributTyp;

    private Attribut() {
        // value object
    }

    public static Attribut erzeuge(String name, int position, AttributTyp typ) {
        return erzeuge(name, position, 1, false, typ);
    }

    public static Attribut erzeuge(String name, int position, int anzahl, boolean anzahlVariabel, AttributTyp typ) {
        Attribut result = new Attribut();
        result.name = name;
        result.position = position;
        result.anzahl = anzahl;
        result.anzahlVariabel = anzahlVariabel;
        result.attributTyp = typ;
        return result;
    }

    public String getName() {
        return name;
    }

    /**
     * Position eines Attributs in einer Attributgruppe oder -liste.
     */
    public int getPosition() {
        return position;
    }

    public int getAnzahl() {
        return anzahl;
    }

    /**
     * Spezifiziert ob die Anzahl der verwendeten Feldelement zwischen 0 und der im Attribut 'anzahl' angegebenen Größe
     * variieren kann.
     */
    public boolean isAnzahlVariabel() {
        return anzahlVariabel;
    }

    public AttributTyp getAttributTyp() {
        return attributTyp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Attribut attribut = (Attribut) o;
        return Objects.equals(getName(), attribut.getName()) &&
                position == attribut.position &&
                anzahl == attribut.anzahl &&
                anzahlVariabel == attribut.anzahlVariabel &&
                Objects.equals(attributTyp, attribut.attributTyp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), position, anzahl, anzahlVariabel, attributTyp);
    }

}
