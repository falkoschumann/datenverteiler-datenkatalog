/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Verwendungsparameter in Online- oder in konfigurierenden Datensätzen.
 * <p>Hier wird verwaltet, ob ein konfigurierender Datensatz dieser Verwendung notwendigerweise an jedem Objekt des
 * entsprechenden Typs sein muss, oder ob der Datensatz optional vorhanden sein kann und ob die konfigurierenden
 * Datensätze dieser Verwendung änderbar sind, oder nicht.</p>
 *
 * @author Falko Schumann
 * @since 3.2
 */
public enum DatensatzVerwendung {

    KONFIGURIERENDER_DATENSATZ_NOTWENDIG("KonfigurierenderDatensatzNotwendig"),

    KONFIGURIERENDER_DATENSATZ_NOTWENDIG_UND_AENDERBAR("KonfigurierenderDatensatzNotwendigUndÄnderbar"),

    KONFIGURIERENDER_DATENSATZ_OPTIONAL("KonfigurierenderDatensatzOptional"),

    KONFIGURIERENDER_DATENSATZ_OPTIONALUND_AENDERBAR("KonfigurierenderDatensatzOptionalUndÄnderbar"),

    ONLINE_DATENSATZ_QUELLE("OnlineDatensatzQuelle"),

    ONLINE_DATENSATZ_SENKE("OnlineDatensatzSenke"),

    ONLINE_DATENSATZ_QUELLE_UND_SENKE("OnlineDatensatzQuelleUndSenke");

    private final String text;

    DatensatzVerwendung(String text) {
        this.text = text;
    }

    public static DatensatzVerwendung of(String text) {
        Optional<DatensatzVerwendung> result = Arrays.stream(values()).filter(e -> e.text.equals(text)).findAny();
        if (result.isPresent())
            return result.get();

        throw new NoSuchElementException("Unbekannte Datensatzverwendung: " + text);
    }

    @Override
    public String toString() {
        return text;
    }

}
