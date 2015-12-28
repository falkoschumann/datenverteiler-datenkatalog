/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import java.util.Objects;

/**
 * Typ von diskreten Zuständen eines Attributwerts.
 * <p><strong>Hinweis: </strong> Ein Wertezustand ist ein Wertobjekt und wird über seine Eigenschaften
 * identifiziert.</p>
 *
 * @author Falko Schumann
 * @since 3.2
 */
public final class Wertezustand extends Systemobjekt {

    private long wert;

    private Wertezustand() {
        super(null);
    }

    public static Wertezustand erzeuge(String name, long wert) {
        Wertezustand result = new Wertezustand();
        result.setName(name);
        result.wert = wert;
        return result;
    }

    public long getWert() {
        return wert;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Wertezustand werteZustand = (Wertezustand) o;
        return Objects.equals(getName(), werteZustand.getName()) &&
                wert == werteZustand.wert;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), wert);
    }

}
