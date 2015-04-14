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

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static de.bsvrz.sys.funclib.datenkatalog.IsDataEqual.dataEqualsTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class BindingIT extends AbstractDatenkatalogIT {

    private Marshaller marshaller;
    private Unmarshaller unmarshaller;

    @Before
    public void setUp() {
        Context context = new Context();
        marshaller = context.createMarshaller();
        unmarshaller = context.createUnmarshaller();
    }

    @Test
    public void testBinding_Aufzaehlungstyp_Objektreferenz() {
        AttributeGroup atg = getModel().getAttributeGroup("atg.messQuerschnittAllgemein");
        Data data = createData(atg);
        data.getUnscaledValue("Typ").setText("HauptFahrbahn");
        data.getReferenceValue("ErsatzMessQuerschnitt").setSystemObjectPid("mq.a10.0000");

        MessQuerschnittAllgemein datum = new MessQuerschnittAllgemein();
        datum.setTyp(MessQuerschnittTyp.HauptFahrbahn);
        datum.setErsatzMessQuerschnitt(getModel().getObject("mq.a10.0000"));

        Data actualData = createData(atg);
        marshaller.marshal(datum, actualData);
        assertThat(actualData, is(dataEqualsTo(data)));

        MessQuerschnittAllgemein actualDatum = unmarshaller.unmarshal(data, MessQuerschnittAllgemein.class);
        assertEquals(datum, actualDatum);
    }

    @Test
    public void testBinding_RelativerZeitstempel_Attributliste_Ganzzahl32Bit_JaNein_Festkommzahl_Aufzaehlungstyp() {
        AttributeGroup atg = getModel().getAttributeGroup("atg.ufdsHelligkeit");
        Data data = createData(atg);
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

        Data actualData = createData(atg);
        marshaller.marshal(datum, actualData);
        assertThat(actualData, is(dataEqualsTo(data)));

        UfdsHelligkeit actualDatum = unmarshaller.unmarshal(data, UfdsHelligkeit.class);
        assertEquals(datum, actualDatum);
    }

    @Test
    public void testBinding_Ganzzahl64Bit_Kommazahl_Zeichenkette() {
        AttributeGroup atg = getModel().getAttributeGroup("atg.werteBereichsEigenschaften");
        Data data = createData(atg);
        data.getUnscaledValue("minimum").set(0);
        data.getUnscaledValue("maximum").set(1000000);
        data.getScaledValue("skalierung").set(0.001);
        data.getTextValue("einheit").setText("km");

        WerteBereichsEigenschaften datum = new WerteBereichsEigenschaften();
        datum.setMinimum(0);
        datum.setMaximum(1000000);
        datum.setSkalierung(0.001);
        datum.setEinheit("km");

        Data actualData = createData(atg);
        marshaller.marshal(datum, actualData);
        assertThat(actualData, is(dataEqualsTo(data)));

        WerteBereichsEigenschaften actualDatum = unmarshaller.unmarshal(data, WerteBereichsEigenschaften.class);
        assertEquals(datum, actualDatum);
    }

    @Test
    public void testBinding_Ganzzahl8Bit_Ganzzahl32Bit_Ganzzahl64Bit() {
        AttributeGroup atg = getModel().getAttributeGroup("atg.straßenTeilSegment");
        Data data = createData(atg);
        data.getUnscaledValue("Länge").set(5000);
        data.getUnscaledValue("AnzahlFahrStreifen").set(3);
        data.getUnscaledValue("SteigungGefälle").set(10);

        StraßenTeilSegment datum = new StraßenTeilSegment();
        datum.setLaenge(5000);
        datum.setAnzahlFahrStreifen((byte) 3);
        datum.setSteigungGefaelle((short) 10);

        Data actualData = createData(atg);
        marshaller.marshal(datum, actualData);
        assertThat(actualData, is(dataEqualsTo(data)));

        StraßenTeilSegment actualDatum = unmarshaller.unmarshal(data, StraßenTeilSegment.class);
        assertEquals(datum, actualDatum);
    }

    @Test
    public void testBinding_Ganzzahl64Bit_Ganzzahl32Bit_Ganzzahl16Bit_RelativerZeitstempel_AbsoluterZeitstempel_Attributliste_Attributfeld() {
        AttributeGroup atg = getModel().getAttributeGroup("atg.stauVerlauf");
        Data data = createData(atg);
        data.getTimeValue("Schrittweite").setMillis(TimeUnit.MINUTES.toMillis(20));
        data.getTimeValue("Dauer").setMillis(TimeUnit.MINUTES.toMillis(60));
        Calendar cal = Calendar.getInstance();
        cal.set(2015, 04, 14, 21, 20, 00);
        Date aufloesungsZeit = cal.getTime();
        data.getTimeValue("AuflösungsZeit").setMillis(aufloesungsZeit.getTime());
        data.getUnscaledValue("MaxLänge").set(12000);
        cal.set(2015, 04, 14, 21, 45, 30);
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
        datum.setDauer(TimeUnit.MINUTES.toMillis(60));
        datum.setAufloesungsZeit(aufloesungsZeit);
        datum.setMaxLaenge(12000);
        datum.setMaxLaengeZeit(maxLaengeZeit);
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

        Data actualData = createData(atg);
        marshaller.marshal(datum, actualData);
        assertThat(actualData, is(dataEqualsTo(data)));

        StauVerlauf actualDatum = unmarshaller.unmarshal(data, StauVerlauf.class);
        assertEquals(datum, actualDatum);
    }

//    @Test
//    public void testBinding() {
//        // XXX u.a. Attributfeld mit Attribut
//        AttributeGroup atg = getModel().getAttributeGroup("atg.linienKoordinaten");
//        Data data = createData(atg);
//
//        LinienKoordinaten datum = new LinienKoordinaten();
//
//        Data actualData = createData(atg);
//        marshaller.marshal(datum, actualData);
//        assertThat(actualData, is(dataEqualsTo(data)));
//
//        LinienKoordinaten actualDatum = unmarshaller.unmarshal(data, LinienKoordinaten.class);
//        assertEquals(datum, actualDatum);
//    }

    // TODO Sonderfall: Objekte statt primitiven Datentypen (Integer vs. int)
    // TODO Sonderfall: Nicht alle Attribut als Property abgebildet
    // TODO Sonderfall: Nicht alle Properties als Attribut vorhanden

}
