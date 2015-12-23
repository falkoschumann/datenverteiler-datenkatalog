/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Genauigkeit eines Fließkommawertes.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public enum FliesskommaAufloesung {

    /**
     * Genauigkeit entsprechend dem IEEE 754 floating-point"single format" mit 32 Bits.
     */
    FLOAT("Float"),

    /**
     * Genauigkeit entsprechend dem IEEE 754 floating-point"double format" mit 64 Bits.
     */
    DOUBLE("Double");

    private final String text;

    FliesskommaAufloesung(String text) {
        this.text = text;
    }

    public static FliesskommaAufloesung of(String text) {
        Optional<FliesskommaAufloesung> result = Arrays.stream(values()).filter(e -> e.text.equals(text)).findAny();
        if (result.isPresent())
            return result.get();

        throw new NoSuchElementException("Unbekannte Fließkommaauflösung: " + text);
    }

    @Override
    public String toString() {
        return text;
    }

}
