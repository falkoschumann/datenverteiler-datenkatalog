/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import java.util.Objects;

/**
 * Beschreibt die Verwendung einer MengenDefinition bei einer TypDefinition.
 * <p><strong>Hinweis: </strong> Eine Mengenverwendung ist ein Wertobjekt und wird über seine Eigenschaften identifiziert.</p>
 *
 * @author Falko Schumann
 * @since 3.2
 */
public final class MengenVerwendung extends SystemObjekt {

    // TODO Soll SystemObjekt für Kurzinfo und Beschreibung abgeleitet werden?

    private String mengenName;
    private MengenTyp mengenTyp;
    private boolean erforderlich;

    private MengenVerwendung() {
        // value object
    }

    public static MengenVerwendung erzeuge(String mengenName, MengenTyp mengenTyp) {
        return erzeuge(mengenName, mengenTyp, true);
    }

    public static MengenVerwendung erzeuge(String mengenName, MengenTyp mengenTyp, boolean erforderlich) {
        MengenVerwendung result = new MengenVerwendung();
        result.mengenName = mengenName;
        result.mengenTyp = mengenTyp;
        result.erforderlich = erforderlich;
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
     * Referenz auf die MengenDefinition die den Typ der Menge beschreibt.
     */
    public MengenTyp getMengenTyp() {
        return mengenTyp;
    }

    /**
     * Gibt an ob die Existenz der Menge unter einem Objekt des jeweiligen Typs erforderlich ist.
     */
    public boolean isErforderlich() {
        return erforderlich;
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
