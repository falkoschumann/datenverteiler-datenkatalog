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
public final class Mengenverwendung extends Systemobjekt {

    private String mengenname;
    private Mengentyp mengentyp;
    private boolean erforderlich;

    private Mengenverwendung() {
        super(null);
    }

    public static Mengenverwendung erzeuge(String mengenName, Mengentyp mengenTyp) {
        return erzeuge(mengenName, mengenTyp, true);
    }

    public static Mengenverwendung erzeuge(String mengenName, Mengentyp mengenTyp, boolean erforderlich) {
        Mengenverwendung result = new Mengenverwendung();
        result.mengenname = mengenName;
        result.mengentyp = mengenTyp;
        result.erforderlich = erforderlich;
        return result;
    }

    /**
     * Persistenter Name der Menge.
     * <p>
     * Der Name unter dem die Menge ausgehend von einem Objekt des jeweiligen Typs referenzierbar ist.
     * </p>
     */
    public String getMengenname() {
        return mengenname;
    }

    /**
     * Referenz auf die MengenDefinition die den Typ der Menge beschreibt.
     */
    public Mengentyp getMengentyp() {
        return mengentyp;
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
        Mengenverwendung that = (Mengenverwendung) o;
        return Objects.equals(mengenname, that.mengenname) &&
                Objects.equals(mengentyp, that.mengentyp) && Objects.equals(erforderlich, that.erforderlich);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mengenname, mengentyp, erforderlich);
    }

}
