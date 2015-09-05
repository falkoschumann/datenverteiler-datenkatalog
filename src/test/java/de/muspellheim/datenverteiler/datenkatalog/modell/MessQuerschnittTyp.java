/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.modell;

import de.muspellheim.datenverteiler.datenkatalog.bind.Zustand;

@Zustand
public enum MessQuerschnittTyp {

    SonstigeFahrbahn,
    HauptFahrbahn,
    NebenFahrbahn,
    Einfahrt,
    Ausfahrt

}
