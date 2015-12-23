/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

import de.muspellheim.datenverteiler.datenkatalog.AbstractDatenkatalogIT;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;

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
    public void testGetKonfigurationsverantwortlich() {
        Set<KonfigurationsVerantwortlicher> konfigurationsverantwortliche = metamodell.getKonfigurationsverantwortliche();
        assertFalse(konfigurationsverantwortliche.isEmpty());
    }

    @Test
    public void testGetKonfigurationsbereiche() {
        Set<KonfigurationsBereich> konfigurationsbereiche = metamodell.getKonfigurationsbereiche();
        assertFalse(konfigurationsbereiche.isEmpty());
    }

    @Test
    public void testKonfigurationsbereich() {
        KonfigurationsBereich verkehr = metamodell.getKonfigurationsbereich("kb.tmVerkehrGlobal");

        assertFalse(verkehr.getTypen().isEmpty());
        assertFalse(verkehr.getMengen().isEmpty());
        assertFalse(verkehr.getAttributgruppen().isEmpty());
        assertFalse(verkehr.getAttributlisten().isEmpty());
        KonfigurationsVerantwortlicher inovat = metamodell.getKonfigurationsverantwortlicher("kv.inovat");
        assertEquals(inovat, verkehr.getZustaendiger());
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
    public void testSubtypen() {
        Set<Typ> subtypen = new LinkedHashSet<>();
        subtypen.add(Typ.erzeugeMitPid("typ.baustelle"));
        subtypen.add(Typ.erzeugeMitPid("typ.stau"));
        subtypen.add(Typ.erzeugeMitPid("typ.unfall"));
        subtypen.add(Typ.erzeugeMitPid("typ.aktion"));
        subtypen.add(Typ.erzeugeMitPid("typ.seitenStreifenFreigabe"));

        Typ result = metamodell.getTyp("typ.situation");

        assertEquals(subtypen, result.getSubTypen());
    }

    @Test
    public void testMengenTyp() {
        MengenTyp typ = metamodell.getMengenTyp("menge.routen");

        assertEquals(MengenTyp.erzeugeMitPid("menge.routen"), typ);
        assertEquals(Collections.singleton(Typ.erzeugeMitPid("typ.route")), typ.getObjektTypen());
    }

    @Test
    public void testAttributgruppen() {
        Set<Attributgruppe> attributgruppen = new LinkedHashSet<>();
        attributgruppen.add(Attributgruppe.erzeugeMitPid("atg.baustellenSimulationModell"));
        attributgruppen.add(Attributgruppe.erzeugeMitPid("atg.stauBestimmungModell"));
        attributgruppen.add(Attributgruppe.erzeugeMitPid("atg.stauPrognoseModell"));

        Typ netz = metamodell.getTyp("typ.verkehrsModellNetz");

        assertEquals(attributgruppen, netz.getAttributgruppen());
    }

    @Test
    public void testAttributgruppe() {
        Attributgruppe expected = Attributgruppe.erzeugeMitPid("atg.werteBereichsEigenschaften");
        expected.getAttribute().add(Attribut.erzeugeMitNameUndTyp("minimum", AttributTyp.erzeugeMitPid("att.zahl")));
        expected.getAttribute().add(Attribut.erzeugeMitNameUndTyp("maximum", AttributTyp.erzeugeMitPid("att.zahl")));
        expected.getAttribute().add(Attribut.erzeugeMitNameUndTyp("skalierung", AttributTyp.erzeugeMitPid("att.faktor")));
        expected.getAttribute().add(Attribut.erzeugeMitNameUndTyp("einheit", AttributTyp.erzeugeMitPid("att.einheit")));

        Attributgruppe attributgruppe = metamodell.getAttributgruppe("atg.werteBereichsEigenschaften");

        assertEquals(expected.getAttribute(), attributgruppe.getAttribute());
    }

    @Test
    public void testMengen() {
        MengenVerwendung aktionen = MengenVerwendung.erzeugeMitNameUndTyp("Aktionen", MengenTyp.erzeugeMitPid("menge.aktionen"));
        MengenVerwendung baustellen = MengenVerwendung.erzeugeMitNameUndTyp("Baustellen", MengenTyp.erzeugeMitPid("menge.baustellen"));
        MengenVerwendung seitenStreifenFreigaben = MengenVerwendung.erzeugeMitNameUndTyp("SeitenStreifenFreigaben", MengenTyp.erzeugeMitPid("menge.seitenStreifenFreigaben"));
        MengenVerwendung situationen = MengenVerwendung.erzeugeMitNameUndTyp("Situationen", MengenTyp.erzeugeMitPid("menge.situationen"));
        MengenVerwendung staus = MengenVerwendung.erzeugeMitNameUndTyp("Staus", MengenTyp.erzeugeMitPid("menge.staus"));
        MengenVerwendung unfaelle = MengenVerwendung.erzeugeMitNameUndTyp("Unfälle", MengenTyp.erzeugeMitPid("menge.unfälle"));
        Set<MengenVerwendung> mengen = new LinkedHashSet<>();
        mengen.add(aktionen);
        mengen.add(baustellen);
        mengen.add(seitenStreifenFreigaben);
        mengen.add(situationen);
        mengen.add(staus);
        mengen.add(unfaelle);

        Typ netz = metamodell.getTyp("typ.verkehrsModellNetz");

        assertEquals(mengen, netz.getMengen());
    }

    @Test
    public void testDynamischesObjekt() {
        Typ stau = metamodell.getTyp("typ.stau");

        assertTrue(stau instanceof DynamischerTyp);
    }

}
