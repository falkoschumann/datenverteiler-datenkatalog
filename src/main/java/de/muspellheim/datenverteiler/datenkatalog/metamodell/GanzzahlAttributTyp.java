/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

/**
 * Definiert ein Attribut vom Datentyp Ganzzahl mit seinen Eigenschaften.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class GanzzahlAttributTyp extends SystemObjekt implements AttributTyp {

    private WerteBereich bereich;
    private DatentypGroesse anzahlBytes;

    public static GanzzahlAttributTyp erzeugeMitPid(String pid) {
        GanzzahlAttributTyp result = new GanzzahlAttributTyp();
        result.setPid(pid);
        return result;
    }

    public WerteBereich getBereich() {
        return bereich;
    }

    public void setBereich(WerteBereich bereich) {
        this.bereich = bereich;
    }

    public DatentypGroesse getAnzahlBytes() {
        return anzahlBytes;
    }

    public void setAnzahlBytes(DatentypGroesse anzahlBytes) {
        this.anzahlBytes = anzahlBytes;
    }

}
