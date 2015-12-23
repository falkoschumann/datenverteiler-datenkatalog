/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Kodierung von Zeichenketten.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public enum ZeichenKodierung {

    ISO_8859_1("ISO-8859-1");

    private final String text;

    ZeichenKodierung(String text) {
        this.text = text;
    }

    public static ZeichenKodierung of(String text) {
        Optional<ZeichenKodierung> result = Arrays.stream(values()).filter(e -> e.text.equals(text)).findAny();
        if (result.isPresent())
            return result.get();

        throw new NoSuchElementException("Unbekannte Zeichenkodierung: " + text);
    }

    @Override
    public String toString() {
        return text;
    }

}
