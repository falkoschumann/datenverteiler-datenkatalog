/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.datenverteiler;

import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.SystemObject;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * Schnittstelle für die wichtigsten Operationen mit dem Datenverteiler.
 *
 * @author Falko Schumann
 * @since 1.2
 */
public interface Datenverteiler {

    /**
     * Meldet ein Datum unter einem Aspekt für ein oder mehrere Objekte zum Senden als Quelle an.
     */
    void anmeldenAlsQuelle(Collection<SystemObject> objekte, Class<?> datumTyp, Aspect aspekt) throws DatenverteilerException;

    /**
     * Meldet ein Datum unter einem Aspekt für ein oder mehrere Objekte zum Senden als Sender an.
     */
    void anmeldenAlsSender(Collection<SystemObject> objekte, Class<?> datumTyp, Aspect aspekt) throws DatenverteilerException;

    /**
     * Meldet ein Datum unter einem Aspekt für ein oder mehrere Objekte als Quelle oder Sender wieder ab.
     */
    void abmeldenAlsSender(Collection<SystemObject> objekte, Class<?> datumTyp, Aspect aspekt) throws DatenverteilerException;

    /**
     * Meldet ein Datum unter einem Aspekt für ein oder mehrere Objekte zum Empfang als Senke an.
     */
    <T> void anmeldenAlsSenke(Consumer<Datensatz<T>> empfaenger, Collection<SystemObject> objekte, Class<T> datumTyp, Aspect aspekt, Empfaengeroption option) throws DatenverteilerException;

    /**
     * Meldet ein Datum unter einem Aspekt für ein oder mehrere Objekte zum Empfang als Empfänger an.
     */
    <T> void anmeldenAlsEmpfaenger(Consumer<Datensatz<T>> empfaenger, Collection<SystemObject> objekte, Class<T> datumTyp, Aspect aspekt, Empfaengeroption option) throws DatenverteilerException;

    /**
     * Meldet ein Datum unter einem Aspekt für ein oder mehrere Objekte zum Empfang als Senke oder Empfänger wieder ab.
     */
    <T> void abmeldenAlsEmpfaenger(Consumer<Datensatz<T>> empfaenger, Collection<SystemObject> objekte, Class<T> datumTyp, Aspect aspekt) throws DatenverteilerException;

    /**
     * Meldet ein Parameterdatum für ein oder mehrere Objekte zum Empfang an. Die Methode blockiert, bis der Parameter
     * einmalig empfangen wurde, maximal aber eine Minute.
     */
    <T> void anmeldenAufParameter(Consumer<Datensatz<T>> empfaenger, Collection<SystemObject> objekte, Class<T> datumTyp) throws DatenverteilerException;

    /**
     * Meldet ein Parameterdatum für ein oder mehrere Objekte zum Empfang wieder ab.
     */
    <T> void abmeldenVonParameter(Consumer<Datensatz<T>> empfaenger, Collection<SystemObject> objekte, Class<T> datumTyp) throws DatenverteilerException;

    /**
     * Ruft einen Parameter eines Objekts synchron ab. Gibt nie {@code null} zurück.
     */
    <T> Datensatz<T> getParameter(SystemObject objekt, Class<T> datumTyp) throws DatenverteilerException;

    /**
     * Liest ein Konfigurationdatum für Objekt ab. Gibt das Konfigurationsdatum oder {@code null} zurück, wenn es nicht
     * versorgt ist.
     */
    <T> T getKonfiguration(SystemObject objekt, Class<T> datumTyp) throws DatenverteilerException;

    /**
     * Sendet einen Datensatz.
     */
    void sendeDatensatz(Datensatz<?> datensatz) throws DatenverteilerException;

    /**
     * Sendet mehrere Datensätze.
     */
    void sendeDatensaetze(Collection<Datensatz<?>> datensaetze) throws DatenverteilerException;

    /**
     * Gibt das Objekt zu einer PID zurück. Gibt das Objekt oder {@code null} zurück, wenn kein Objekt zu der
     * angegebenen PID existiert.
     */
    SystemObject getObjekt(String pid) throws DatenverteilerException;

    /**
     * Gibt den Aspekt zu einer PID zurück. Gibt den Aspekt oder {@code null} zurück, wenn kein Aspekt zu der
     * angegebenen PID existiert.
     */
    Aspect getAspekt(String pid) throws DatenverteilerException;

}
