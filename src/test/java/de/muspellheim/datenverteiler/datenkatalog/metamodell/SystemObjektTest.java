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
        SystemObjekt nameUndPid = new TestingSystemObjekt();
        nameUndPid.setName("Foo");
        nameUndPid.setPid("obj.foo");
        assertEquals("Foo", nameUndPid.getNameOderPid());

        SystemObjekt name = new TestingSystemObjekt();
        name.setName("Foo");
        assertEquals("Foo", name.getNameOderPid());

        SystemObjekt pid = new TestingSystemObjekt();
        pid.setPid("obj.foo");
        assertEquals("obj.foo", pid.getNameOderPid());

        SystemObjekt nichts = new TestingSystemObjekt();
        assertEquals("", nichts.getNameOderPid());
    }

    @Test
    public void testToString() {
        SystemObjekt nameUndPid = new TestingSystemObjekt();
        nameUndPid.setName("Foo");
        nameUndPid.setPid("obj.foo");
        assertEquals("TestingSystemObjekt Foo (obj.foo)", nameUndPid.toString());

        SystemObjekt name = new TestingSystemObjekt();
        name.setName("Foo");
        assertEquals("TestingSystemObjekt Foo", name.toString());

        SystemObjekt pid = new TestingSystemObjekt();
        pid.setPid("obj.foo");
        assertEquals("TestingSystemObjekt (obj.foo)", pid.toString());

        SystemObjekt nichts = new TestingSystemObjekt();
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

        assertTrue(SystemObjekt.compareToNameOderPid(mqTyp, mqTyp) == 0);
        assertTrue(SystemObjekt.compareToNameOderPid(mqTyp, mqAtg) > 0);
        assertTrue(SystemObjekt.compareToNameOderPid(fsTyp, mqTyp) < 0);
    }

    private class TestingSystemObjekt extends SystemObjekt {
    }

}
