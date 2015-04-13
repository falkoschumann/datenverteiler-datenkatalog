/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.bind;

final class Assert {

    private Assert() {
        // utility class
    }

    public static void notNull(Object object) {
        notNull("", object);
    }

    public static void notNull(String message, Object object) {
        if (object == null) throw new NullPointerException(message);
    }

}
