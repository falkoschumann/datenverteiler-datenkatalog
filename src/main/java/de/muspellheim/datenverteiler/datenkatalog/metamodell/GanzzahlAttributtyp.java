/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Definiert ein Attribut vom Datentyp Ganzzahl mit seinen Eigenschaften.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class GanzzahlAttributtyp extends Attributtyp {

    private Wertebereich bereich;
    private Datentypgroesse anzahlBytes;
    private Set<Wertezustand> zustaende = new LinkedHashSet<>();

    public GanzzahlAttributtyp(String pid) {
        super(pid);
    }

    public Wertebereich getBereich() {
        return bereich;
    }

    public void setBereich(Wertebereich bereich) {
        this.bereich = bereich;
    }

    public Datentypgroesse getAnzahlBytes() {
        return anzahlBytes;
    }

    public void setAnzahlBytes(Datentypgroesse anzahlBytes) {
        this.anzahlBytes = anzahlBytes;
    }

    public Set<Wertezustand> getZustaende() {
        return zustaende;
    }

}
