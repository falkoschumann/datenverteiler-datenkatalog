/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.bind;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.sys.funclib.datenkatalog.AbstractDatenkatalogIT;
import de.bsvrz.sys.funclib.datenkatalog.modell.*;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static de.bsvrz.sys.funclib.datenkatalog.DatenkatalogMatchers.dataEqualsTo;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class BindingIT extends AbstractDatenkatalogIT {

    private Context context;
    private Marshaller marshaller;
    private Unmarshaller unmarshaller;

    @Before
    public void setUp() {
        context = new Context(getModel());
        marshaller = context.createMarshaller();
        unmarshaller = context.createUnmarshaller();
    }

    @Test
    public void testBinding_NullIstNull() {
        assertNull(marshaller.marshal(null));
        assertNull(unmarshaller.unmarshal(null, UfdsHelligkeit.class));
    }

    @Test
    public void testBinding_Aufzaehlungstyp_Objektreferenz() {
        AttributeGroup atg = getModel().getAttributeGroup("atg.messQuerschnittAllgemein");
        Data data = context.createData(atg);
        data.getUnscaledValue("Typ").setText("HauptFahrbahn");
        data.getReferenceValue("ErsatzMessQuerschnitt").setSystemObjectPid("mq.a10.0000");

        MessQuerschnittAllgemein datum = new MessQuerschnittAllgemein();
        datum.setTyp(MessQuerschnittTyp.HauptFahrbahn);
        datum.setErsatzMessQuerschnitt(getModel().getObject("mq.a10.0000"));

        Data actualData = marshaller.marshal(datum);
        assertThat(actualData, is(dataEqualsTo(data)));

        MessQuerschnittAllgemein actualDatum = unmarshaller.unmarshal(data, MessQuerschnittAllgemein.class);
        assertEquals(datum, actualDatum);
    }

    @Test
    public void testBinding_RelativerZeitstempel_Attributliste_Ganzzahl32Bit_JaNein_Ganzzahl32BitAlsFestkommzahl_Aufzaehlungstyp() {
        AttributeGroup atg = getModel().getAttributeGroup("atg.ufdsHelligkeit");
        Data data = context.createData(atg);
        data.getTimeValue("T").setSeconds(60);
        data.getItem("Helligkeit").getUnscaledValue("Wert").set(60000);
        data.getItem("Helligkeit").getItem("Status").getItem("Erfassung").getUnscaledValue("NichtErfasst").setText("Nein");
        data.getItem("Helligkeit").getItem("Status").getItem("PlFormal").getUnscaledValue("WertMax").setText("Ja");
        data.getItem("Helligkeit").getItem("Status").getItem("PlFormal").getUnscaledValue("WertMin").setText("Nein");
        data.getItem("Helligkeit").getItem("Status").getItem("MessWertErsetzung").getUnscaledValue("Implausibel").setText("Nein");
        data.getItem("Helligkeit").getItem("Status").getItem("MessWertErsetzung").getUnscaledValue("Interpoliert").setText("Ja");
        data.getItem("Helligkeit").getItem("Güte").getScaledValue("Index").set(0.618);
        data.getItem("Helligkeit").getItem("Güte").getUnscaledValue("Verfahren").setText("Standard");

        UfdsHelligkeit datum = new UfdsHelligkeit();
        datum.setT(TimeUnit.MINUTES.toMillis(1));
        datum.getHelligkeit().setWert(60000);
        datum.getHelligkeit().getStatus().getErfassung().setNichtErfasst(false);
        datum.getHelligkeit().getStatus().getPlFormal().setWertMax(true);
        datum.getHelligkeit().getStatus().getPlFormal().setWertMin(false);
        datum.getHelligkeit().getStatus().getMessWertErsetzung().setImplausibel(false);
        datum.getHelligkeit().getStatus().getMessWertErsetzung().setInterpoliert(true);
        datum.getHelligkeit().getGuete().setIndex(0.618);
        datum.getHelligkeit().getGuete().setVerfahren(GueteVerfahren.Standard);

        Data actualData = marshaller.marshal(datum);
        assertThat(actualData, is(dataEqualsTo(data)));

        UfdsHelligkeit actualDatum = unmarshaller.unmarshal(data, UfdsHelligkeit.class);
        assertEquals(datum, actualDatum);
    }

    @Test
    public void testBinding_Ganzzahl64Bit_Kommazahl_Zeichenkette() {
        AttributeGroup atg = getModel().getAttributeGroup("atg.werteBereichsEigenschaften");
        Data data = context.createData(atg);
        data.getUnscaledValue("minimum").set(0);
        data.getUnscaledValue("maximum").set(1000000);
        data.getScaledValue("skalierung").set(0.001);
        data.getTextValue("einheit").setText("km");

        WerteBereichsEigenschaften datum = new WerteBereichsEigenschaften();
        datum.setMinimum(0);
        datum.setMaximum(1000000);
        datum.setSkalierung(0.001);
        datum.setEinheit("km");

        Data actualData = marshaller.marshal(datum);
        assertThat(actualData, is(dataEqualsTo(data)));

        WerteBereichsEigenschaften actualDatum = unmarshaller.unmarshal(data, WerteBereichsEigenschaften.class);
        assertEquals(datum, actualDatum);
    }

    @Test
    public void testBinding_Ganzzahl8Bit_Ganzzahl32Bit_Ganzzahl64Bit() {
        AttributeGroup atg = getModel().getAttributeGroup("atg.straßenTeilSegment");
        Data data = context.createData(atg);
        data.getUnscaledValue("Länge").set(5000);
        data.getUnscaledValue("AnzahlFahrStreifen").set(3);
        data.getUnscaledValue("SteigungGefälle").set(10);

        StraßenTeilSegment datum = new StraßenTeilSegment();
        datum.setLaenge(5000);
        datum.setAnzahlFahrStreifen((byte) 3);
        datum.setSteigungGefaelle((short) 10);

        Data actualData = marshaller.marshal(datum);
        assertThat(actualData, is(dataEqualsTo(data)));

        StraßenTeilSegment actualDatum = unmarshaller.unmarshal(data, StraßenTeilSegment.class);
        assertEquals(datum, actualDatum);
    }

    @Test
    public void testBinding_Ganzzahl64Bit_Ganzzahl32Bit_Ganzzahl16Bit_RelativerZeitstempel_AbsoluterZeitstempel_Attributliste_Attributfeld() {
        AttributeGroup atg = getModel().getAttributeGroup("atg.stauVerlauf");
        Data data = context.createData(atg);
        data.getTimeValue("Schrittweite").setMillis(TimeUnit.MINUTES.toMillis(20));
        data.getTimeValue("Dauer").setMillis(TimeUnit.MINUTES.toMillis(60));
        Calendar cal = Calendar.getInstance();
        cal.set(2015, Calendar.APRIL, 14, 21, 20, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date aufloesungsZeit = cal.getTime();
        data.getTimeValue("AuflösungsZeit").setMillis(aufloesungsZeit.getTime());
        data.getUnscaledValue("MaxLänge").set(12000);
        cal.set(2015, Calendar.APRIL, 14, 21, 45, 30);
        Date maxLaengeZeit = cal.getTime();
        data.getTimeValue("MaxLängeZeit").setMillis(maxLaengeZeit.getTime());
        data.getArray("Prognoseverlauf").setLength(3);
        data.getArray("Prognoseverlauf").getItem(0).getUnscaledValue("Zufluss").set(1500);
        data.getArray("Prognoseverlauf").getItem(0).getUnscaledValue("Kapazität").set(1000);
        data.getArray("Prognoseverlauf").getItem(0).getUnscaledValue("Länge").set(4000);
        data.getArray("Prognoseverlauf").getItem(0).getTimeValue("VerlustZeit").setMillis(TimeUnit.MINUTES.toMillis(20));
        data.getArray("Prognoseverlauf").getItem(0).getUnscaledValue("vKfz").set(20);
        data.getArray("Prognoseverlauf").getItem(1).getUnscaledValue("Zufluss").set(2000);
        data.getArray("Prognoseverlauf").getItem(1).getUnscaledValue("Kapazität").set(1000);
        data.getArray("Prognoseverlauf").getItem(1).getUnscaledValue("Länge").set(10000);
        data.getArray("Prognoseverlauf").getItem(1).getTimeValue("VerlustZeit").setMillis(TimeUnit.MINUTES.toMillis(30));
        data.getArray("Prognoseverlauf").getItem(1).getUnscaledValue("vKfz").set(15);
        data.getArray("Prognoseverlauf").getItem(2).getUnscaledValue("Zufluss").set(1200);
        data.getArray("Prognoseverlauf").getItem(2).getUnscaledValue("Kapazität").set(1000);
        data.getArray("Prognoseverlauf").getItem(2).getUnscaledValue("Länge").set(8000);
        data.getArray("Prognoseverlauf").getItem(2).getTimeValue("VerlustZeit").setMillis(TimeUnit.MINUTES.toMillis(10));
        data.getArray("Prognoseverlauf").getItem(2).getUnscaledValue("vKfz").set(40);

        StauVerlauf datum = new StauVerlauf();
        datum.setSchrittweite(TimeUnit.MINUTES.toMillis(20));
        datum.setDauer(Duration.ofMinutes(60));
        datum.setAufloesungsZeit(LocalDateTime.of(2015, Month.APRIL, 14, 21, 20, 0));
        datum.setMaxLaenge(12000);
        datum.setMaxLaengeZeit(LocalDateTime.of(2015, Month.APRIL, 14, 21, 45, 30));
        StauVerlaufPrognoseSchritt schritt1 = new StauVerlaufPrognoseSchritt();
        schritt1.setZufluss(1500);
        schritt1.setKapazitaet(1000);
        schritt1.setLaenge(4000);
        schritt1.setVerlustZeit(TimeUnit.MINUTES.toMillis(20));
        schritt1.setVKfz((short) 20);
        datum.getPrognoseverlauf().add(schritt1);
        StauVerlaufPrognoseSchritt schritt2 = new StauVerlaufPrognoseSchritt();
        schritt2.setZufluss(2000);
        schritt2.setKapazitaet(1000);
        schritt2.setLaenge(10000);
        schritt2.setVerlustZeit(TimeUnit.MINUTES.toMillis(30));
        schritt2.setVKfz((short) 15);
        datum.getPrognoseverlauf().add(schritt2);
        StauVerlaufPrognoseSchritt schritt3 = new StauVerlaufPrognoseSchritt();
        schritt3.setZufluss(1200);
        schritt3.setKapazitaet(1000);
        schritt3.setLaenge(8000);
        schritt3.setVerlustZeit(TimeUnit.MINUTES.toMillis(10));
        schritt3.setVKfz((short) 40);
        datum.getPrognoseverlauf().add(schritt3);

        Data actualData = marshaller.marshal(datum);
        assertThat(actualData, is(dataEqualsTo(data)));

        StauVerlauf actualDatum = unmarshaller.unmarshal(data, StauVerlauf.class);
        assertEquals(datum, actualDatum);
    }

    @Test
    public void testBinding_Attributfeld_Ganzzahl32BitAlsFestkommazahl() {
        AttributeGroup atg = getModel().getAttributeGroup("atg.linienKoordinaten");
        Data data = context.createData(atg);
        data.getScaledArray("x").set(11.1, 22.2, 33.3);
        data.getScaledArray("y").set(4.4, 5.5, 6.6);

        LinienKoordinaten datum = new LinienKoordinaten();
        datum.setX(11.1, 22.2, 33.3);
        datum.setY(4.4, 5.5, 6.6);

        Data actualData = marshaller.marshal(datum);
        assertThat(actualData, is(dataEqualsTo(data)));

        LinienKoordinaten actualDatum = unmarshaller.unmarshal(data, LinienKoordinaten.class);
        assertEquals(datum, actualDatum);
    }

    @Test
    public void testBinding_Attributfeld_Ganzzahl32Bit_AbsoluterZeitstempel() throws ParseException {
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.GERMANY);
        AttributeGroup atg = getModel().getAttributeGroup("atg.verkehrsDatenLangZeitMSV");
        Data data = context.createData(atg);
        data.getUnscaledValue("01SpitzenStundeQKfzGesamt").set(3000);
        data.getTimeArray("01SpitzenStundeQKfzGesamtZeitPunkte").setMillis(dateFormat.parse("16.04.2015 20:00").getTime(), dateFormat.parse("16.04.2015 21:00").getTime());
        data.getUnscaledValue("30SpitzenStundeQKfzGesamt").set(4000);
        data.getTimeArray("30SpitzenStundeQKfzGesamtZeitPunkte").setMillis(dateFormat.parse("16.04.2015 22:00").getTime(), dateFormat.parse("16.04.2015 23:00").getTime());
        data.getUnscaledValue("50SpitzenStundeQKfzGesamt").set(5000);
        data.getTimeArray("50SpitzenStundeQKfzGesamtZeitPunkte").setMillis(dateFormat.parse("17.04.2015 00:00").getTime(), dateFormat.parse("17.04.2015 01:00").getTime());

        VerkehrsDatenLangZeitMSV datum = new VerkehrsDatenLangZeitMSV();
        datum.set01SpitzenStundeQKfzGesamt(3000);
        datum.set01SpitzenStundeQKfzGesamtZeitPunkte(Arrays.asList(dateFormat.parse("16.04.2015 20:00"), dateFormat.parse("16.04.2015 21:00")));
        datum.set30SpitzenStundeQKfzGesamt(4000);
        datum.set30SpitzenStundeQKfzGesamtZeitPunkte(LocalDateTime.of(2015, 4, 16, 22, 0), LocalDateTime.of(2015, 4, 16, 23, 0));
        datum.set50SpitzenStundeQKfzGesamt(5000);
        datum.set50SpitzenStundeQKfzGesamtZeitPunkte(dateFormat.parse("17.04.2015 00:00"), dateFormat.parse("17.04.2015 01:00"));

        Data actualData = marshaller.marshal(datum);
        assertThat(actualData, is(dataEqualsTo(data)));

        VerkehrsDatenLangZeitMSV actualDatum = unmarshaller.unmarshal(data, VerkehrsDatenLangZeitMSV.class);
        assertEquals(datum, actualDatum);
    }


    @Test
    public void testBinding_PropertyFuerAttributFehlt_KeinFehlerWennDefaultwerteGesetztSind() {
        AttributeGroup atg = getModel().getAttributeGroup("atg.bilanzVerkehrsStärke");
        Data data = context.createData(atg);
        data.setToDefault();
        data.getUnscaledValue("QLkw").set(1000);
        data.getUnscaledValue("QPkw").set(2000);

        BilanzVerkehrsStaerke datum = new BilanzVerkehrsStaerke();
        datum.setQLkw(1000);
        datum.setQPkw(2000);

        Data actualData = marshaller.marshal(datum);
        assertThat(actualData, is(dataEqualsTo(data)));

        BilanzVerkehrsStaerke actualDatum = unmarshaller.unmarshal(data, BilanzVerkehrsStaerke.class);
        assertEquals(datum, actualDatum);
    }

    @Test
    public void testBinding_PropertyOhneDazugehoerigesAttribut_KeinFehlerPropertyWirdIgnoriert() {
        AttributeGroup atg = getModel().getAttributeGroup("atg.achsLastMessStelle");
        Data data = context.createData(atg);
        data.getReferenceValue("AchsLastMessStellenQuelle").setSystemObjectPid("mq.a10.0000");
        data.getReferenceValue("FahrStreifen").setSystemObjectPid("fs.mq.a10.0000");

        AchsLastMessStelle datum = new AchsLastMessStelle();
        datum.setAchsLastMessStellenQuelle(getModel().getObject("mq.a10.0000"));
        datum.setFahrStreifen(getModel().getObject("fs.mq.a1.0000"));

        Data actualData = marshaller.marshal(datum);
        assertThat(actualData, is(dataEqualsTo(data)));

        AchsLastMessStelle actualDatum = unmarshaller.unmarshal(data, AchsLastMessStelle.class);
        assertEquals(datum, actualDatum);
    }

    @Test(expected = DataBindingException.class)
    public void testBindung_AttributgruppePidFalsch_Fehler() {
        Foobar foobar = new Foobar();
        foobar.setFoo(43);
        foobar.setBar("Foobar");
        marshaller.marshal(foobar); // throws exception
    }

    @Test(expected = DataBindingException.class)
    public void testBindung_AttributgruppenDefinitionNichtAngegeben_Fehler() {
        JPanel panel = new JPanel();
        marshaller.marshal(panel); // throws exception
    }

}
