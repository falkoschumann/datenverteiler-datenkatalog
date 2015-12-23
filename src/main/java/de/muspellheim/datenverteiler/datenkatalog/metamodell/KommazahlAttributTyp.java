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
public class KommazahlAttributTyp extends SystemObjekt implements AttributTyp {

    private String einheit;
    private FliesskommaAufloesung genauigkeit;

    public static KommazahlAttributTyp erzeugeMitPid(String pid) {
        KommazahlAttributTyp result = new KommazahlAttributTyp();
        result.setPid(pid);
        return result;
    }

    public String getEinheit() {
        return einheit;
    }

    public void setEinheit(String einheit) {
        this.einheit = einheit;
    }

    public FliesskommaAufloesung getGenauigkeit() {
        return genauigkeit;
    }

    public void setGenauigkeit(FliesskommaAufloesung genauigkeit) {
        this.genauigkeit = genauigkeit;
    }

}
