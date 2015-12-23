/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Bestimmt die Referenzierungsart (Gerichtete Assoziation, Aggregation und Komposition).
 *
 * @author Falko Schumann
 * @since 3.2
 */
public enum Referenzierungsart {

    /**
     * Die Referenzierungsart 'Gerichtete Assoziation'.
     */
    ASSOZIATION("Assoziation"),

    /**
     * Die Referenzierungsart 'Aggregation'.
     */
    AGGREGATION("Aggregation"),

    /**
     * Die Referenzierungsart 'Komposition'.
     */
    KOMPOSITION("Komposition");

    private final String text;

    Referenzierungsart(String text) {
        this.text = text;
    }

    public static Referenzierungsart of(String text) {
        Optional<Referenzierungsart> result = Arrays.stream(values()).filter(e -> e.text.equals(text)).findAny();
        if (result.isPresent())
            return result.get();

        throw new NoSuchElementException("Unbekannte Referenzierungsart: " + text);
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
