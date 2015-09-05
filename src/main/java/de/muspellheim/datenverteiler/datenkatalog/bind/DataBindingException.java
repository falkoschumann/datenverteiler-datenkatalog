/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.bind;

/**
 * Wird bei Fehlern beim Überführen eines POJO, das ein Datum darstellt, in ein {@code Data}, das eine
 * Attributgruppe darstellt oder beim umgekehrten Überführungsweg geworfen.
 *
 * @author Falko Schumann
 * @since 1.0
 */
public class DataBindingException extends RuntimeException {

    /**
     * Erzeugt eine Ausnahme mit der dazugehörigen Fehlermeldung.
     *
     * @param nachricht die Fehlermeldung zur Ausnahme.
     */
    public DataBindingException(String nachricht) {
        super(nachricht);
    }

    /**
     * Erzeugt eine Ausnahme mit der dazugehörigen Fehlermeldung und dem Fehlergrund.
     *
     * @param nachricht die Fehlermeldung zur Ausnahme.
     * @param ursaache  der Grund der Ausnahme.
     */
    public DataBindingException(String nachricht, Throwable ursaache) {
        super(nachricht, ursaache);
    }

}
