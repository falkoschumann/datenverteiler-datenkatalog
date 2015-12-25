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
        Set<Konfigurationsverantwortlicher> konfigurationsverantwortliche = metamodell.getKonfigurationsverantwortliche();
        assertFalse(konfigurationsverantwortliche.isEmpty());
    }

    @Test
    public void testGetKonfigurationsbereiche() {
        Set<Konfigurationsbereich> konfigurationsbereiche = metamodell.getKonfigurationsbereiche();
        assertFalse(konfigurationsbereiche.isEmpty());
    }

    @Test
    public void testKonfigurationsbereich() {
        Konfigurationsbereich verkehr = metamodell.getKonfigurationsbereich("kb.tmVerkehrGlobal");

        assertFalse(verkehr.getModell().isEmpty());
        Konfigurationsverantwortlicher inovat = metamodell.getKonfigurationsverantwortlicher("kv.inovat");
        assertEquals(inovat, verkehr.getZustaendiger());
    }

    @Test
    public void testZustaender() {
        Konfigurationsverantwortlicher kv = Konfigurationsverantwortlicher.erzeugeMitPid("kv.inovat");

        Konfigurationsbereich kb = metamodell.getKonfigurationsbereich("kb.tmVerkehrGlobal");

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

        assertEquals(Konfigurationsbereich.erzeugeMitPid("kb.metaModellGlobal"), typ.getKonfigurationsbereich());
    }

    @Test
    public void testSupertypen() {
        Typ konfigurationsObjekt = Typ.erzeugeMitPid("typ.konfigurationsObjekt");
        Typ stoerfallIndikator = Typ.erzeugeMitPid("typ.störfallIndikator");
        Typ netzBestandTeil = Typ.erzeugeMitPid("typ.netzBestandTeil");

        Typ netz = metamodell.getTyp("typ.netz");

        assertEquals(Collections.singleton(netzBestandTeil), netz.getSupertypen());
        assertEquals(Collections.singleton(stoerfallIndikator), netz.getSupertypen().toArray(new Typ[0])[0].getSupertypen());
        assertEquals(Collections.singleton(konfigurationsObjekt), netz.getSupertypen().toArray(new Typ[0])[0].getSupertypen().toArray(new Typ[0])[0].getSupertypen());
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

        assertEquals(subtypen, result.getSubtypen());
    }

    @Test
    public void testMengenTyp() {
        Mengentyp typ = metamodell.getMengentyp("menge.routen");

        assertEquals(Mengentyp.erzeugeMitPid("menge.routen"), typ);
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
        expected.getAttribute().add(Attribut.erzeuge("minimum", 1, GanzzahlAttributtyp.erzeugeMitPid("att.zahl")));
        expected.getAttribute().add(Attribut.erzeuge("maximum", 2, GanzzahlAttributtyp.erzeugeMitPid("att.zahl")));
        expected.getAttribute().add(Attribut.erzeuge("skalierung", 3, KommazahlAttributtyp.erzeugeMitPid("att.faktor")));
        expected.getAttribute().add(Attribut.erzeuge("einheit", 4, ZeichenkettenAttributtyp.erzeugeMitPid("att.einheit")));

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
        Set<Attributgruppenverwendung> attributgruppenverwendungen = new LinkedHashSet<>();
        attributgruppenverwendungen.add(Attributgruppenverwendung.erzeugeMitPid("atgv.atg.verkehrsDatenKurzZeitIntervall.asp.externeErfassung"));
        attributgruppenverwendungen.add(Attributgruppenverwendung.erzeugeMitPid("atgv.atg.verkehrsDatenKurzZeitIntervall.asp.messWertErsetzung"));
        attributgruppenverwendungen.add(Attributgruppenverwendung.erzeugeMitPid("atgv.atg.verkehrsDatenKurzZeitIntervall.asp.plausibilitätsPrüfungFormal"));
        attributgruppenverwendungen.add(Attributgruppenverwendung.erzeugeMitPid("atgv.atg.verkehrsDatenKurzZeitIntervall.asp.plausibilitätsPrüfungLogisch"));

        Attributgruppe attributgruppe = metamodell.getAttributgruppe("atg.verkehrsDatenKurzZeitIntervall");

        assertEquals(aspekte, attributgruppe.getAspekte());
        assertEquals(attributgruppenverwendungen, attributgruppe.getAttributgruppenverwendungen());
    }

    @Test
    public void testZeichenkettenAttributTyp() {
        ZeichenkettenAttributtyp attributtyp = (ZeichenkettenAttributtyp) metamodell.getAttributtyp("att.einheit");

        assertEquals(64, attributtyp.getLaenge());
        assertEquals(Zeichenkodierung.ISO_8859_1, attributtyp.getKodierung());
    }

    @Test
    public void testZeitstempelAttributTyp() {
        ZeitstempelAttributtyp attributtyp = (ZeitstempelAttributtyp) metamodell.getAttributtyp("att.zeitDauer");

        assertEquals(true, attributtyp.isRelativ());
        assertEquals(Zeitaufloesung.MILLISEKUNDEN, attributtyp.getGenauigkeit());
    }

    @Test
    public void testKommazahlAttributTyp() {
        KommazahlAttributtyp attributtyp = (KommazahlAttributtyp) metamodell.getAttributtyp("att.faktor");

        assertEquals("", attributtyp.getEinheit());
        assertEquals(Fliesskommaaufloesung.DOUBLE, attributtyp.getGenauigkeit());
    }

    @Test
    public void testObjektReferenzAttributTyp() {
        ObjektreferenzAttributtyp attributtyp = (ObjektreferenzAttributtyp) metamodell.getAttributtyp("att.typReferenz");

        assertEquals(false, attributtyp.isUndefiniertErlaubt());
        assertEquals(Referenzierungsart.AGGREGATION, attributtyp.getReferenzierungsart());
    }

    @Test
    public void testGanzzahlAttributTyp() {
        GanzzahlAttributtyp attributtyp = (GanzzahlAttributtyp) metamodell.getAttributtyp("att.geschwindigkeit");

        assertEquals(Datentypgroesse.SHORT, attributtyp.getAnzahlBytes());
        assertEquals(Wertebereich.erzeuge(0, 254, 1, "km/h"), attributtyp.getBereich());
    }

    @Test
    public void testGanzzahlAttributTypMitZustaenden() {
        GanzzahlAttributtyp attributtyp = (GanzzahlAttributtyp) metamodell.getAttributtyp("att.zeitAufloesung");

        Set<Wertezustand> expected = new LinkedHashSet<>();
        expected.add(Wertezustand.erzeuge("Sekunden", 0));
        expected.add(Wertezustand.erzeuge("Millisekunden", 1));
        assertEquals(expected, attributtyp.getZustaende());
    }

    @Test
    public void testMengen() {
        Mengenverwendung aktionen = Mengenverwendung.erzeuge("Aktionen", Mengentyp.erzeugeMitPid("menge.aktionen"));
        Mengenverwendung baustellen = Mengenverwendung.erzeuge("Baustellen", Mengentyp.erzeugeMitPid("menge.baustellen"));
        Mengenverwendung seitenStreifenFreigaben = Mengenverwendung.erzeuge("SeitenStreifenFreigaben", Mengentyp.erzeugeMitPid("menge.seitenStreifenFreigaben"), false);
        Mengenverwendung situationen = Mengenverwendung.erzeuge("Situationen", Mengentyp.erzeugeMitPid("menge.situationen"));
        Mengenverwendung staus = Mengenverwendung.erzeuge("Staus", Mengentyp.erzeugeMitPid("menge.staus"));
        Mengenverwendung unfaelle = Mengenverwendung.erzeuge("Unfälle", Mengentyp.erzeugeMitPid("menge.unfälle"));
        Set<Mengenverwendung> mengen = new LinkedHashSet<>();
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
