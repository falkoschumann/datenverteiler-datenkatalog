/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.modell;

import de.bsvrz.dav.daf.main.config.SystemObject;

import java.util.Objects;

public class MessQuerschnittAllgemein {

    private MessQuerschnittTyp typ;
    private SystemObject ersatzMessQuerschnitt;

    public MessQuerschnittTyp getTyp() {
        return typ;
    }

    public void setTyp(MessQuerschnittTyp typ) {
        this.typ = typ;
    }

    public SystemObject getErsatzMessQuerschnitt() {
        return ersatzMessQuerschnitt;
    }

    public void setErsatzMessQuerschnitt(SystemObject ersatzMessQuerschnitt) {
        this.ersatzMessQuerschnitt = ersatzMessQuerschnitt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessQuerschnittAllgemein that = (MessQuerschnittAllgemein) o;
        return Objects.equals(typ, that.typ) &&
                Objects.equals(ersatzMessQuerschnitt, that.ersatzMessQuerschnitt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typ, ersatzMessQuerschnitt);
    }

    @Override
    public String toString() {
        return "MessQuerschnittAllgemein{" +
                "typ=" + typ +
                ", ersatzMessQuerschnitt=" + ersatzMessQuerschnitt +
                '}';
    }

}
