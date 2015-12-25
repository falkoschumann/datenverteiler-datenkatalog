/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

/**
 * Aspekt, unter dem Attributgruppen verwendet werden können.
 * <p>Ein Datensatz ist immer einer Attributgruppe und einem Aspekt zugeordnet. Die Attributgruppe beschreibt den Aufbau
 * des Datensatzes und der Aspekt legt die Verwendung und damit die Bedeutung (z.B."soll" und"ist") des Datensatzes
 * fest. Welche Attributgruppen welche Aspekte verwenden können, ist als Menge bei der entsprechenden Attributgruppe
 * versorgt.</p>
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class Aspekt extends SystemObjekt {

    public static Aspekt erzeugeMitPid(String pid) {
        Aspekt result = new Aspekt();
        result.setPid(pid);
        return result;
    }

}
