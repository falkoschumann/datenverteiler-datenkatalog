/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

/**
 * Definiert ein Attribut vom Datentyp Zeichenkette mit seinen Eigenschaften.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class ZeichenkettenAttributtyp extends Attributtyp {

    private int laenge;
    private Zeichenkodierung kodierung = Zeichenkodierung.ISO_8859_1;

    public ZeichenkettenAttributtyp(String pid) {
        super(pid);
    }

    public int getLaenge() {
        return laenge;
    }

    public void setLaenge(int laenge) {
        this.laenge = laenge;
    }

    public Zeichenkodierung getKodierung() {
        return kodierung;
    }

    public void setKodierung(Zeichenkodierung kodierung) {
        this.kodierung = kodierung;
    }

}
