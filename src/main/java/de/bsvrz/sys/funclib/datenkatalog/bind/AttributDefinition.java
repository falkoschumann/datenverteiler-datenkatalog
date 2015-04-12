/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.bind;

import java.lang.annotation.*;

/**
 * Die Annotation kennzeichnet eine Property als Attribut.
 * <p>Die Annotaton ist optional, muss aber verwendet werden, wenn der Name der Property nicht dem Namen des Attributs
 * entspricht, z.&nbsp;B. wenn der Attributname Umlaute enthält, die im Propertynamen umschrieben sind.</p>
 * <p>Wird die Annotation verwendet, muss sie am Getter notiert werden.</p>
 *
 * @author Falko Schumann
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AttributDefinition {

    /**
     * Der Name des Attributs.
     *
     * @return der Attributname.
     */
    String name() default "";

}
