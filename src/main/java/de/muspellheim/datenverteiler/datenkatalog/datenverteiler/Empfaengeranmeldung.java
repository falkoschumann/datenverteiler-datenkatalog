/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.datenverteiler;

import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.SystemObject;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Beschreibt eine einzelne Anmeldung als Empfänger für Datensätze vom Datenverteiler.
 *
 * @author Falko Schumann
 * @since 3.0
 */
public final class Empfaengeranmeldung<T> {

    private Consumer<Datensatz<T>> empfaenger;
    private Collection<SystemObject> objekte;
    private Class<T> datumTyp;
    private Aspect aspekt;

    private Empfaengeranmeldung() {
        // value object
    }

    public static <T> Empfaengeranmeldung of(Consumer<Datensatz<T>> empfaenger, Collection<SystemObject> objekte, Class<T> datumTyp, Aspect aspekt) {
        Empfaengeranmeldung<T> result = new Empfaengeranmeldung<>();
        result.empfaenger = empfaenger;
        result.objekte = objekte;
        result.datumTyp = datumTyp;
        result.aspekt = aspekt;
        return result;
    }

    public Consumer<Datensatz<T>> getEmpfaenger() {
        return empfaenger;
    }

    public Collection<SystemObject> getObjekte() {
        return objekte;
    }

    public Class<T> getDatumTyp() {
        return datumTyp;
    }

    public Aspect getAspekt() {
        return aspekt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Empfaengeranmeldung that = (Empfaengeranmeldung) o;
        return Objects.equals(empfaenger, that.empfaenger) &&
                Objects.equals(objekte, that.objekte) &&
                Objects.equals(datumTyp, that.datumTyp) &&
                Objects.equals(aspekt, that.aspekt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empfaenger, objekte, datumTyp, aspekt);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Empfaengeranmeldung{");
        sb.append("empfaenger=").append(empfaenger);
        sb.append(", objekte=").append(objekte);
        sb.append(", datumTyp=").append(datumTyp);
        sb.append(", aspekt=").append(aspekt);
        sb.append('}');
        return sb.toString();
    }

}
