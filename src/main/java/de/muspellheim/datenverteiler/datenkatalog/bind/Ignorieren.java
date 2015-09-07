/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.bind;

import java.lang.annotation.*;

/**
 * Die Annotation kennzeichnet eine Property, die beim Binding an Attribute ignoriert werden soll. Die Annotation muss
 * am Getter der zu ignorierenden Property notiert werden.
 *
 * @author Falko Schumann
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Ignorieren {

    // tagging only

}
