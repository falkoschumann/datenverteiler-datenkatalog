/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import java.util.Set;
import java.util.TreeSet;

/**
 * Beschreibt die Verwendung einer MengenDefinition bei einer TypDefinition.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class MengenTyp extends Typ {

    private final Set<Typ> objektTypen = new TreeSet<>(SystemObjekt::compareToNameOderPid);
    private int minimaleAnzahl;
    private int maximaleAnzahl;
    private boolean aenderbar;
    private Referenzierungsart referenzierungsart;

    public static MengenTyp erzeugeMitPid(String pid) {
        MengenTyp result = new MengenTyp();
        result.setPid(pid);
        return result;
    }

    /**
     * Enthält die möglichen Typen von Objekten, die in Mengen des jeweiligen MengenTyps verwendet werden können.
     */
    public Set<Typ> getObjektTypen() {
        return objektTypen;
    }

    /**
     * Minimale Anzahl der Elemente in der Menge.
     */
    public int getMinimaleAnzahl() {
        return minimaleAnzahl;
    }

    /**
     * Minimale Anzahl der Elemente in der Menge.
     */
    public void setMinimaleAnzahl(int minimaleAnzahl) {
        this.minimaleAnzahl = minimaleAnzahl;
    }

    /**
     * Maximale Anzahl der Elemente in der Menge.
     */
    public int getMaximaleAnzahl() {
        return maximaleAnzahl;
    }

    /**
     * Maximale Anzahl der Elemente in der Menge.
     */
    public void setMaximaleAnzahl(int maximaleAnzahl) {
        this.maximaleAnzahl = maximaleAnzahl;
    }

    /**
     * Legt fest, ob Mengen dieses Typs online änderbar sind.
     */
    public boolean isAenderbar() {
        return aenderbar;
    }

    /**
     * Legt fest, ob Mengen dieses Typs online änderbar sind.
     */
    public void setAenderbar(boolean aenderbar) {
        this.aenderbar = aenderbar;
    }

    /**
     * Referenzierungsart von Mengen (Gerichtete Assoziation, Aggregation oder Komposition).
     * <p>Zu jedem Mengentyp von Konfigurationsmengen muss verwaltet werden, ob die Referenzen auf die Elemente einer
     * Menge dieses Typs als gerichtete Assoziation, als Aggregation oder als Komposition realisiert werden soll.
     * (TPuK1-90 Referenzierungsart von Konfigurationsmengen). Zu jedem Mengentyp von dynamischen Mengen darf als
     * Referenzierungsart nur die gerichtete Assoziation zugelassen werden. (TPuK1-91 Referenzierungsart von dynamischen
     * Mengen).</p>
     */
    public Referenzierungsart getReferenzierungsart() {
        return referenzierungsart;
    }

    /**
     * Referenzierungsart von Mengen (Gerichtete Assoziation, Aggregation oder Komposition).
     * <p>Zu jedem Mengentyp von Konfigurationsmengen muss verwaltet werden, ob die Referenzen auf die Elemente einer
     * Menge dieses Typs als gerichtete Assoziation, als Aggregation oder als Komposition realisiert werden soll.
     * (TPuK1-90 Referenzierungsart von Konfigurationsmengen). Zu jedem Mengentyp von dynamischen Mengen darf als
     * Referenzierungsart nur die gerichtete Assoziation zugelassen werden. (TPuK1-91 Referenzierungsart von dynamischen
     * Mengen).</p>
     */
    public void setReferenzierungsart(Referenzierungsart referenzierungsart) {
        this.referenzierungsart = referenzierungsart;
    }

}
