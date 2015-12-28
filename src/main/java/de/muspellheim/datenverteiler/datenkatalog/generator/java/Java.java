/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.generator.java;

import de.muspellheim.datenverteiler.datenkatalog.metamodell.*;

/**
 * Utility-Klasse für das Generieren von Java-Klassen.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public final class Java {

    private static final boolean verwendeNameAlsBezeichner;

    static {
        // TODO System-Property dokumentieren
        verwendeNameAlsBezeichner = "name".equals(System.getProperty("generator.java.bezeichner", "name").toLowerCase());
        if (verwendeNameAlsBezeichner) {
            System.out.println("Verwende den Namen der Objekte als Klassenbezeichner.");
        } else {
            System.out.println("Verwende die PID der Objekte als Klassenbezeichner.");
        }
    }

    private Java() {
        // utility class
    }

    public static String paket(Systemobjekt systemObjekt) {
        String kv = systemObjekt.getKonfigurationsbereich().getZustaendiger().getPid();
        if (kv.startsWith("kv.")) kv = kv.substring(3);
        kv = bezeichner(kv).toLowerCase();
        String kb = systemObjekt.getKonfigurationsbereich().getPid();
        if (kb.startsWith("kb.")) kb = kb.substring(3);
        kb = bezeichner(kb).toLowerCase();
        return kv + "." + kb;
    }

    public static String klasse(Systemobjekt systemObjekt) {
        String result;
        if (verwendeNameAlsBezeichner) {
            result = systemObjekt.getNameOderPid();
        } else {
            result = systemObjekt.getPid();
        }
        result = bezeichner(result);
        result = result.substring(0, 1).toUpperCase() + result.substring(1);
        if (systemObjekt instanceof Attributgruppe)
            result = "Atg" + result;
        else if (systemObjekt instanceof Attributliste)
            result = "Atl" + result;
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
        if (!Character.isJavaIdentifierStart(result.codePointAt(0))) {
            result = "_" + result;
            // TODO throw new IllegalArgumentException("Verbotenes Startzeichen für Java-Bezeichner: " + name);
        }
        for (int i = 0; i < result.length(); i++) {
            if (!Character.isJavaIdentifierPart(result.codePointAt(i))) {
                result = result.substring(0, i) +
                        result.substring(i + 1, i + 2).toUpperCase() +
                        result.substring(i + 2);
            }
        }
        result = result.substring(0, 1).toLowerCase() + result.substring(1);
        return result;
    }

    public static String getter(Attribut attribut) {
        // TODO att.jaNein als boolean behandeln
        String result = bezeichner(attribut.getName());
        result = result.substring(0, 1).toUpperCase() + result.substring(1);
        result = "get" + result;
        return result;
    }

    public static String setter(Attribut attribut) {
        String result = bezeichner(attribut.getName());
        result = result.substring(0, 1).toUpperCase() + result.substring(1);
        result = "set" + result;
        return result;
    }

    public static String typ(Attributtyp attributtyp) {
        // TODO att.jaNein als boolean behandeln
        if (attributtyp instanceof ZeichenkettenAttributtyp) {
            return "String";
        }
        if (attributtyp instanceof ZeitstempelAttributtyp) {
            ZeitstempelAttributtyp zeitstempel = (ZeitstempelAttributtyp) attributtyp;
            if (zeitstempel.isRelativ())
                return "Duration";
            return "LocalDateTime";
        }
        if (attributtyp instanceof KommazahlAttributtyp) {
            KommazahlAttributtyp kommazahl = (KommazahlAttributtyp) attributtyp;
            switch (kommazahl.getGenauigkeit()) {
                case FLOAT:
                    return "float";
                case DOUBLE:
                    return "double";
                default:
                    throw new IllegalStateException("Unbekannte Fließkommaauflösung: " + kommazahl.getGenauigkeit());
            }

        }
        if (attributtyp instanceof ObjektreferenzAttributtyp) {
            return "SystemObject";
        }
        if (attributtyp instanceof GanzzahlAttributtyp) {
            GanzzahlAttributtyp ganzzahl = (GanzzahlAttributtyp) attributtyp;
            switch (ganzzahl.getAnzahlBytes()) {
                case BYTE:
                    return "byte";
                case SHORT:
                    return "short";
                case INT:
                    return "int";
                case LONG:
                    return "long";
                default:
                    throw new IllegalStateException("Unbekannte Datentypgröße: " + ganzzahl.getAnzahlBytes());
            }
        }
        if (attributtyp instanceof Attributliste) {
            return klasse(attributtyp);
        }
        throw new IllegalArgumentException("Unbekannter Attributtyp: " + attributtyp);
    }

}
