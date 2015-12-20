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
        String result = systemObjekt.getPid();
        if (result.startsWith("typ.")) result = result.substring(4);
        if (result.startsWith("atg.")) result = result.substring(4);
        if (result.startsWith("atl.")) result = result.substring(4);
        if (result.startsWith("att.")) result = result.substring(4);
        result = bezeichner(result);
        result = result.substring(0, 1).toUpperCase() + result.substring(1);
        return result;
    }

    public static String bezeichner(String name) {
        return name.
                replaceAll("Ä", "Ae").
                replaceAll("ä", "ae").
                replaceAll("Ö", "Oe").
                replaceAll("ö", "oe").
                replaceAll("Ü", "Ue").
                replaceAll("ü", "ue").
                replaceAll("ß", "ss");
    }

}
