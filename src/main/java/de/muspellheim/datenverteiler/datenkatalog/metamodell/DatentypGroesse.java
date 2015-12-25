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
public enum Datentypgroesse {

    BYTE("Byte"),

    SHORT("Short"),

    INT("Int"),

    LONG("Long");

    private final String text;

    Datentypgroesse(String text) {
        this.text = text;
    }

    public static Datentypgroesse of(String text) {
        Optional<Datentypgroesse> result = Arrays.stream(values()).filter(e -> e.text.equals(text)).findAny();
        if (result.isPresent())
            return result.get();

        throw new NoSuchElementException("Unbekannte Datentypgröße: " + text);
    }

    @Override
    public String toString() {
        return text;
    }

}
