/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Fasst Attribute in einer Liste zusammen.
 * <p>Attributlisten definieren eine Folge von Attributen die in anderen Attributlisten oder Attributgruppen
 * referenziert werden kann. Die Attributlistendefinition dient dabei als Attributtyp des referenzierenden
 * Attributs.</p>
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class AttributListenDefinition extends AttributTyp implements AttributMenge {

    private final Set<Attribut> attribute = new LinkedHashSet<>();

    public static AttributListenDefinition erzeugeMitPid(String pid) {
        AttributListenDefinition result = new AttributListenDefinition();
        result.setPid(pid);
        return result;
    }

    /**
     * Menge der Attribute.
     */
    public Set<Attribut> getAttribute() {
        return attribute;
    }


}
