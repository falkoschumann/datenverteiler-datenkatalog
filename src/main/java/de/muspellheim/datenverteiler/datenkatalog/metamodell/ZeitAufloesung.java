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
public enum Zeitaufloesung {

    SEKUNDEN("Sekunden"),

    MILLISEKUNDEN("Millisekunden");

    private final String text;

    Zeitaufloesung(String text) {
        this.text = text;
    }

    public static Zeitaufloesung of(String text) {
        Optional<Zeitaufloesung> result = Arrays.stream(values()).filter(e -> e.text.equals(text)).findAny();
        if (result.isPresent())
            return result.get();

        throw new NoSuchElementException("Unbekannte Zeitauflösung: " + text);
    }

    @Override
    public String toString() {
        return text;
    }

}
