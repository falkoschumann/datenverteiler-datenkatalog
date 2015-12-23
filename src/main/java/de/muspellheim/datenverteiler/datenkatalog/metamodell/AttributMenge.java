/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Zusammenfassung von Attributen. Wird bei Attributgruppen und Attributlisten benötigt.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public abstract class AttributMenge extends SystemObjekt {

    private final Set<Attribut> attribute = new LinkedHashSet<>();

    /**
     * Menge der Attribute.
     */
    public Set<Attribut> getAttribute() {
        return attribute;
    }

}
