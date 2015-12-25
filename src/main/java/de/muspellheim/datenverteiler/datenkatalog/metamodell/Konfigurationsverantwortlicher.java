/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

/**
 * Objekte dieses Typs können für die Konfiguration von konfigurierenden Objekten zuständig sein. (z.B. VRZen, UZen,
 * System).
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class Konfigurationsverantwortlicher extends Systemobjekt {

    public static Konfigurationsverantwortlicher erzeugeMitPid(String pid) {
        Konfigurationsverantwortlicher result = new Konfigurationsverantwortlicher();
        result.setPid(pid);
        return result;
    }

}
