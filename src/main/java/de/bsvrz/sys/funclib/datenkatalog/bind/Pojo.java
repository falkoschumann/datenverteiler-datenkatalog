/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.bind;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

final class Pojo {

    private Pojo() {
        // utility class
    }

    public static BeanInfo getBeanInfo(Class<?> clazz) {
        try {
            return Introspector.getBeanInfo(clazz);
        } catch (Exception ex) {
            throw new DataBindingException("Fehler beim Analysieren der Klasse " + clazz + ".", ex);
        }
    }

    public static Object create(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception ex) {
            throw new DataBindingException("Fehler beim Instanziieren eines Objekts der Klasse " + clazz + ".", ex);
        }
    }

    public static Object get(Object obj, PropertyDescriptor pd) {
        try {
            return pd.getReadMethod().invoke(obj);
        } catch (Exception ex) {
            throw new DataBindingException("Fehler beim Lesen der Property " + pd.getName() + " eines Objekts der Klasse " + obj.getClass() + ".", ex);
        }
    }

    public static void set(Object obj, PropertyDescriptor pd, Object value) {
        try {
            pd.getWriteMethod().invoke(obj, value);
        } catch (Exception ex) {
            throw new DataBindingException("Fehler beim Schreiben der Property " + pd.getName() + " eines Objekts der Klasse " + obj.getClass() + ".", ex);
        }
    }

}