/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.generator.java;

import de.muspellheim.datenverteiler.datenkatalog.metamodell.SystemObjekt;

/**
 * Utility-Klasse für das Generieren von Java-Klassen.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public final class Java {

    private static final boolean verwendePidAlsBezeicher;

    static {
        // TODO System-Property dokumentieren
        verwendePidAlsBezeicher = "name".equals(System.getProperty("generator.java.klassenname", "name").toLowerCase());
        if (verwendePidAlsBezeicher) {
            System.out.println("Verwende den Namen der Objekte als Klassenbezeichner.");
        } else {
            System.out.println("Verwende die PID der Objekte als Klassenbezeichner.");
        }
    }

    private Java() {
        // utility class
    }

    public static String paket(SystemObjekt systemObjekt) {
        String kv = systemObjekt.getBereich().getZustaendiger().getPid();
        if (kv.startsWith("kv.")) kv = kv.substring(3);
        kv = bezeichner(kv).toLowerCase();
        String kb = systemObjekt.getBereich().getPid();
        if (kb.startsWith("kb.")) kb = kb.substring(3);
        kb = bezeichner(kb).toLowerCase();
        return kv + "." + kb;
    }

    public static String klasse(SystemObjekt systemObjekt) {
        String result;
        if (verwendePidAlsBezeicher) {
            result = systemObjekt.getNameOderPid();
        } else {
            result = systemObjekt.getPid();
        }
        result = bezeichner(result);
        result = result.substring(0, 1).toUpperCase() + result.substring(1);
        return result;
    }

    public static String bezeichner(String name) {
        String result = name.
                replaceAll("Ä", "Ae").
                replaceAll("ä", "ae").
                replaceAll("Ö", "Oe").
                replaceAll("ö", "oe").
                replaceAll("Ü", "Ue").
                replaceAll("ü", "ue").
                replaceAll("ß", "ss");
        if (!Character.isJavaIdentifierStart(name.codePointAt(0)))
            throw new IllegalArgumentException("Verbotenes Startzeichen für Java-Bezeichner: " + name);
        for (int i = 0; i < result.length(); i++) {
            if (!Character.isJavaIdentifierPart(result.codePointAt(i))) {
                result = result.substring(0, i) +
                        result.substring(i + 1, i + 2).toUpperCase() +
                        result.substring(i + 2);
            }
        }
        return result;
    }

}
