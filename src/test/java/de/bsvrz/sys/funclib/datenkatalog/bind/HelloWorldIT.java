/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.bind;

import de.bsvrz.sys.funclib.datenkatalog.AbstractDatenkatalogIT;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class HelloWorldIT extends AbstractDatenkatalogIT {

    @Test
    public void test() {
        assertNotNull(getModel().getType("typ.typ"));
    }

}
