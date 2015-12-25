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
public final class Attribut extends Systemobjekt {

    private String name;
    private int position;
    private int anzahl;
    private boolean anzahlVariabel;
    private Attributtyp attributtyp;

    private Attribut() {
        // value object
    }

    public static Attribut erzeuge(String name, int position, Attributtyp typ) {
        return erzeuge(name, position, 1, false, typ);
    }

    public static Attribut erzeuge(String name, int position, int anzahl, boolean anzahlVariabel, Attributtyp typ) {
        Attribut result = new Attribut();
        result.name = name;
        result.position = position;
        result.anzahl = anzahl;
        result.anzahlVariabel = anzahlVariabel;
        result.attributtyp = typ;
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

    public Attributtyp getAttributtyp() {
        return attributtyp;
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
                Objects.equals(attributtyp, attribut.attributtyp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), position, anzahl, anzahlVariabel, attributtyp);
    }

}
