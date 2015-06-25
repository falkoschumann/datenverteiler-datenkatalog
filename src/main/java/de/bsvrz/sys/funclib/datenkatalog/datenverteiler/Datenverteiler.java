/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.datenverteiler;

import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.SystemObject;

import java.util.function.Consumer;

/**
 * Schnittstelle für die wichtigsten Operationen mit dem Datenverteiler.
 *
 * @author Falko Schumann
 * @since 1.2
 */
public interface Datenverteiler {

    /**
     * Meldet ein Datum unter einem Aspekt für ein oder mehrere Objekt zum Senden als Quelle an.
     */
    void anmeldenAlsQuelle(Class<?> datumTyp, Aspect aspekt, SystemObject... objekte) throws DatenverteilerException;

    /**
     * Meldet ein Datum unter einem Aspekt für ein oder mehrere Objekt zum Senden als Sender an.
     */
    void anmeldenAlsSender(Class<?> datumTyp, Aspect aspekt, SystemObject... objekte) throws DatenverteilerException;

    /**
     * Meldet ein Datum unter einem Aspekt für ein oder mehrere Objekt als Quelle oder Sender wieder ab.
     */
    void abmeldenAlsSender(Class<?> datumTyp, Aspect aspekt, SystemObject... objekte);

    /**
     * Meldet ein Datum unter einem Aspekt für ein oder mehrere Objekt zum Empfang als Senke an.
     */
    <T> void anmeldenAlsSenke(Consumer<Datensatz<T>> empfaenger, Class<T> datumTyp, Aspect aspekt, SystemObject... objekte);

    /**
     * Meldet ein Datum unter einem Aspekt für ein oder mehrere Objekt zum Empfang als Empfänger an.
     */
    <T> void anmeldenAlsEmpfaenger(Consumer<Datensatz<T>> empfaenger, Class<T> datumTyp, Aspect aspekt, SystemObject... objekte);

    /**
     * Meldet ein Datum unter einem Aspekt für ein oder mehrere Objekt zum Empfang als Senke oder Empfänger wieder ab.
     */
    <T> void abmeldenAlsEmpfaenger(Consumer<Datensatz<T>> empfaenger, Class<T> datumTyp, Aspect aspekt, SystemObject... objekte);

    /**
     * Meldet ein Parameterdatum für ein oder mehrere Objekt zum Empfang an.
     */
    <T> void anmeldenAufParameter(Consumer<Datensatz<T>> empfaenger, Class<T> datumTyp, SystemObject... objekte);

    /**
     * Meldet ein Parameterdatum für ein oder mehrere Objekt zum Empfang wieder ab.
     */
    <T> void abmeldenVonParameter(Consumer<Datensatz<T>> empfaenger, Class<T> datumTyp, SystemObject... objekte);

    /**
     * Ruft einen Parameter eines Objekts synchron ab. Gibt nie {@code null} zurück.
     */
    <T> Datensatz<T> parameter(Class<T> datumTyp, SystemObject objekt);

    /**
     * Liest ein Konfigurationdatum für Objekt ab. Gibt das Konfigurationsdatum oder {@code null} zurück, wenn es nicht
     * versorgt ist.
     */
    <T> T konfiguration(Class<T> datumTyp, SystemObject objekt);

    /**
     * Sendet einen oder mehrere Datensätze.
     */
    void sendeDatensatz(Datensatz<?>... datensaetze) throws DatenverteilerException;

    /**
     * Gibt das Objekt zu einer PID zurück. Gibt das Objekt oder {@code null} zurück, wenn kein Objekt zu der
     * angegebenen PID existiert.
     */
    SystemObject objekt(String pid);

    /**
     * Gibt den Aspekt zu einer PID zurück. Gibt den Aspekt oder {@code null} zurück, wenn kein Aspekt zu der
     * angegebenen PID existiert.
     */
    Aspect aspekt(String pid);

}
