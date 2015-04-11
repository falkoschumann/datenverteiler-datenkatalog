/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.bind;

import de.bsvrz.dav.daf.main.Data;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.NoSuchElementException;

/**
 * Der Kontext für das Binding und abstrakte Fabrik für {@link Marshaller} und {@link Unmarshaller}.
 *
 * @author Falko Schumann
 * @since 1.0
 */
public class Context {

    // TODO Exception-Handling verbessern: Exception durch DataBindingException ersetzen

    static Data getAttribut(Data data, String name) {
        try {
            return data.getItem(name.substring(0, 1).toUpperCase() + name.substring(1));
        } catch (NoSuchElementException ex) {
            return data.getItem(name);
        }
    }

    /**
     * Erzeugt einen Marshaller.
     *
     * @return der Marshaller.
     */
    public Marshaller createMarshaller() {
        return new MarshallerImpl();
    }

    /**
     * Erzeugt einen Unmarshaller.
     *
     * @return der Unmarshaller.
     */
    public Unmarshaller createUnmarshaller() {
        return new UnmarshallerImpl();
    }

    private static class MarshallerImpl implements Marshaller {

        @Override
        public void marshal(Object datum, Data data) throws Exception {
            BeanInfo beanInfo = Introspector.getBeanInfo(datum.getClass());
            for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
                if (pd.getName().equals("class")) continue;

                AttributAdapter adapter = new StandardAttributAdapterFactory().createAdapter(pd);
                Data att = getAttribut(data, pd.getName());
                adapter.marshal(pd.getReadMethod().invoke(datum), att);
            }
        }

    }

    private static class UnmarshallerImpl implements Unmarshaller {

        @Override
        public <T> T unmarshal(Data data, Class<T> datumClass) throws Exception {
            T result = datumClass.newInstance();
            BeanInfo beanInfo = Introspector.getBeanInfo(datumClass);
            for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
                if (pd.getName().equals("class")) continue;

                AttributAdapter adapter = new StandardAttributAdapterFactory().createAdapter(pd);
                Data att = getAttribut(data, pd.getName());
                pd.getWriteMethod().invoke(result, adapter.unmarshal(att));
            }
            return result;
        }

    }

}
