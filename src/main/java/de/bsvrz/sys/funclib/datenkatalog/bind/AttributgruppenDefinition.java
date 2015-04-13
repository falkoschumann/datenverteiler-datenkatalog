/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.bind;

import java.lang.annotation.*;

/**
 * Die Annotation kennzeichnet eine Klasse als Datum einer Attributgruppe. Die Namen der Properties müssen exakt den
 * Namen der Attribute entsprechen.
 *
 * @author Falko Schumann
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AttributgruppenDefinition {

    // tagging only

}
