/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog;

import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.puk.config.configFile.datamodel.ConfigDataModel;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.File;

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

    protected DataModel getModel() {
        return model;
    }

}
