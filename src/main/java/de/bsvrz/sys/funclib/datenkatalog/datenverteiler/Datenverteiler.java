/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.datenverteiler;

import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.SystemObject;

import java.util.function.Consumer;

/**
 * Schnittstelle für die wichtigsten Operationen mit dem Datenverteiler.
 *
 * @author Falko Schumann
 * @since 1.2
 */
public interface Datenverteiler {

    void anmeldenAlsQuelle(Class<?> datumTyp, Aspect aspekt, SystemObject... objekte) throws DatenverteilerException;

    void abmeldenAlsQuelle(Class<?> datumTyp, Aspect aspekt, SystemObject... objekte);

    <T> void anmeldenAlsEmpfaenger(Consumer<T> empfaenger, Class<?> datumTyp, Aspect aspekt, SystemObject... objekte);

    <T> void abmeldenAlsEmpfaenger(Consumer<T> empfaenger, Class<?> datumTyp, Aspect aspekt, SystemObject... objekte);

    void sendeDatensatz(Datensatz<?>... datensaetze) throws DatenverteilerException;

    Aspect aspekt(String pid);

}
