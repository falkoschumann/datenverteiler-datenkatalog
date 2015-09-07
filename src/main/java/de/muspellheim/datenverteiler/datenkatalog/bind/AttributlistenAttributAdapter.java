/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.bind;

import de.bsvrz.dav.daf.main.Data;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.Locale;
import java.util.NoSuchElementException;

class AttributlistenAttributAdapter implements AttributAdapter {

    private final Object datum;

    AttributlistenAttributAdapter(Class<?> datumClass) {
        this(Pojo.create(datumClass));
    }

    AttributlistenAttributAdapter(Object datum) {
        this.datum = datum;
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
            if (Pojo.isAttributliste(pd)) {
                Object property = Pojo.get(datum, pd);
                new AttributlistenAttributAdapter(property).unmarshal(att);
            } else if (Pojo.isAttributfeld(pd) && Pojo.isReadOnly(pd) && Collection.class.isAssignableFrom(pd.getPropertyType())) {
                Collection property = (Collection<?>) Pojo.get(datum, pd);
                property.addAll((Collection) new AttributfeldAttributAdapter(property.getClass(), pd.getReadMethod().getAnnotation(AttributfeldDefinition.class)).unmarshal(att));
            } else {
                Pojo.set(datum, pd, adapter.unmarshal(att));
            }
        }
        return datum;
    }

    private static boolean ignorieren(PropertyDescriptor pd) {
        return "class".equals(pd.getName()) || pd.getReadMethod().getAnnotation(Ignorieren.class) != null;
    }

    private static Data getAttribut(Data data, PropertyDescriptor pd) {
        String attributname;
        if (attributDefinitionAngegeben(pd)) {
            attributname = pd.getReadMethod().getAnnotation(AttributDefinition.class).name();
        } else {
            try {
                attributname = firstToUpper(pd);
                return data.getItem(attributname);
            } catch (NoSuchElementException | IllegalArgumentException ex) {
                attributname = firstToLower(pd);
            }
        }
        return data.getItem(attributname);
    }

    private static boolean attributDefinitionAngegeben(PropertyDescriptor pd) {
        AttributDefinition definition = pd.getReadMethod().getAnnotation(AttributDefinition.class);
        return definition != null && !definition.name().isEmpty();
    }

    private static String firstToUpper(PropertyDescriptor pd) {
        return pd.getName().substring(0, 1).toUpperCase(Locale.GERMANY) + pd.getName().substring(1);
    }

    private static String firstToLower(PropertyDescriptor pd) {
        return pd.getName().substring(0, 1).toLowerCase(Locale.GERMANY) + pd.getName().substring(1);
    }

}
