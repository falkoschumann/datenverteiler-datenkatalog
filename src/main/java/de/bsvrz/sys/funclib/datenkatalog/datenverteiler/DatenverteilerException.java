/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.datenverteiler;

/**
 * Diese Ausnahme wird vom {@link Datenverteiler} geworfen wenn es ein Problem gibt.
 *
 * @author Falko Schumann
 * @since 1.2
 */
public class DatenverteilerException extends RuntimeException {

    private static final long serialVersionUID = -7113346122462304213L;

    public DatenverteilerException(String nachricht) {
        super(nachricht);
    }

    public DatenverteilerException(String nachricht, Throwable ursache) {
        super(nachricht, ursache);
    }

}
