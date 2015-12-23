/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import java.util.Objects;

/**
 * Attribut im Kontext einer Attributmenge.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class Attribut extends SystemObjekt {

    private AttributTyp attributTyp;

    public static Attribut erzeugeMitNameUndTyp(String name, AttributTyp typ) {
        Attribut result = new Attribut();
        result.setName(name);
        result.setAttributTyp(typ);
        return result;
    }

    public AttributTyp getAttributTyp() {
        return attributTyp;
    }

    public void setAttributTyp(AttributTyp attributTyp) {
        this.attributTyp = attributTyp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SystemObjekt)) return false;
        SystemObjekt that = (SystemObjekt) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

}
