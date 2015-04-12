/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.bind;

import de.bsvrz.dav.daf.main.Data;

/**
 * Der Kontext für das Binding und abstrakte Fabrik für {@link Marshaller} und {@link Unmarshaller}.
 *
 * @author Falko Schumann
 * @since 1.0
 */
public class Context {

    /**
     * Erzeugt einen Marshaller.
     *
     * @return der Marshaller.
     */
    public Marshaller createMarshaller() {
        return new Marshaller() {

            @Override
            public void marshal(Object datum, Data data) {
                Assert.notNull("datum", datum);
                Assert.notNull("data", data);
                new AttributlistenAttributAdapter(datum.getClass()).marshal(datum, data);
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
