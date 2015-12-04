/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.muspellheim.datenverteiler.datenkatalog.AbstractDatenkatalogIT;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Integrationstests für das Metamodells.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class MetamodellIT extends AbstractDatenkatalogIT {

    private Metamodell metamodell;

    @Before
    public void setUp() {
        metamodell = new Metamodell(getModel());
    }

    @Test
    public void testTyp_Netz_SuperTypUndMenge() {
        Typ stoerfallIndikator = new Typ();
        stoerfallIndikator.setName("StörfallIndikator");

        Typ netzBestandTeil = new Typ();
        netzBestandTeil.setName("NetzBestandTeil");
        netzBestandTeil.getSuperTypen().add(stoerfallIndikator);

        MengenVerwendung netzBestandTeile = new MengenVerwendung();
        netzBestandTeile.setMengenName("NetzBestandTeile");
        netzBestandTeile.getObjektTypen().add(netzBestandTeil);

        Typ netz = new Typ();
        netz.setName("Netz");
        netz.getSuperTypen().add(netzBestandTeil);
        netz.getMengen().add(netzBestandTeile);

        Typ actual = metamodell.getTyp("typ.netz");

        assertEquals(netz, actual);
    }

    @Test
    public void testTyp_Stau_SuperTypUndDynamischerTyp() {
        Typ situation = new Typ();
        situation.setName("Situation");
        situation.setDynamisch(true);

        Typ stau = new Typ();
        stau.setName("Stau");
        stau.setDynamisch(true);
        stau.getSuperTypen().add(situation);

        Typ actual = metamodell.getTyp("typ.stau");

        assertEquals(stau, actual);
    }

}
