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
    public void testGetKonfigurationsbereiche() {
        Set<Konfigurationsbereich> konfigurationsbereiche = metamodell.getKonfigurationsbereiche();
        assertFalse(konfigurationsbereiche.isEmpty());
    }

    @Test
    public void testKonfigurationsbereich() {
        Konfigurationsbereich verkehr = metamodell.gibKonfigurationsbereich("kb.tmVerkehrGlobal");

        assertFalse(verkehr.getModell().isEmpty());
        Konfigurationsverantwortlicher inovat = metamodell.gibKonfigurationsverantwortlicher("kv.inovat");
        assertEquals(inovat, verkehr.getZustaendiger());
    }

    @Test
    public void testZustaender() {
        Konfigurationsverantwortlicher kv = new Konfigurationsverantwortlicher("kv.inovat");

        Konfigurationsbereich kb = metamodell.gibKonfigurationsbereich("kb.tmVerkehrGlobal");

        assertEquals(kv, kb.getZustaendiger());
    }

    @Test
    public void testNameUndPid() {
        Typ typ = metamodell.gibTyp("typ.aspekt");

        assertEquals("Aspekt", typ.getName());
        assertEquals("typ.aspekt", typ.getPid());
    }

    @Test
    public void testKurzinfoUndBeschreibung() {
        Typ typ = metamodell.gibTyp("typ.attributListenDefinition");

        assertEquals("Fasst Attribute in einer Liste zusammen.", typ.getKurzinfo());
        assertEquals("Attributlisten definieren eine Folge von Attributen die in anderen Attributlisten oder Attributgruppen referenziert werden kann.\n" +
                "                    Die Attributlistendefinition dient dabei als Attributtyp des referenzierenden Attributs.", typ.getBeschreibung());
    }

    @Test
    public void testBereich() {
        Typ typ = metamodell.gibTyp("typ.aspekt");

        assertEquals(new Konfigurationsbereich("kb.metaModellGlobal"), typ.getKonfigurationsbereich());
    }

    @Test
    public void testSupertypen() {
        Typ konfigurationsObjekt = new Typ("typ.konfigurationsObjekt");
        Typ stoerfallIndikator = new Typ("typ.störfallIndikator");
        Typ netzBestandTeil = new Typ("typ.netzBestandTeil");

        Typ netz = metamodell.gibTyp("typ.netz");

        assertEquals(Collections.singleton(netzBestandTeil), netz.getSupertypen());
        assertEquals(Collections.singleton(stoerfallIndikator), netz.getSupertypen().toArray(new Typ[0])[0].getSupertypen());
        assertEquals(Collections.singleton(konfigurationsObjekt), netz.getSupertypen().toArray(new Typ[0])[0].getSupertypen().toArray(new Typ[0])[0].getSupertypen());
    }

    @Test
    public void testSubtypen() {
        Set<Typ> subtypen = new LinkedHashSet<>();
        subtypen.add(new Typ("typ.baustelle"));
        subtypen.add(new Typ("typ.stau"));
        subtypen.add(new Typ("typ.unfall"));
        subtypen.add(new Typ("typ.aktion"));
        subtypen.add(new Typ("typ.seitenStreifenFreigabe"));

        Typ result = metamodell.gibTyp("typ.situation");

        assertEquals(subtypen, result.getSubtypen());
    }

    @Test
    public void testMengenTyp() {
        Mengentyp typ = metamodell.gibMengentyp("menge.routen");

        assertEquals(new Mengentyp("menge.routen"), typ);
        assertEquals(Collections.singleton(new Typ("typ.route")), typ.getObjektTypen());
        assertEquals(2, typ.getMinimaleAnzahl());
        assertEquals(0, typ.getMaximaleAnzahl());
        assertEquals(false, typ.isAenderbar());
        assertEquals(Referenzierungsart.ASSOZIATION, typ.getReferenzierungsart());
    }

    @Test
    public void testAttributgruppen() {
        Set<Attributgruppe> attributgruppen = new LinkedHashSet<>();
        attributgruppen.add(new Attributgruppe("atg.baustellenSimulationModell"));
        attributgruppen.add(new Attributgruppe("atg.stauBestimmungModell"));
        attributgruppen.add(new Attributgruppe("atg.stauPrognoseModell"));

        Typ netz = metamodell.gibTyp("typ.verkehrsModellNetz");

        assertEquals(attributgruppen, netz.getAttributgruppen());
    }

    @Test
    public void testAttributgruppe() {
        Attributgruppe expected = new Attributgruppe("atg.werteBereichsEigenschaften");
        expected.getAttribute().add(Attribut.erzeuge("minimum", 1, new GanzzahlAttributtyp("att.zahl")));
        expected.getAttribute().add(Attribut.erzeuge("maximum", 2, new GanzzahlAttributtyp("att.zahl")));
        expected.getAttribute().add(Attribut.erzeuge("skalierung", 3, new KommazahlAttributtyp("att.faktor")));
        expected.getAttribute().add(Attribut.erzeuge("einheit", 4, new ZeichenkettenAttributtyp("att.einheit")));

        Attributgruppe attributgruppe = metamodell.gibAttributgruppe("atg.werteBereichsEigenschaften");

        assertEquals(expected.getAttribute(), attributgruppe.getAttribute());
    }

    @Test
    public void testAspekt() {
        Set<Aspekt> aspekte = new LinkedHashSet<>();
        aspekte.add(new Aspekt("asp.externeErfassung"));
        aspekte.add(new Aspekt("asp.messWertErsetzung"));
        aspekte.add(new Aspekt("asp.plausibilitätsPrüfungFormal"));
        aspekte.add(new Aspekt("asp.plausibilitätsPrüfungLogisch"));
        Set<Attributgruppenverwendung> attributgruppenverwendungen = new LinkedHashSet<>();
        attributgruppenverwendungen.add(new Attributgruppenverwendung("atgv.atg.verkehrsDatenKurzZeitIntervall.asp.externeErfassung"));
        attributgruppenverwendungen.add(new Attributgruppenverwendung("atgv.atg.verkehrsDatenKurzZeitIntervall.asp.messWertErsetzung"));
        attributgruppenverwendungen.add(new Attributgruppenverwendung("atgv.atg.verkehrsDatenKurzZeitIntervall.asp.plausibilitätsPrüfungFormal"));
        attributgruppenverwendungen.add(new Attributgruppenverwendung("atgv.atg.verkehrsDatenKurzZeitIntervall.asp.plausibilitätsPrüfungLogisch"));

        Attributgruppe attributgruppe = metamodell.gibAttributgruppe("atg.verkehrsDatenKurzZeitIntervall");

        assertEquals(aspekte, attributgruppe.getAspekte());
        assertEquals(attributgruppenverwendungen, attributgruppe.getAttributgruppenverwendungen());
    }

    @Test
    public void testZeichenkettenAttributTyp() {
        ZeichenkettenAttributtyp attributtyp = (ZeichenkettenAttributtyp) metamodell.gibAttributtyp("att.einheit");

        assertEquals(64, attributtyp.getLaenge());
        assertEquals(Zeichenkodierung.ISO_8859_1, attributtyp.getKodierung());
    }

    @Test
    public void testZeitstempelAttributTyp() {
        ZeitstempelAttributtyp attributtyp = (ZeitstempelAttributtyp) metamodell.gibAttributtyp("att.zeitDauer");

        assertEquals(true, attributtyp.isRelativ());
        assertEquals(Zeitaufloesung.MILLISEKUNDEN, attributtyp.getGenauigkeit());
    }

    @Test
    public void testKommazahlAttributTyp() {
        KommazahlAttributtyp attributtyp = (KommazahlAttributtyp) metamodell.gibAttributtyp("att.faktor");

        assertEquals("", attributtyp.getEinheit());
        assertEquals(Fliesskommaaufloesung.DOUBLE, attributtyp.getGenauigkeit());
    }

    @Test
    public void testObjektReferenzAttributTyp() {
        ObjektreferenzAttributtyp attributtyp = (ObjektreferenzAttributtyp) metamodell.gibAttributtyp("att.typReferenz");

        assertEquals(false, attributtyp.isUndefiniertErlaubt());
        assertEquals(Referenzierungsart.AGGREGATION, attributtyp.getReferenzierungsart());
    }

    @Test
    public void testGanzzahlAttributTyp() {
        GanzzahlAttributtyp attributtyp = (GanzzahlAttributtyp) metamodell.gibAttributtyp("att.geschwindigkeit");

        assertEquals(Datentypgroesse.SHORT, attributtyp.getAnzahlBytes());
        assertEquals(Wertebereich.erzeuge(0, 254, 1, "km/h"), attributtyp.getBereich());
    }

    @Test
    public void testGanzzahlAttributTypMitZustaenden() {
        GanzzahlAttributtyp attributtyp = (GanzzahlAttributtyp) metamodell.gibAttributtyp("att.zeitAufloesung");

        Set<Wertezustand> expected = new LinkedHashSet<>();
        expected.add(Wertezustand.erzeuge("Sekunden", 0));
        expected.add(Wertezustand.erzeuge("Millisekunden", 1));
        assertEquals(expected, attributtyp.getZustaende());
    }

    @Test
    public void testMengen() {
        Mengenverwendung aktionen = Mengenverwendung.erzeuge("Aktionen", new Mengentyp("menge.aktionen"));
        Mengenverwendung baustellen = Mengenverwendung.erzeuge("Baustellen", new Mengentyp("menge.baustellen"));
        Mengenverwendung seitenStreifenFreigaben = Mengenverwendung.erzeuge("SeitenStreifenFreigaben", new Mengentyp("menge.seitenStreifenFreigaben"), false);
        Mengenverwendung situationen = Mengenverwendung.erzeuge("Situationen", new Mengentyp("menge.situationen"));
        Mengenverwendung staus = Mengenverwendung.erzeuge("Staus", new Mengentyp("menge.staus"));
        Mengenverwendung unfaelle = Mengenverwendung.erzeuge("Unfälle", new Mengentyp("menge.unfälle"));
        Set<Mengenverwendung> mengen = new LinkedHashSet<>();
        mengen.add(aktionen);
        mengen.add(baustellen);
        mengen.add(seitenStreifenFreigaben);
        mengen.add(situationen);
        mengen.add(staus);
        mengen.add(unfaelle);

        Typ netz = metamodell.gibTyp("typ.verkehrsModellNetz");

        assertEquals(mengen, netz.getMengen());
    }

    @Test
    public void testDynamischesObjekt() {
        Typ stau = metamodell.gibTyp("typ.stau");

        assertTrue(stau instanceof DynamischerTyp);
    }

}
