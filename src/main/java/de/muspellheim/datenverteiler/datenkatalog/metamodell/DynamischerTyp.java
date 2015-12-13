/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

/**
 * Der Typ aller dynamischen Typobjekte.
 * <p>Objekte dieses Typs sind die Typen, die direkt oder indirekt den Typ DynamischesObjekt erweitern, also die Typen
 * deren Objekte zur Laufzeit erzeugt und gelöscht werden können.</p>
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class DynamischerTyp extends Typ {

    public static DynamischerTyp erzeugeMitPid(String pid) {
        DynamischerTyp result = new DynamischerTyp();
        result.setPid(pid);
        return result;
    }

    @Override
    public String toString() {
        return "DynamischerTyp{" +
                "name='" + getName() + '\'' +
                ", pid='" + getPid() + '\'' +
                ", kurzinfo='" + getKurzinfo() + '\'' +
                ", beschreibung='" + getBeschreibung() + '\'' +
                ", mengen=" + getMengen() +
                ", superTypen=" + getSuperTypen() +
                "}";
    }

}
