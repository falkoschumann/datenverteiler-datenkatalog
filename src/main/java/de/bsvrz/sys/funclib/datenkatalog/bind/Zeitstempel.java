/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.bind;

import java.lang.annotation.*;

/**
 * Die Annotation kennzeichnet eine Property als Zeitstempelattribut.
 * <p>Die Annotaton ist optional, muss aber verwendet werden, wenn ein Zeitstempel als {@code long} statt als
 * {@code Date} deklariert ist, um das Zeitstempelattribut von einem Ganzzahlattribut unterscheiden zu können.</p>
 * <p>Wird die Annotation verwendet, muss sie am Getter notiert werden.</p>
 *
 * @author Falko Schumann
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Zeitstempel {

    /**
     * Die Genauigkeit des Zeitstempels. Wird der Zeitstempel als {@code long} deklariert, gibt die Genauigkeit die
     * Maßeinheit des Zeitstempels an.
     *
     * @return die Zeitstempelgenauigkeit.
     */
    Genauigkeit genauigkeit() default Genauigkeit.MILLISEKUNDEN;

    /**
     * Definiert die Genauigkeit des Zeitstempels.
     *
     * @author Falko Schumann
     * @since 1.0
     */
    enum Genauigkeit {

        SEKUNDEN,
        MILLISEKUNDEN

    }

}
