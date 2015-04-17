/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.bind;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.sys.funclib.datenkatalog.AbstractDatenkatalogIT;
import de.bsvrz.sys.funclib.datenkatalog.modell.MessQuerschnittAllgemein;
import de.bsvrz.sys.funclib.datenkatalog.modell.MessQuerschnittTyp;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class CodeBeispieleInDokumentationIT extends AbstractDatenkatalogIT {

    @Test
    public void testUsage_Marshalling() {
        //DataModel model = ...
        DataModel model = getModel();
        Context context = new Context(model);

        MessQuerschnittAllgemein datum = new MessQuerschnittAllgemein();
        datum.setTyp(MessQuerschnittTyp.HauptFahrbahn);
        datum.setErsatzMessQuerschnitt(model.getObject("mq.a10.0000"));
        Marshaller marshaller = context.createMarshaller();
        Data data = marshaller.marshal(datum);

        assertNotNull(data);
    }

    @Test
    public void testUsage_Unmarshalling() {
        //DataModel model = ...
        DataModel model = getModel();
        Context context = new Context(model);

        AttributeGroup atg = model.getAttributeGroup("atg.messQuerschnittAllgemein");
        Data data = model.getObject("mq.a10.0000").getConfigurationData(atg);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        MessQuerschnittAllgemein datum = unmarshaller.unmarshal(data, MessQuerschnittAllgemein.class);

        assertNotNull(datum);
    }

}
