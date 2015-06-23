/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.datenverteiler;

import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.SystemObject;

import java.time.LocalDateTime;

/**
 * Ein Datensatz ist ein Datum einer Attributgruppe, das für ein  Systemobjekt gesendet oder empfangen wird.
 *
 * @author Falko Schumann
 * @param <T> der Typ des Datums dieses Datensatzes.
 * @since 1.2
 */
public final class Datensatz<T> {

    private SystemObject objekt;
    private T datum;
    private Aspect aspekt;
    private LocalDateTime zeitstempel;

    private Datensatz() {
        // hide constructor
    }

    /**
     * Erzeugt einen Datensatz, der keine Daten enthält.
     */
    public static <T> Datensatz<T> of(SystemObject objekt, Aspect aspekt) {
        return of(objekt, null, aspekt, LocalDateTime.now());
    }

    /**
     * Erzeugt einen Datensatz mit der aktuellen Systemzeit als Zeitstempel.
     */
    public static <T> Datensatz<T> of(SystemObject objekt, T datum, Aspect aspekt) {
        return of(objekt, datum, aspekt, LocalDateTime.now());
    }

    /**
     * Erzeugt einen vollständigen Datensatz.
     */
    public static <T> Datensatz<T> of(SystemObject objekt, T datum, Aspect aspekt, LocalDateTime zeitstempel) {
        Datensatz<T> result = new Datensatz<>();
        result.objekt = objekt;
        result.datum = datum;
        result.aspekt = aspekt;
        result.zeitstempel = zeitstempel;
        return result;
    }

    public SystemObject getObjekt() {
        return objekt;
    }

    public boolean enthaeltDaten() {
        return getDatum() != null;
    }

    public T getDatum() {
        return datum;
    }

    public Aspect getAspekt() {
        return aspekt;
    }

    public LocalDateTime getZeitstempel() {
        return zeitstempel;
    }

    @Override
    public String toString() {
        return "Datensatz{" +
                "objekt=" + objekt +
                ", datum=" + datum +
                ", aspekt=" + aspekt +
                ", zeitstempel=" + zeitstempel +
                '}';
    }

}
