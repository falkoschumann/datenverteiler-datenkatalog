/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog;

import de.bsvrz.dav.daf.main.Data;
import org.hamcrest.Matcher;

public final class DatenkatalogMatchers {

    private DatenkatalogMatchers() {
        // utility class
    }

    public static Matcher<Data> dataEqualsTo(Data operand) {
        return IsDataEqual.dataEqualsTo(operand);
    }

}
