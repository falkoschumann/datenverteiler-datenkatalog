/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.util;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.muspellheim.datenverteiler.datenkatalog.AbstractDatenkatalogIT;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class SingleObjektFabrikTest extends AbstractDatenkatalogIT {

    private SingleObjektFabrik fabrik;

    @Before
    public void setUp() {
        fabrik = new SingleObjektFabrik(getModel());
    }

    @Test
    public void testEinzigesObjektFinden() {
        SystemObject aoe = fabrik.bestimmeObjekt("typ.autarkeOrganisationsEinheit", "-aoe=");
        assertNotNull(aoe);
    }

    @Test(expected = IllegalStateException.class)
    public void testMehrereObjekteGefunden_AusnahmeWerfen() {
        fabrik.bestimmeObjekt("typ.messQuerschnittAllgemein", "-mq=");
    }

    @Test(expected = IllegalStateException.class)
    public void testKeinObjektGefunden_AusnahmeWerfen() {
        fabrik.bestimmeObjekt("typ.straße", "-mq=");
    }

    public void testObjektMitPid() {
        SystemObject mq = fabrik.bestimmeObjekt("typ.messQuerschnittAllgemein", "mq.a10.0001", "-mq=");
        assertNotNull(mq);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPidUnbekannt_AusnahmeWerfen() {
        fabrik.bestimmeObjekt("typ.messQuerschnittAllgemein", "mq.foo", "-mq=");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypUndPidPassenNichtZusammen_AusnahmeWerfen() {
        fabrik.bestimmeObjekt("typ.autarkeOrganisationsEinheit", "mq.a10.0001", "-aoe=");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypUnbekannt_AusnahmeWerfen() {
        fabrik.bestimmeObjekt("typ.foo", "-foo=");
    }

}
