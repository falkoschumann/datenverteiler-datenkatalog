/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.muspellheim.datenverteiler.datenkatalog.AbstractDatenkatalogIT;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
    public void testGetKonfigurationsbereiche() {
        Set<KonfigurationsBereich> konfigurationsbereiche = metamodell.getKonfigurationsbereiche();
        assertFalse(konfigurationsbereiche.isEmpty());
    }

    @Test
    public void testZustaender() {
        KonfigurationsVerantwortlicher kv = KonfigurationsVerantwortlicher.erzeugeMitPid("kv.inovat");

        KonfigurationsBereich kb = metamodell.getKonfigurationsbereich("kb.tmVerkehrGlobal");

        assertEquals(kv, kb.getZustaendiger());
    }

    @Test
    public void testNameUndPid() {
        Typ typ = metamodell.getTyp("typ.aspekt");

        assertEquals("Aspekt", typ.getName());
        assertEquals("typ.aspekt", typ.getPid());
    }

    @Test
    public void testKurzinfoUndBeschreibung() {
        Typ typ = metamodell.getTyp("typ.attributListenDefinition");

        assertEquals("Fasst Attribute in einer Liste zusammen.", typ.getKurzinfo());
        assertEquals("Attributlisten definieren eine Folge von Attributen die in anderen Attributlisten oder Attributgruppen referenziert werden kann.\n" +
                "                    Die Attributlistendefinition dient dabei als Attributtyp des referenzierenden Attributs.", typ.getBeschreibung());
    }

    @Test
    public void testBereich() {
        Typ typ = metamodell.getTyp("typ.aspekt");

        assertEquals(KonfigurationsBereich.erzeugeMitPid("kb.metaModellGlobal"), typ.getBereich());
    }

    @Test
    public void testSupertypen() {
        Typ konfigurationsObjekt = Typ.erzeugeMitPid("typ.konfigurationsObjekt");
        Typ stoerfallIndikator = Typ.erzeugeMitPid("typ.störfallIndikator");
        Typ netzBestandTeil = Typ.erzeugeMitPid("typ.netzBestandTeil");

        Typ netz = metamodell.getTyp("typ.netz");

        assertEquals(Collections.singleton(netzBestandTeil), netz.getSuperTypen());
        assertEquals(Collections.singleton(stoerfallIndikator), netz.getSuperTypen().toArray(new Typ[0])[0].getSuperTypen());
        assertEquals(Collections.singleton(konfigurationsObjekt), netz.getSuperTypen().toArray(new Typ[0])[0].getSuperTypen().toArray(new Typ[0])[0].getSuperTypen());
    }

    @Test
    public void testMengen() {
        Typ stoerfallIndikator = Typ.erzeugeMitPid("typ.störfallIndikator");
        Typ netzBestandTeil = Typ.erzeugeMitPid("typ.netzBestandTeil");
        netzBestandTeil.getSuperTypen().add(stoerfallIndikator);

        MengenVerwendung netzBestandTeile = new MengenVerwendung();
        netzBestandTeile.getObjektTypen().add(netzBestandTeil);

        Typ netz = metamodell.getTyp("typ.netz");

        assertEquals(Collections.singleton(netzBestandTeile), netz.getMengen());
    }

}
