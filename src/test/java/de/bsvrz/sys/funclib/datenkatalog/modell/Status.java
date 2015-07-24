/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.modell;

import de.bsvrz.sys.funclib.datenkatalog.bind.AttributlistenDefinition;

import java.text.MessageFormat;
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
        return MessageFormat.format("Status'{'erfassung={0}, plFormal={1}, messWertErsetzung={2}'}'",
                erfassung, plFormal, messWertErsetzung);
    }

}
