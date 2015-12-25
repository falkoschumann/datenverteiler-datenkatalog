/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unittests für Systemobjekte.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class SystemObjektTest {

    @Test
    public void testGetNameOderPid() {
        Systemobjekt nameUndPid = new TestingSystemObjekt();
        nameUndPid.setName("Foo");
        nameUndPid.setPid("obj.foo");
        assertEquals("Foo", nameUndPid.getNameOderPid());

        Systemobjekt name = new TestingSystemObjekt();
        name.setName("Foo");
        assertEquals("Foo", name.getNameOderPid());

        Systemobjekt pid = new TestingSystemObjekt();
        pid.setPid("obj.foo");
        assertEquals("obj.foo", pid.getNameOderPid());

        Systemobjekt nichts = new TestingSystemObjekt();
        assertEquals("", nichts.getNameOderPid());
    }

    @Test
    public void testToString() {
        Systemobjekt nameUndPid = new TestingSystemObjekt();
        nameUndPid.setName("Foo");
        nameUndPid.setPid("obj.foo");
        assertEquals("TestingSystemObjekt Foo (obj.foo)", nameUndPid.toString());

        Systemobjekt name = new TestingSystemObjekt();
        name.setName("Foo");
        assertEquals("TestingSystemObjekt Foo", name.toString());

        Systemobjekt pid = new TestingSystemObjekt();
        pid.setPid("obj.foo");
        assertEquals("TestingSystemObjekt (obj.foo)", pid.toString());

        Systemobjekt nichts = new TestingSystemObjekt();
        assertEquals("TestingSystemObjekt", nichts.toString());
    }

    @Test
    public void testCompareToNameOderPid() {
        Typ mqTyp = Typ.erzeugeMitPid("typ.messQuerschnitt");
        mqTyp.setName("MessQuerschnitt");
        Attributgruppe mqAtg = Attributgruppe.erzeugeMitPid("atg.messQuerschnitt");
        mqAtg.setName("MessQuerschnitt");
        Typ fsTyp = Typ.erzeugeMitPid("typ.fahrStreifen");
        fsTyp.setName("FahrStreifen");

        assertTrue(Systemobjekt.compareToNameOderPid(mqTyp, mqTyp) == 0);
        assertTrue(Systemobjekt.compareToNameOderPid(mqTyp, mqAtg) > 0);
        assertTrue(Systemobjekt.compareToNameOderPid(fsTyp, mqTyp) < 0);
    }

    private class TestingSystemObjekt extends Systemobjekt {
    }

}
