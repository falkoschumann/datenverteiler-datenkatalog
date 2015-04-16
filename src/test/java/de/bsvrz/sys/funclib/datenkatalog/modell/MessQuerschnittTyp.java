/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.modell;

import de.bsvrz.sys.funclib.datenkatalog.bind.Zustand;

@Zustand
public enum MessQuerschnittTyp {

    SonstigeFahrbahn,
    HauptFahrbahn,
    NebenFahrbahn,
    Einfahrt,
    Ausfahrt

}
