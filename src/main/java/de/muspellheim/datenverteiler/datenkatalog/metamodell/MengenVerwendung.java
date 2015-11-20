/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

/**
 * Beschreibt die Verwendung einer MengenDefinition bei einer TypDefinition.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class MengenVerwendung {

    private String mengenName;
    private MengenTyp mengenTyp;

    /**
     * Persistenter Name der Menge.
     * <p>
     * Der Name unter dem die Menge ausgehend von einem Objekt des jeweiligen Typs referenzierbar ist.
     * </p>
     */
    public String getMengenName() {
        return mengenName;
    }

    /**
     * Persistenter Name der Menge.
     * <p>
     * Der Name unter dem die Menge ausgehend von einem Objekt des jeweiligen Typs referenzierbar ist.
     * </p>
     */
    public void setMengenName(String mengenName) {
        this.mengenName = mengenName;
    }

    /**
     * Referenz auf die MengenDefinition die den Typ der Menge beschreibt.
     */
    public MengenTyp getMengenTyp() {
        return mengenTyp;
    }

    /**
     * Referenz auf die MengenDefinition die den Typ der Menge beschreibt.
     */
    public void setMengenTyp(MengenTyp mengenTyp) {
        this.mengenTyp = mengenTyp;
    }

}
