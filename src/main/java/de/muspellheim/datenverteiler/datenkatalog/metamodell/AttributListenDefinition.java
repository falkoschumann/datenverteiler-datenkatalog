/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

/**
 * Fasst Attribute in einer Liste zusammen.
 * <p>Attributlisten definieren eine Folge von Attributen die in anderen Attributlisten oder Attributgruppen
 * referenziert werden kann. Die Attributlistendefinition dient dabei als Attributtyp des referenzierenden
 * Attributs.</p>
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class AttributListenDefinition extends AttributMenge implements AttributTyp {

    public static AttributListenDefinition erzeugeMitPid(String pid) {
        AttributListenDefinition result = new AttributListenDefinition();
        result.setPid(pid);
        return result;
    }

}
