/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.modell;

import de.muspellheim.datenverteiler.datenkatalog.bind.AttributlistenDefinition;

import java.util.Objects;

@AttributlistenDefinition
public class Status {

    private StatusExterneErfassung erfassung = new StatusExterneErfassung();
    private StatusPlausibilitaetsPruefungFormal plFormal = new StatusPlausibilitaetsPruefungFormal();
    private StatusMessWertErsetzung messWertErsetzung = new StatusMessWertErsetzung();

    public StatusExterneErfassung getErfassung() {
        return erfassung;
    }

    public void setErfassung(StatusExterneErfassung erfassung) {
        this.erfassung = erfassung;
    }

    public StatusPlausibilitaetsPruefungFormal getPlFormal() {
        return plFormal;
    }

    public void setPlFormal(StatusPlausibilitaetsPruefungFormal plFormal) {
        this.plFormal = plFormal;
    }

    public StatusMessWertErsetzung getMessWertErsetzung() {
        return messWertErsetzung;
    }

    public void setMessWertErsetzung(StatusMessWertErsetzung messWertErsetzung) {
        this.messWertErsetzung = messWertErsetzung;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status status = (Status) o;
        return Objects.equals(erfassung, status.erfassung) &&
                Objects.equals(plFormal, status.plFormal) &&
                Objects.equals(messWertErsetzung, status.messWertErsetzung);
    }

    @Override
    public int hashCode() {
        return Objects.hash(erfassung, plFormal, messWertErsetzung);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Status{");
        sb.append("erfassung=").append(erfassung);
        sb.append(", plFormal=").append(plFormal);
        sb.append(", messWertErsetzung=").append(messWertErsetzung);
        sb.append('}');
        return sb.toString();
    }

}
