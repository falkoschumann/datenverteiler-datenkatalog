/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.modell;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.datenkatalog.bind.AttributgruppenDefinition;
import de.bsvrz.sys.funclib.datenkatalog.bind.Ignorieren;

import java.util.Objects;

@AttributgruppenDefinition(pid = "atg.achsLastMessStelle")
public class AchsLastMessStelle {

    private SystemObject achsLastMessStellenQuelle;
    private SystemObject fahrStreifen;
    private String zusatzProperty;

    public SystemObject getAchsLastMessStellenQuelle() {
        return achsLastMessStellenQuelle;
    }

    public void setAchsLastMessStellenQuelle(SystemObject achsLastMessStellenQuelle) {
        this.achsLastMessStellenQuelle = achsLastMessStellenQuelle;
    }

    public SystemObject getFahrStreifen() {
        return fahrStreifen;
    }

    public void setFahrStreifen(SystemObject fahrStreifen) {
        this.fahrStreifen = fahrStreifen;
    }

    @Ignorieren
    public String getZusatzProperty() {
        return zusatzProperty;
    }

    public void setZusatzProperty(String zusatzProperty) {
        this.zusatzProperty = zusatzProperty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AchsLastMessStelle that = (AchsLastMessStelle) o;
        return Objects.equals(achsLastMessStellenQuelle, that.achsLastMessStellenQuelle) &&
                Objects.equals(fahrStreifen, that.fahrStreifen) &&
                Objects.equals(zusatzProperty, that.zusatzProperty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(achsLastMessStellenQuelle, fahrStreifen, zusatzProperty);
    }

    @Override
    public String toString() {
        return "AchsLastMessStelle{" +
                "achsLastMessStellenQuelle=" + achsLastMessStellenQuelle +
                ", fahrStreifen=" + fahrStreifen +
                ", zusatzProperty='" + zusatzProperty + '\'' +
                '}';
    }

}
