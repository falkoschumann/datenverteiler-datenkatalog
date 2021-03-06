/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.bind;

import de.bsvrz.dav.daf.main.Data;

/**
 * Überführt ein {@code Data}, das eine Attributgruppe darstellt, in ein POJO, das ein Datum darstellt.
 *
 * @author Falko Schumann
 * @since 1.0
 */
public interface Unmarshaller {

    /**
     * Überführt eine Attributgruppe in ein Datum.
     *
     * @param attributgruppe das {@code Data} der Attributgruppe.
     * @param datumTyp       die Klasse des Datums.
     * @param <T>            der Typ des Datums.
     * @return das Datum der Attributgruppe als POJO.
     * @throws DataBindingException bei einem unerwarteten Problem beim Unmarshalling.
     */
    <T> T unmarshal(Data attributgruppe, Class<T> datumTyp);

}
