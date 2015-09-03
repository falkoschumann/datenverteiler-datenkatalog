/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.datenverteiler;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class DatenverteilerTest {

    @Test
    public void testKonvertiereZeitstempelDatenverteiler() {
        LocalDateTime zeitstempel = DatenverteilerImpl.konvertiereZeitstempelVomDatenverteilerZeitstempel(1441307793000L);

        assertEquals(LocalDateTime.of(2015, 9, 3, 21, 16, 33), zeitstempel);
    }

    @Test
    public void testKonvertiereZeitstempelNachDatenverteiler() {
        long zeitstempel = DatenverteilerImpl.konvertiereZeitstempelNachDatenverteiler(LocalDateTime.of(2015, 9, 3, 21, 16, 33));

        assertEquals(1441307793000L, zeitstempel);
    }

}
