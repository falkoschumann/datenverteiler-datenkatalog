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

    private final Set<Typ> objektTypen = new TreeSet<>();

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
