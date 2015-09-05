/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.bind;

import java.lang.annotation.*;

/**
 * Die Annotation kennzeichnet eine Property als Attributfeld.
 * <p>Die Annotation muss angegeben werden, damit der Typ der Feldelemente definiert ist. Der Typ der Property muss
 * {@code Collection} oder davon ein abgeleiteter Typ sein.</p>
 *
 * @author Falko Schumann
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AttributfeldDefinition {

    /**
     * Der Typ der Elemente des Attributfelds.
     *
     * @return der Typ der Feldelemente.
     */
    Class<?> elementtyp();

}
