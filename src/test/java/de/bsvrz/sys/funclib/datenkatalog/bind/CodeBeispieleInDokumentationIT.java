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

public class CodeBeispieleInDokumentationIT extends AbstractDatenkatalogIT {

    @Test
    public void testUsage() {
        //ClientDavInterface dav =...;
        //DataModel model = dav.getDataModel();
        DataModel model = getModel();
        Context context = new Context();

        // Unmarshalling
        AttributeGroup atg = model.getAttributeGroup("atg.messQuerschnittAllgemein");
        Data data = model.getObject("mq.a10.0000").getConfigurationData(atg);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        MessQuerschnittAllgemein datum = unmarshaller.unmarshal(data, MessQuerschnittAllgemein.class);

        // Marshalling
        //data = dav.createData(atg);
        data = createData(atg);
        datum = new MessQuerschnittAllgemein();
        datum.setTyp(MessQuerschnittTyp.HauptFahrbahn);
        datum.setErsatzMessQuerschnitt(model.getObject("mq.a10.0000"));
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(datum, data);
    }

}
