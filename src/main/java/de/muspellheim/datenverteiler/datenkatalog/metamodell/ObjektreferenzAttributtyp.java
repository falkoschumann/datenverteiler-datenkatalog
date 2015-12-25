/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

/**
 * Definiert ein Attribut vom Datentyp Objektreferenz mit seinen Eigenschaften.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class ObjektreferenzAttributtyp extends Attributtyp {

    private Typ typ;
    private boolean undefiniertErlaubt;
    private Referenzierungsart referenzierungsart;

    public static ObjektreferenzAttributtyp erzeugeMitPid(String pid) {
        ObjektreferenzAttributtyp result = new ObjektreferenzAttributtyp();
        result.setPid(pid);
        return result;
    }

    /**
     * Gibt den Objekttyp der referenzierten Objekte an.
     * <p>Zu Referenz-Attributtypen muss verwaltet werden, von welchem Objekttyp die durch Attributwerte dieses
     * Attributtyps referenzierten Objekte sein müssen (TPuK1-73 Objekttyp der referenzierten Objekte).</p>
     */
    public Typ getTyp() {
        return typ;
    }

    /**
     * Gibt den Objekttyp der referenzierten Objekte an.
     * <p>Zu Referenz-Attributtypen muss verwaltet werden, von welchem Objekttyp die durch Attributwerte dieses
     * Attributtyps referenzierten Objekte sein müssen (TPuK1-73 Objekttyp der referenzierten Objekte).</p>
     */
    public void setTyp(Typ typ) {
        this.typ = typ;
    }

    /**
     * Gibt an, ob undefinierte Referenzen in Attributwerten dieses Attributtyps zugelassen werden.
     * <p>Zu Referenz-Attributtypen muss verwaltet werden, ob undefinierte Referenzen in Attributwerten dieses
     * Attributtyps zugelassen werden (TPuK1-74 Undefinierte Referenzen).</p>
     */
    public boolean isUndefiniertErlaubt() {
        return undefiniertErlaubt;
    }

    /**
     * Gibt an, ob undefinierte Referenzen in Attributwerten dieses Attributtyps zugelassen werden.
     * <p>Zu Referenz-Attributtypen muss verwaltet werden, ob undefinierte Referenzen in Attributwerten dieses
     * Attributtyps zugelassen werden (TPuK1-74 Undefinierte Referenzen).</p>
     */
    public void setUndefiniertErlaubt(boolean undefiniertErlaubt) {
        this.undefiniertErlaubt = undefiniertErlaubt;
    }

    /**
     * Referenzierungsart von Referenzen (Gerichtete Assoziation, Aggregation oder Komposition).
     * <p>Zu Referenz-Attributtypen muss verwaltet werden, ob die in Attributwerten dieses Attributtyps enthaltenen
     * Referenzen als gerichtete Assoziationen, als Aggregationen oder als Kompositionen realisiert werden sollen
     * (TPuK1-75 Referenzierungsart von Referenzen).</p>
     */
    public Referenzierungsart getReferenzierungsart() {
        return referenzierungsart;
    }

    /**
     * Referenzierungsart von Referenzen (Gerichtete Assoziation, Aggregation oder Komposition).
     * <p>Zu Referenz-Attributtypen muss verwaltet werden, ob die in Attributwerten dieses Attributtyps enthaltenen
     * Referenzen als gerichtete Assoziationen, als Aggregationen oder als Kompositionen realisiert werden sollen
     * (TPuK1-75 Referenzierungsart von Referenzen).</p>
     */
    public void setReferenzierungsart(Referenzierungsart referenzierungsart) {
        this.referenzierungsart = referenzierungsart;
    }

}
