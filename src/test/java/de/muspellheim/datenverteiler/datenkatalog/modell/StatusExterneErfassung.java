/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.modell;

import de.muspellheim.datenverteiler.datenkatalog.bind.AttributlistenDefinition;

import java.util.Objects;

@AttributlistenDefinition
public class StatusExterneErfassung {

    private boolean nichtErfasst;

    public boolean isNichtErfasst() {
        return nichtErfasst;
    }

    public void setNichtErfasst(boolean nichtErfasst) {
        this.nichtErfasst = nichtErfasst;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatusExterneErfassung that = (StatusExterneErfassung) o;
        return Objects.equals(nichtErfasst, that.nichtErfasst);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nichtErfasst);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StatusExterneErfassung{");
        sb.append("nichtErfasst=").append(nichtErfasst);
        sb.append('}');
        return sb.toString();
    }

}
