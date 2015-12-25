/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

/**
 * Definiert ein Attribut vom Datentyp Zeitstempel mit seinen Eigenschaften.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class ZeitstempelAttributTyp extends AttributTyp {

    private boolean relativ;
    private ZeitAufloesung genauigkeit = ZeitAufloesung.MILLISEKUNDEN;

    public static ZeitstempelAttributTyp erzeugeMitPid(String pid) {
        ZeitstempelAttributTyp result = new ZeitstempelAttributTyp();
        result.setPid(pid);
        return result;
    }

    /**
     * Gibt an, ob eine Attribut dieses Attributtyps eine relative oder absolute Zeitangabe ist. Bei absoluten
     * Zeitangaben bezieht sich die Angabe auf den 1. Januar 1970 00:00 Uhr.
     */
    public boolean isRelativ() {
        return relativ;
    }

    /**
     * Gibt an, ob eine Attribut dieses Attributtyps eine relative oder absolute Zeitangabe ist. Bei absoluten
     * Zeitangaben bezieht sich die Angabe auf den 1. Januar 1970 00:00 Uhr.
     */
    public void setRelativ(boolean relativ) {
        this.relativ = relativ;
    }

    public ZeitAufloesung getGenauigkeit() {
        return genauigkeit;
    }

    public void setGenauigkeit(ZeitAufloesung genauigkeit) {
        this.genauigkeit = genauigkeit;
    }

}
