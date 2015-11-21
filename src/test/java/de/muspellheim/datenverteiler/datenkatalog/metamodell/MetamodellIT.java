/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.muspellheim.datenverteiler.datenkatalog.AbstractDatenkatalogIT;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

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
    public void testTyp() {
        Typ konfigurationsObjekt = new Typ();
        konfigurationsObjekt.setName("KonfigurationsObjekt");

        Typ typ = new Typ();
        typ.setName("Typ");
        typ.getSuperTypen().add(konfigurationsObjekt);

        Typ stoerfallIndikator = new Typ();
        stoerfallIndikator.setName("StörfallIndikator");
        stoerfallIndikator.getSuperTypen().add(konfigurationsObjekt);

        Typ netzBestandTeil = new Typ();
        netzBestandTeil.setName("NetzBestandTeil");
        netzBestandTeil.getSuperTypen().add(stoerfallIndikator);

        MengenVerwendung netzBestandTeile = new MengenVerwendung();
        netzBestandTeile.setMengenName("NetzBestandTeile");
        netzBestandTeile.getObjektTypen().add(netzBestandTeil);

        Typ netz = metamodell.getTyp("typ.netz");

        assertEquals("Netz", netz.getName());
        assertEquals(Collections.singleton(netzBestandTeil), netz.getSuperTypen());
        assertEquals(Collections.singleton(netzBestandTeile), netz.getMengen());
    }

}
