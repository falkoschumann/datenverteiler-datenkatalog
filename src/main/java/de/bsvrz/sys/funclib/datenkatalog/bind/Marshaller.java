/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.bind;

import de.bsvrz.dav.daf.main.Data;

/**
 * Überführt ein POJO, das ein Datum darstellt, in ein {@code Data}, das eine Attributgruppe darstellt.
 *
 * @author Falko Schumann
 * @since 1.0
 */
public interface Marshaller {

    /**
     * Überführt ein Datum in eine Attributgruppe.
     *
     * @param datum          das Datum als POJO.
     * @param attributgruppe das {@code Data} der dazugehörigen Attributgruppe.
     */
    void marshal(Object datum, Data attributgruppe) throws Exception;

}
