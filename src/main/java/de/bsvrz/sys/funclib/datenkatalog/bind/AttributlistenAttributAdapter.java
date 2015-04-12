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

class AttributlistenAttributAdapter implements AttributAdapter {

    private final Class<?> datumClass;

    AttributlistenAttributAdapter(Class<?> datumClass) {
        this.datumClass = datumClass;
    }

    private static Data getAttribut(Data data, PropertyDescriptor pd) {
        String attributname;
        if (attributDefinitionAngegeben(pd)) {
            attributname = pd.getReadMethod().getAnnotation(AttributDefinition.class).name();
        } else {
            try {
                attributname = pd.getName().substring(0, 1).toUpperCase() + pd.getName().substring(1);
            } catch (NoSuchElementException ex) {
                attributname = pd.getName();
            }
        }
        return data.getItem(attributname);
    }

    private static boolean attributDefinitionAngegeben(PropertyDescriptor pd) {
        return pd.getReadMethod().getAnnotation(AttributDefinition.class) != null && !pd.getReadMethod().getAnnotation(AttributDefinition.class).name().isEmpty();
    }

    @Override
    public void marshal(Object propertyValue, Data attribut) throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(propertyValue.getClass());
        for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
            if (pd.getName().equals("class")) continue;

            AttributAdapter adapter = new StandardAttributAdapterFactory().createAdapter(pd);
            Data att = getAttribut(attribut, pd);
            adapter.marshal(pd.getReadMethod().invoke(propertyValue), att);
        }
    }

    @Override
    public Object unmarshal(Data data) throws Exception {
        Object result = datumClass.newInstance();
        BeanInfo beanInfo = Introspector.getBeanInfo(datumClass);
        for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
            if (pd.getName().equals("class")) continue;

            // TODO Attributliste auch ohne Setter umwandeln
            AttributAdapter adapter = new StandardAttributAdapterFactory().createAdapter(pd);
            Data att = getAttribut(data, pd);
            pd.getWriteMethod().invoke(result, adapter.unmarshal(att));
        }
        return result;
    }

}
