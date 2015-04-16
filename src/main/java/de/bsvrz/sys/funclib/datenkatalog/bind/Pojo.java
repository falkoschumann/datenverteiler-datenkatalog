/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.bind;

import de.bsvrz.dav.daf.main.config.SystemObject;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Date;

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
        if (!isReadable(pd))
            throw new DataBindingException("Die Property " + pd.getName() + " der Klasse " + obj.getClass().getName() + " hat keinen Getter und kann somit nicht gelesen werden.");

        try {
            return pd.getReadMethod().invoke(obj);
        } catch (Exception ex) {
            throw new DataBindingException("Fehler beim Lesen der Property " + pd.getName() + " eines Objekts der Klasse " + obj.getClass().getName() + ".", ex);
        }
    }

    public static boolean isReadable(PropertyDescriptor pd) {
        return pd.getReadMethod() != null;
    }

    public static void set(Object obj, PropertyDescriptor pd, Object value) {
        if (!isWritable(pd))
            throw new DataBindingException("Die Property " + pd.getName() + " der Klasse " + obj.getClass().getName() + " hat keinen Setter und kann somit nicht geschrieben werden.");

        try {
            pd.getWriteMethod().invoke(obj, value);
        } catch (Exception ex) {
            throw new DataBindingException("Fehler beim Schreiben der Property " + pd.getName() + " eines Objekts der Klasse " + obj.getClass().getName() + ".", ex);
        }
    }

    public static boolean isWritable(PropertyDescriptor pd) {
        return pd.getWriteMethod() != null;
    }

    public static boolean isZustand(PropertyDescriptor pd) {
        return pd.getPropertyType().getAnnotation(Zustand.class) != null;
    }

    public static boolean isZeitstempel(PropertyDescriptor pd) {
        return pd.getReadMethod().getAnnotation(Zeitstempel.class) != null || pd.getPropertyType() == Date.class;
    }

    public static boolean isObjektreferenz(PropertyDescriptor pd) {
        return pd.getPropertyType().isAssignableFrom(SystemObject.class);
    }

    public static boolean isAttributliste(PropertyDescriptor pd) {
        return pd.getPropertyType().getAnnotation(AttributlistenDefinition.class) != null;
    }

    public static boolean isAttributfeld(PropertyDescriptor pd) {
        return pd.getReadMethod().getAnnotation(AttributfeldDefinition.class) != null || pd.getReadMethod().getReturnType().isArray();
    }

    public static boolean isDouble(PropertyDescriptor pd) {
        return pd.getPropertyType() == Double.class || pd.getPropertyType() == double.class;
    }

    public static boolean isFloat(PropertyDescriptor pd) {
        return pd.getPropertyType() == Float.class || pd.getPropertyType() == float.class;
    }

    public static boolean isLong(PropertyDescriptor pd) {
        return pd.getPropertyType() == Long.class || pd.getPropertyType() == long.class;
    }

    public static boolean isInteger(PropertyDescriptor pd) {
        return pd.getPropertyType() == Integer.class || pd.getPropertyType() == int.class;
    }

    public static boolean isShort(PropertyDescriptor pd) {
        return pd.getPropertyType() == Short.class || pd.getPropertyType() == short.class;
    }

    public static boolean isByte(PropertyDescriptor pd) {
        return pd.getPropertyType() == Byte.class || pd.getPropertyType() == byte.class;
    }

    public static boolean isString(PropertyDescriptor pd) {
        return pd.getPropertyType() == String.class;
    }

    public static boolean isBoolean(PropertyDescriptor pd) {
        return pd.getPropertyType() == Boolean.class || pd.getPropertyType() == boolean.class;
    }

}