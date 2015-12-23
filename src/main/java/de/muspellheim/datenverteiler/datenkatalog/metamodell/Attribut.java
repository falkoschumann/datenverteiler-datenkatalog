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

    public static Attribut erzeugeMitNameUndTyp(String name, AttributTyp typ) {
        Attribut result = new Attribut();
        result.setName(name);
        return result;
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
