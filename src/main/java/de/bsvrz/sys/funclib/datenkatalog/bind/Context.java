/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.bind;

import de.bsvrz.dav.daf.communication.dataRepresentation.AttributeBaseValueDataFactory;
import de.bsvrz.dav.daf.communication.dataRepresentation.AttributeHelper;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;

/**
 * Der Kontext für das Binding und abstrakte Fabrik für {@link Marshaller} und {@link Unmarshaller}.
 *
 * @author Falko Schumann
 * @since 1.0
 */
public class Context {

    private final DataModel model;

    public Context(DataModel model) {
        Assert.notNull("model", model);
        this.model = model;
    }

    static Data createData(AttributeGroup atg) {
        Data result = AttributeBaseValueDataFactory.createAdapter(atg, AttributeHelper.getAttributesValues(atg));
        result.setToDefault();
        return result;
    }

    /**
     * Erzeugt einen Marshaller.
     *
     * @return der Marshaller.
     */
    public Marshaller createMarshaller() {
        return new Marshaller() {

            @Override
            public Data marshal(Object datum) {
                Assert.notNull("datum", datum);
                Data result = createData(getAttributeGroup(datum));
                new AttributlistenAttributAdapter(datum.getClass()).marshal(datum, result);
                return result;
            }

            private AttributeGroup getAttributeGroup(Object datum) {
                return model.getAttributeGroup(datum.getClass().getAnnotation(AttributgruppenDefinition.class).pid());
            }

        };
    }

    /**
     * Erzeugt einen Unmarshaller.
     *
     * @return der Unmarshaller.
     */
    public Unmarshaller createUnmarshaller() {
        return new Unmarshaller() {

            @Override
            @SuppressWarnings("unchecked")
            public <T> T unmarshal(Data data, Class<T> datumClass) {
                Assert.notNull("data", data);
                Assert.notNull("datumClass", datumClass);
                return (T) new AttributlistenAttributAdapter(datumClass).unmarshal(data);
            }

        };
    }

}
