/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.util;

import org.junit.Test;

public class AssertTest {

    @Test
    public void testNotNull() {
        Assert.notNull(new Object());
    }

    @Test(expected = NullPointerException.class)
    public void testNotNull_Null() {
        Assert.notNull(null);
    }

}
