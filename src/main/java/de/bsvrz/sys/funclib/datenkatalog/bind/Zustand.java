/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.bind;

import java.lang.annotation.*;

/**
 * Die Annotation kennzeichnet ein Enum als Ganzzahlattributtyp, dass nur Zustände enthält. Die Namen der Enum-Werte
 * müssen exakt den Namen der Zustände entsprechen.
 *
 * @author Falko Schumann
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Zustand {

    // tagging only

}
