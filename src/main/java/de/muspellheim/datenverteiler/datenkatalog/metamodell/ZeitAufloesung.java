/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Zeitliche Auflösung einer Zeitangabe.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public enum ZeitAufloesung {

    SEKUNDEN("Sekunden"),

    MILLISEKUNDEN("Millisekunden");

    private final String text;

    ZeitAufloesung(String text) {
        this.text = text;
    }

    public static ZeitAufloesung of(String text) {
        Optional<ZeitAufloesung> result = Arrays.stream(values()).filter(e -> e.text.equals(text)).findAny();
        if (result.isPresent())
            return result.get();

        throw new NoSuchElementException("Unbekannte Zeitauflösung: " + text);
    }

    @Override
    public String toString() {
        return text;
    }

}
