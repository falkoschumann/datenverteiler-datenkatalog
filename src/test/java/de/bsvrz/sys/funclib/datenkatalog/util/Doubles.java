/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.util;

public final class Doubles {

    private Doubles() {
        // utility class
    }

    public static boolean equals(double[] a1, double[] a2, double delta) {
        if (a1 == a2)
            return true;
        if (a1 == null || a2 == null)
            return false;

        int length = a1.length;
        if (a2.length != length)
            return false;

        for (int i = 0; i < length; i++)
            if (!equals(a1[i], a2[i], delta))
                return false;

        return true;
    }

    public static boolean equals(double d1, double d2, double delta) {
        return (d1 == d2) || (Math.abs(d1 - d2) <= delta);
    }

}
