/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.datenverteiler;

/**
 * Schnittstelle für die wichtigsten Operationen mit dem Datenverteiler.
 *
 * @author Falko Schumann
 * @since 1.5
 */
public enum Empfaengeroption {

    /**
     * Diese Option meldet einen Empfänger für alle Datensätze, nicht nur geänderte, aber nicht für nachgelieferte an.
     */
    NORMAL("normal"),

    /**
     * Diese Option meldet einen Empfänger nur für geänderte Datensätze an.
     */
    DELTA("delta"),

    /**
     * Diese Option meldet einen Empfänger für alle Datensätze, nicht nur geänderte, einschließlich nachgelieferter an.
     */
    NACHGELIEFERT("nachgeliefert");

    private String text;

    private Empfaengeroption(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
