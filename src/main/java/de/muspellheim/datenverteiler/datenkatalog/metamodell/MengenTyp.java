/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;


import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Typ der MengenDefinition.
 * <p>
 * Jede MengenDefinition wird als Objekt dieses Typs umgesetzt. Hier werden die Eigenschaften der MengenDefinitionen zusammengefasst.
 * </p>
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class MengenTyp extends Typ {

    private Set<Typ> objektTypen = new LinkedHashSet<>();

    /**
     * Enthält die möglichen Typen von Objekten, die in Mengen des jeweiligen MengenTyps verwendet werden können.
     */
    public Set<Typ> getObjektTypen() {
        return objektTypen;
    }

}
