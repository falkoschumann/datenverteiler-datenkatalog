/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import java.util.Objects;

/**
 * Beschreibt die Verwendung einer MengenDefinition bei einer TypDefinition.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class MengenVerwendung extends SystemObjekt {

    private String mengenName;
    private MengenTyp mengenTyp;
    private boolean erforderlich = true;

    public static MengenVerwendung erzeugeMitNameUndTyp(String mengenName, MengenTyp mengenTyp) {
        MengenVerwendung result = new MengenVerwendung();
        result.setMengenName(mengenName);
        result.setMengenTyp(mengenTyp);
        return result;
    }

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

    /**
     * Gibt an ob die Existenz der Menge unter einem Objekt des jeweiligen Typs erforderlich ist.
     */
    public boolean isErforderlich() {
        return erforderlich;
    }

    /**
     * Gibt an ob die Existenz der Menge unter einem Objekt des jeweiligen Typs erforderlich ist.
     */
    public void setErforderlich(boolean erforderlich) {
        this.erforderlich = erforderlich;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MengenVerwendung that = (MengenVerwendung) o;
        return Objects.equals(mengenName, that.mengenName) &&
                Objects.equals(mengenTyp, that.mengenTyp) && Objects.equals(erforderlich, that.erforderlich);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mengenName, mengenTyp, erforderlich);
    }

}
