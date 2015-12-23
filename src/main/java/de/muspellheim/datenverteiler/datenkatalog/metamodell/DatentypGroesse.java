/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Anzahl der von einem einfachen Datentyp benötigten Bytes.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public enum DatentypGroesse {

    BYTE("Byte"),

    SHORT("Short"),

    INT("Int"),

    LONG("Long");

    private final String text;

    DatentypGroesse(String text) {
        this.text = text;
    }

    public static DatentypGroesse of(String text) {
        Optional<DatentypGroesse> result = Arrays.stream(values()).filter(e -> e.text.equals(text)).findAny();
        if (result.isPresent())
            return result.get();

        throw new NoSuchElementException("Unbekannte Datentypgröße: " + text);
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
