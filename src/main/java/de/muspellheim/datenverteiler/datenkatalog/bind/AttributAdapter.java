/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.bind;

import de.bsvrz.dav.daf.main.Data;

/**
 * Adapter zwischen Property und Attribut.
 *
 * @author Falko Schumann
 * @since 1.0
 */
public interface AttributAdapter {

    /**
     * Überführt eine Property in ein Attribut.
     *
     * @param propertyValue der Wert der Property.
     * @param attribut      das {@code Data} des Attributs, in das der Propertywert geschrieben werden soll.
     * @throws DataBindingException bei einem unerwarteten Problem beim Marshalling.
     */
    void marshal(Object propertyValue, Data attribut);

    /**
     * Überführt ein Attribut in eine Property.
     *
     * @param data das {@code Data}, das den Attributwert enthält.
     * @return der dazugehörige Property-Wert.
     * @throws DataBindingException bei einem unerwarteten Problem beim Unmarshalling.
     */
    Object unmarshal(Data data);

}
