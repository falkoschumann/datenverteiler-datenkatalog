/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.bind;

import de.bsvrz.dav.daf.main.config.SystemObject;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.time.LocalDateTime;
import java.util.Collection;
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
            throw new DataBindingException("Die Property " + pd.getName() + " der Klasse " + obj.getClass().getName()
                    + " hat keinen Getter und kann somit nicht gelesen werden.");

        try {
            return pd.getReadMethod().invoke(obj);
        } catch (Exception ex) {
            throw new DataBindingException("Fehler beim Lesen der Property " + pd.getName()
                    + " eines Objekts der Klasse " + obj.getClass().getName() + ".", ex);
        }
    }

    public static boolean isReadable(PropertyDescriptor pd) {
        return pd.getReadMethod() != null;
    }

    public static boolean isReadOnly(PropertyDescriptor pd) {
        return !isWritable(pd);
    }

    public static void set(Object obj, PropertyDescriptor pd, Object value) {
        if (isReadOnly(pd))
            throw new DataBindingException("Die Property " + pd.getName() + " der Klasse " + obj.getClass().getName()
                    + " hat keinen Setter und kann somit nicht geschrieben werden.");

        try {
            pd.getWriteMethod().invoke(obj, value);
        } catch (Exception ex) {
            throw new DataBindingException("Fehler beim Schreiben der Property " + pd.getName()
                    + " eines Objekts der Klasse " + obj.getClass().getName() + ".", ex);
        }
    }

    public static boolean isWritable(PropertyDescriptor pd) {
        return pd.getWriteMethod() != null;
    }

    public static boolean isZustand(PropertyDescriptor pd) {
        return pd.getPropertyType().getAnnotation(Zustand.class) != null;
    }

    public static boolean isZeitstempel(PropertyDescriptor pd) {
        return pd.getReadMethod().getAnnotation(Zeitstempel.class) != null || isZeitstempel(pd.getPropertyType());
    }

    public static boolean isZeitstempel(Class<?> propertyType) {
        return propertyType == Date.class || propertyType == LocalDateTime.class;
    }

    public static boolean isObjektreferenz(PropertyDescriptor pd) {
        return isObjektreferenz(pd.getPropertyType());
    }

    public static boolean isObjektreferenz(Class<?> propertyType) {
        return propertyType.isAssignableFrom(SystemObject.class);
    }

    public static boolean isAttributliste(PropertyDescriptor pd) {
        return isAttributliste(pd.getPropertyType());
    }

    public static boolean isAttributliste(Class<?> propertyType) {
        return propertyType.getAnnotation(AttributlistenDefinition.class) != null;
    }

    public static boolean isAttributfeld(PropertyDescriptor pd) {
        AttributfeldDefinition definition = pd.getReadMethod().getAnnotation(AttributfeldDefinition.class);
        return (definition != null && Collection.class.isAssignableFrom(pd.getPropertyType())) || pd.getPropertyType().isArray();
    }

    public static boolean isDouble(PropertyDescriptor pd) {
        return isDouble(pd.getPropertyType());
    }

    public static boolean isDouble(Class<?> propertyType) {
        return propertyType == Double.class || propertyType == double.class;
    }

    public static boolean isFloat(PropertyDescriptor pd) {
        return isFloat(pd.getPropertyType());
    }

    public static boolean isFloat(Class<?> propertyType) {
        return propertyType == Float.class || propertyType == float.class;
    }

    public static boolean isLong(PropertyDescriptor pd) {
        return isLong(pd.getPropertyType());
    }

    public static boolean isLong(Class<?> propertyType) {
        return propertyType == Long.class || propertyType == long.class;
    }

    public static boolean isInteger(PropertyDescriptor pd) {
        return isInteger(pd.getPropertyType());
    }

    public static boolean isInteger(Class<?> propertyType) {
        return propertyType == Integer.class || propertyType == int.class;
    }

    public static boolean isShort(PropertyDescriptor pd) {
        return isShort(pd.getPropertyType());
    }

    public static boolean isShort(Class<?> propertyType) {
        return propertyType == Short.class || propertyType == short.class;
    }

    public static boolean isByte(PropertyDescriptor pd) {
        return isByte(pd.getPropertyType());
    }

    public static boolean isByte(Class<?> propertyType) {
        return propertyType == Byte.class || propertyType == byte.class;
    }

    public static boolean isString(PropertyDescriptor pd) {
        return isString(pd.getPropertyType());
    }

    public static boolean isString(Class<?> propertyType) {
        return propertyType == String.class;
    }

    public static boolean isBoolean(PropertyDescriptor pd) {
        return isBoolean(pd.getPropertyType());
    }

    public static boolean isBoolean(Class<?> propertyType) {
        return propertyType == Boolean.class || propertyType == boolean.class;
    }

}
