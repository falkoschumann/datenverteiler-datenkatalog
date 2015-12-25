/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import java.util.Set;

/**
 * Zusammenfassung von Attributen. Wird bei Attributgruppen und Attributlisten benötigt.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public interface Attributmenge {

    /**
     * Menge der Attribute.
     */
    Set<Attribut> getAttribute();

}
