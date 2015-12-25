/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

/**
 * Definiert ein Attribut vom Datentyp Kommazahl mit seinen Eigenschaften.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class KommazahlAttributtyp extends Attributtyp {

    private String einheit;
    private Fliesskommaaufloesung genauigkeit;

    public static KommazahlAttributtyp erzeugeMitPid(String pid) {
        KommazahlAttributtyp result = new KommazahlAttributtyp();
        result.setPid(pid);
        return result;
    }

    public String getEinheit() {
        return einheit;
    }

    public void setEinheit(String einheit) {
        this.einheit = einheit;
    }

    public Fliesskommaaufloesung getGenauigkeit() {
        return genauigkeit;
    }

    public void setGenauigkeit(Fliesskommaaufloesung genauigkeit) {
        this.genauigkeit = genauigkeit;
    }

}
