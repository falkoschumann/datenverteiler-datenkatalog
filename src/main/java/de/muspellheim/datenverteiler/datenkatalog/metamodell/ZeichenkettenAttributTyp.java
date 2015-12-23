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
public class ZeichenkettenAttributTyp extends SystemObjekt implements AttributTyp {

    private int laenge;
    private ZeichenKodierung kodierung = ZeichenKodierung.ISO_8859_1;

    public static ZeichenkettenAttributTyp erzeugeMitPid(String pid) {
        ZeichenkettenAttributTyp result = new ZeichenkettenAttributTyp();
        result.setPid(pid);
        return result;
    }

    public int getLaenge() {
        return laenge;
    }

    public void setLaenge(int laenge) {
        this.laenge = laenge;
    }

    public ZeichenKodierung getKodierung() {
        return kodierung;
    }

    public void setKodierung(ZeichenKodierung kodierung) {
        this.kodierung = kodierung;
    }

}
