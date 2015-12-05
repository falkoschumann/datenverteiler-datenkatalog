/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.muspellheim.datenverteiler.datenkatalog.AbstractDatenkatalogIT;
import org.junit.Before;
import org.junit.Test;

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
    public void testTyp_Netz_SuperTypUndMenge() {
        Typ stoerfallIndikator = new Typ();
        stoerfallIndikator.setName("StörfallIndikator");
        stoerfallIndikator.setPid("typ.störfallIndikator");
        stoerfallIndikator.setKurzinfo("Allgemeiner Störfallindikator, wird von einer Reihe\n" +
                "\t\t\t\t\tanderer Objekttypen erweitert, selbst aber nie\n" +
                "\t\t\t\t\tdirekt instanziert.");
        stoerfallIndikator.setBeschreibung("");

        Typ netzBestandTeil = new Typ();
        netzBestandTeil.setName("NetzBestandTeil");
        netzBestandTeil.setPid("typ.netzBestandTeil");
        netzBestandTeil.setKurzinfo("Ein Netz besteht aus NetzBestandTeilen, also aus\n" +
                "\t\t\t\t\tÄußerenStraßenSegmenten und bereits definierten\n" +
                "\t\t\t\t\tNetzen.");
        netzBestandTeil.setBeschreibung("");
        netzBestandTeil.getSuperTypen().add(stoerfallIndikator);

        MengenVerwendung netzBestandTeile = new MengenVerwendung();
        netzBestandTeile.setName("");
        netzBestandTeile.setPid("");
        netzBestandTeile.setKurzinfo("Netzbestandteile, aus denen das Netz besteht.");
        netzBestandTeile.setBeschreibung("");
        netzBestandTeile.setMengenName("NetzBestandTeile");
        netzBestandTeile.getObjektTypen().add(netzBestandTeil);

        Typ netz = new Typ();
        netz.setName("Netz");
        netz.setPid("typ.netz");
        netz.setKurzinfo("Ein Netz besteht aus NetzBestandTeilen, also aus\n" +
                "\t\t\t\t\tÄußerenStraßenSegmenten und bereits definierten\n" +
                "\t\t\t\t\tNetzen.");
        netz.setBeschreibung("");
        netz.getSuperTypen().add(netzBestandTeil);
        netz.getMengen().add(netzBestandTeile);

        Typ actual = metamodell.getTyp("typ.netz");

        assertEquals(netz, actual);
    }

    @Test
    public void testTyp_Stau_SuperTypUndDynamischerTyp() {
        Typ situation = new Typ();
        situation.setName("Situation");
        situation.setPid("typ.situation");
        situation.setKurzinfo("Dynamisch erzeugtes Situationsobjekt.");
        situation.setBeschreibung("");
        situation.setDynamisch(true);

        Typ stau = new Typ();
        stau.setName("Stau");
        stau.setPid("typ.stau");
        stau.setKurzinfo("Dynamisch erzeugtes Stauobjekt.");
        stau.setBeschreibung("");
        stau.setDynamisch(true);
        stau.getSuperTypen().add(situation);

        Typ actual = metamodell.getTyp("typ.stau");

        assertEquals(stau, actual);
    }

    @Test
    public void testGetKonfigurationsbereiche() {
        Set<KonfigurationsBereich> konfigurationsbereiche = metamodell.getKonfigurationsbereiche();
        assertFalse(konfigurationsbereiche.isEmpty());
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

}
