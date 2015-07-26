/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.modell;

import de.bsvrz.sys.funclib.datenkatalog.bind.AttributlistenDefinition;

import java.text.MessageFormat;
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
        return MessageFormat.format("StatusMessWertErsetzung'{'implausibel={0}, interpoliert={1}'}'",
                implausibel, interpoliert);
    }

}
