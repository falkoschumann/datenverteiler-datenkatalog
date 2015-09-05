/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.bind;

import java.lang.annotation.*;

/**
 * Die Annotation kennzeichnet eine Klasse als Attributliste. Die Namen der Properties müssen exakt den
 * Namen der Attribute entsprechen.
 *
 * @author Falko Schumann
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AttributlistenDefinition {

    // tagging only

}
