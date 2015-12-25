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

        assertFalse(verkehr.getModell().isEmpty());
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

        assertEquals(KonfigurationsBereich.erzeugeMitPid("kb.metaModellGlobal"), typ.getKonfigurationsBereich());
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
        assertEquals(2, typ.getMinimaleAnzahl());
        assertEquals(0, typ.getMaximaleAnzahl());
        assertEquals(false, typ.isAenderbar());
        assertEquals(Referenzierungsart.ASSOZIATION, typ.getReferenzierungsart());
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
        expected.getAttribute().add(Attribut.erzeuge("minimum", 1, GanzzahlAttributTyp.erzeugeMitPid("att.zahl")));
        expected.getAttribute().add(Attribut.erzeuge("maximum", 2, GanzzahlAttributTyp.erzeugeMitPid("att.zahl")));
        expected.getAttribute().add(Attribut.erzeuge("skalierung", 3, KommazahlAttributTyp.erzeugeMitPid("att.faktor")));
        expected.getAttribute().add(Attribut.erzeuge("einheit", 4, ZeichenkettenAttributTyp.erzeugeMitPid("att.einheit")));

        Attributgruppe attributgruppe = metamodell.getAttributgruppe("atg.werteBereichsEigenschaften");

        assertEquals(expected.getAttribute(), attributgruppe.getAttribute());
    }

    @Test
    public void testAspekt() {
        Set<Aspekt> aspekte = new LinkedHashSet<>();
        aspekte.add(Aspekt.erzeugeMitPid("asp.externeErfassung"));
        aspekte.add(Aspekt.erzeugeMitPid("asp.messWertErsetzung"));
        aspekte.add(Aspekt.erzeugeMitPid("asp.plausibilitätsPrüfungFormal"));
        aspekte.add(Aspekt.erzeugeMitPid("asp.plausibilitätsPrüfungLogisch"));
        Set<AttributgruppenVerwendung> attributgruppenVerwendungen = new LinkedHashSet<>();
        attributgruppenVerwendungen.add(AttributgruppenVerwendung.erzeugeMitPid("atgv.atg.verkehrsDatenKurzZeitIntervall.asp.externeErfassung"));
        attributgruppenVerwendungen.add(AttributgruppenVerwendung.erzeugeMitPid("atgv.atg.verkehrsDatenKurzZeitIntervall.asp.messWertErsetzung"));
        attributgruppenVerwendungen.add(AttributgruppenVerwendung.erzeugeMitPid("atgv.atg.verkehrsDatenKurzZeitIntervall.asp.plausibilitätsPrüfungFormal"));
        attributgruppenVerwendungen.add(AttributgruppenVerwendung.erzeugeMitPid("atgv.atg.verkehrsDatenKurzZeitIntervall.asp.plausibilitätsPrüfungLogisch"));

        Attributgruppe attributgruppe = metamodell.getAttributgruppe("atg.verkehrsDatenKurzZeitIntervall");

        assertEquals(aspekte, attributgruppe.getAspekte());
        assertEquals(attributgruppenVerwendungen, attributgruppe.getAttributgruppenVerwendungen());
    }

    @Test
    public void testZeichenkettenAttributTyp() {
        ZeichenkettenAttributTyp attributTyp = (ZeichenkettenAttributTyp) metamodell.getAttributTyp("att.einheit");

        assertEquals(64, attributTyp.getLaenge());
        assertEquals(ZeichenKodierung.ISO_8859_1, attributTyp.getKodierung());
    }

    @Test
    public void testZeitstempelAttributTyp() {
        ZeitstempelAttributTyp attributTyp = (ZeitstempelAttributTyp) metamodell.getAttributTyp("att.zeitDauer");

        assertEquals(true, attributTyp.isRelativ());
        assertEquals(ZeitAufloesung.MILLISEKUNDEN, attributTyp.getGenauigkeit());
    }

    @Test
    public void testKommazahlAttributTyp() {
        KommazahlAttributTyp attributTyp = (KommazahlAttributTyp) metamodell.getAttributTyp("att.faktor");

        assertEquals("", attributTyp.getEinheit());
        assertEquals(FliesskommaAufloesung.DOUBLE, attributTyp.getGenauigkeit());
    }

    @Test
    public void testObjektReferenzAttributTyp() {
        ObjektReferenzAttributTyp attributTyp = (ObjektReferenzAttributTyp) metamodell.getAttributTyp("att.typReferenz");

        assertEquals(false, attributTyp.isUndefiniertErlaubt());
        assertEquals(Referenzierungsart.AGGREGATION, attributTyp.getReferenzierungsart());
    }

    @Test
    public void testGanzzahlAttributTyp() {
        GanzzahlAttributTyp attributTyp = (GanzzahlAttributTyp) metamodell.getAttributTyp("att.geschwindigkeit");

        assertEquals(DatentypGroesse.SHORT, attributTyp.getAnzahlBytes());
        assertEquals(WerteBereich.erzeuge(0, 254, 1, "km/h"), attributTyp.getBereich());
    }

    @Test
    public void testGanzzahlAttributTypMitZustaenden() {
        GanzzahlAttributTyp attributTyp = (GanzzahlAttributTyp) metamodell.getAttributTyp("att.zeitAufloesung");

        Set<WerteZustand> expected = new LinkedHashSet<>();
        expected.add(WerteZustand.erzeuge("Sekunden", 0));
        expected.add(WerteZustand.erzeuge("Millisekunden", 1));
        assertEquals(expected, attributTyp.getZustaende());
    }

    @Test
    public void testMengen() {
        MengenVerwendung aktionen = MengenVerwendung.erzeuge("Aktionen", MengenTyp.erzeugeMitPid("menge.aktionen"));
        MengenVerwendung baustellen = MengenVerwendung.erzeuge("Baustellen", MengenTyp.erzeugeMitPid("menge.baustellen"));
        MengenVerwendung seitenStreifenFreigaben = MengenVerwendung.erzeuge("SeitenStreifenFreigaben", MengenTyp.erzeugeMitPid("menge.seitenStreifenFreigaben"), false);
        MengenVerwendung situationen = MengenVerwendung.erzeuge("Situationen", MengenTyp.erzeugeMitPid("menge.situationen"));
        MengenVerwendung staus = MengenVerwendung.erzeuge("Staus", MengenTyp.erzeugeMitPid("menge.staus"));
        MengenVerwendung unfaelle = MengenVerwendung.erzeuge("Unfälle", MengenTyp.erzeugeMitPid("menge.unfälle"));
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
