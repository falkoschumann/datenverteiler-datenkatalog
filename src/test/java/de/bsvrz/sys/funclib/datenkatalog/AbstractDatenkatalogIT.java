/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog;

import de.bsvrz.dav.daf.communication.dataRepresentation.AttributeBaseValue;
import de.bsvrz.dav.daf.communication.dataRepresentation.AttributeBaseValueDataFactory;
import de.bsvrz.dav.daf.communication.dataRepresentation.AttributeHelper;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.puk.config.configFile.datamodel.ConfigDataModel;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.File;
import java.util.List;

public abstract class AbstractDatenkatalogIT {

    private static ConfigDataModel model;

    @BeforeClass
    public static void setUpBeforeClass() {
        model = new ConfigDataModel(new File("src/test/konfiguration/verwaltungsdaten.xml"));
    }

    @AfterClass
    public static void tearDownAfterClass() {
        model.close();
    }

    protected static Data createData(AttributeGroup atg) {
        List<AttributeBaseValue> attributesValues = AttributeHelper.getAttributesValues(atg);
        return AttributeBaseValueDataFactory.createAdapter(atg, attributesValues);
    }

    protected DataModel getModel() {
        return model;
    }

}
