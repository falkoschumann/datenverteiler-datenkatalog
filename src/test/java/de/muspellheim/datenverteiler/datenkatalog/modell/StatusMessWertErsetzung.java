/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.modell;

import de.muspellheim.datenverteiler.datenkatalog.bind.AttributlistenDefinition;

import java.util.Objects;

@AttributlistenDefinition
public class StatusMessWertErsetzung {

    private boolean implausibel;
    private boolean interpoliert;

    public boolean isImplausibel() {
        return implausibel;
    }

    public void setImplausibel(boolean implausibel) {
        this.implausibel = implausibel;
    }

    public boolean isInterpoliert() {
        return interpoliert;
    }

    public void setInterpoliert(boolean interpoliert) {
        this.interpoliert = interpoliert;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatusMessWertErsetzung that = (StatusMessWertErsetzung) o;
        return Objects.equals(implausibel, that.implausibel) &&
                Objects.equals(interpoliert, that.interpoliert);
    }

    @Override
    public int hashCode() {
        return Objects.hash(implausibel, interpoliert);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StatusMessWertErsetzung{");
        sb.append("implausibel=").append(implausibel);
        sb.append(", interpoliert=").append(interpoliert);
        sb.append('}');
        return sb.toString();
    }

}
