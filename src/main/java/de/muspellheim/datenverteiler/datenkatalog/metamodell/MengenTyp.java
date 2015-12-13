/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Beschreibt die Verwendung einer MengenDefinition bei einer TypDefinition.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class MengenTyp extends SystemObjekt {

    private Set<Typ> objektTypen = new LinkedHashSet<>();

    /**
     * Enthält die möglichen Typen von Objekten, die in Mengen des jeweiligen MengenTyps verwendet werden können.
     */
    public Set<Typ> getObjektTypen() {
        return objektTypen;
    }

    public static MengenTyp erzeugeMitPid(String pid) {
        MengenTyp result = new MengenTyp();
        result.setPid(pid);
        return result;
    }

    @Override
    public String toString() {
        return "MengenVerwendung{" +
                "name='" + getName() + '\'' +
                ", pid='" + getPid() + '\'' +
                ", kurzinfo='" + getKurzinfo() + '\'' +
                ", beschreibung='" + getBeschreibung() + '\'' +
                ", objektTypen=" + objektTypen +
                "}";
    }

}
