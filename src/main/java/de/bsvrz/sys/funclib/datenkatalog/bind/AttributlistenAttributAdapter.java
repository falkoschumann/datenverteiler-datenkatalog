/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.bind;

import de.bsvrz.dav.daf.main.Data;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.util.NoSuchElementException;

class AttributlistenAttributAdapter implements AttributAdapter {

    private final Object datum;

    AttributlistenAttributAdapter(Class<?> datumClass) {
        datum = Pojo.create(datumClass);
    }

    public AttributlistenAttributAdapter(Object datum) {
        this.datum = datum;
    }

    private static Data getAttribut(Data data, PropertyDescriptor pd) {
        String attributname;
        if (attributDefinitionAngegeben(pd)) {
            attributname = pd.getReadMethod().getAnnotation(AttributDefinition.class).name();
        } else {
            try {
                attributname = firstToUpper(pd);
                return data.getItem(attributname);
            } catch (NoSuchElementException ex) {
                attributname = firstToLower(pd);
            }
        }
        return data.getItem(attributname);
    }

    private static String firstToUpper(PropertyDescriptor pd) {
        return pd.getName().substring(0, 1).toUpperCase() + pd.getName().substring(1);
    }

    private static String firstToLower(PropertyDescriptor pd) {
        return pd.getName().substring(0, 1).toLowerCase() + pd.getName().substring(1);
    }

    private static boolean attributDefinitionAngegeben(PropertyDescriptor pd) {
        return pd.getReadMethod().getAnnotation(AttributDefinition.class) != null && !pd.getReadMethod().getAnnotation(AttributDefinition.class).name().isEmpty();
    }

    private static boolean ignorieren(PropertyDescriptor pd) {
        return pd.getName().equals("class") || pd.getReadMethod().getAnnotation(Ignorieren.class) != null;
    }

    @Override
    public void marshal(Object propertyValue, Data attribut) {
        BeanInfo beanInfo = Pojo.getBeanInfo(propertyValue.getClass());
        for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
            if (ignorieren(pd)) continue;

            AttributAdapter adapter = new StandardAttributAdapterFactory().createAdapter(pd);
            Data att = getAttribut(attribut, pd);
            adapter.marshal(Pojo.get(propertyValue, pd), att);
        }
    }

    @Override
    public Object unmarshal(Data data) {
        BeanInfo beanInfo = Pojo.getBeanInfo(datum.getClass());
        for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
            if (ignorieren(pd)) continue;

            AttributAdapter adapter = new StandardAttributAdapterFactory().createAdapter(pd);
            Data att = getAttribut(data, pd);
            if (Pojo.isAttributliste(pd) && !Pojo.isWritable(pd)) {
                Object property = Pojo.get(datum, pd);
                new AttributlistenAttributAdapter(property).unmarshal(att);
            } else {
                Pojo.set(datum, pd, adapter.unmarshal(att));
            }
        }
        return datum;
    }

}
