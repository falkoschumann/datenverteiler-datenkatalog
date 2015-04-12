/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.bind;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.sys.funclib.datenkatalog.AbstractDatenkatalogIT;
import de.bsvrz.sys.funclib.datenkatalog.modell.GueteVerfahren;
import de.bsvrz.sys.funclib.datenkatalog.modell.MessQuerschnittAllgemein;
import de.bsvrz.sys.funclib.datenkatalog.modell.MessQuerschnittTyp;
import de.bsvrz.sys.funclib.datenkatalog.modell.UfdsHelligkeit;
import org.junit.Before;
import org.junit.Test;

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
    public void testAufzaehlungstypUndObjektreferenz() throws Exception {
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
    public void testRelativerZeitstempelUndAttributlisteUndGanzzahl32BitUndJaNeinUndFestkommzahlUndAufzaehlungstyp() throws Exception {
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

}
