/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.util;

/**
 * Wird verwendet zur Verifikation von Vor- und Nachbedingungen von Funktionen.
 *
 * @author Falko Schumann
 * @since 1.0
 */
public final class Assert {

    /**
     * Verifiziert, dass eine Referenz nicht {@code null} ist.
     *
     * @param object die Referenz die verifiziert werden soll.
     * @throws NullPointerException wenn die Referenz {@code null} ist.
     */
    public static void notNull(Object object) {
        notNull("", object);
    }

    /**
     * Verifiziert, dass eine Referenz nicht {@code null} ist.
     *
     * @param message die Meldung, die der Exception mitgegegeben wird.
     * @param object  die Referenz die verifiziert werden soll.
     * @throws NullPointerException wenn die Referenz {@code null} ist.
     */
    public static void notNull(String message, Object object) {
        if (object == null) throw new NullPointerException(message);
    }

}
