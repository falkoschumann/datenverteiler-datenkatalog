/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.bind;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.sys.funclib.datenkatalog.AbstractDatenkatalogIT;
import de.bsvrz.sys.funclib.datenkatalog.modell.MessQuerschnittAllgemein;
import de.bsvrz.sys.funclib.datenkatalog.modell.MessQuerschnittTyp;
import org.junit.Before;
import org.junit.Test;

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
    public void testEnumerationUndObjektreferenz() throws Exception {
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

}
