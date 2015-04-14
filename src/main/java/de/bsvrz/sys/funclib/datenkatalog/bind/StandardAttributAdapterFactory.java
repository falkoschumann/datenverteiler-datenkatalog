/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.bind;

import de.bsvrz.dav.daf.main.config.SystemObject;

import java.beans.PropertyDescriptor;
import java.util.Date;

class StandardAttributAdapterFactory {

    AttributAdapter createAdapter(PropertyDescriptor pd) {
        if (isZustand(pd)) return new ZustandAttributAdapter(pd.getPropertyType());
        if (isZeitstempel(pd))
            return new ZeitstempelAttributAdapter(pd.getPropertyType(), pd.getReadMethod().getAnnotation(Zeitstempel.class));
        if (isObjektreferenz(pd)) return new ObjektreferenzAttributAdapter();
        if (isAttributliste(pd)) return new AttributlistenAttributAdapter(pd.getPropertyType());
        if (isAttributfeld(pd))
            return new AttributfeldAttributAdapter(pd.getReadMethod().getAnnotation(AttributfeldDefinition.class));
        if (isDouble(pd)) return new DoubleAttributAdapter();
        if (isFloat(pd)) return new FloatAttributAdapter();
        if (isLong(pd)) return new LongAttributAdapter();
        if (isInteger(pd)) return new IntegerAttributAdapter();
        if (isShort(pd)) return new ShortAttributAdapter();
        if (isByte(pd)) return new ByteAttributAdapter();
        if (isString(pd)) return new StringAttributAdapter();
        if (isBoolean(pd)) return new BooleanAttributAdapter();

        throw new IllegalStateException("Kein AttributAdapter gefunden für " + pd + ".");
    }

    private boolean isZustand(PropertyDescriptor pd) {
        return pd.getPropertyType().getAnnotation(Zustand.class) != null;
    }

    private boolean isZeitstempel(PropertyDescriptor pd) {
        return pd.getReadMethod().getAnnotation(Zeitstempel.class) != null || pd.getPropertyType() == Date.class;
    }

    private boolean isObjektreferenz(PropertyDescriptor pd) {
        return pd.getPropertyType().isAssignableFrom(SystemObject.class);
    }

    private boolean isAttributliste(PropertyDescriptor pd) {
        return pd.getPropertyType().getAnnotation(AttributlistenDefinition.class) != null;
    }

    private boolean isAttributfeld(PropertyDescriptor pd) {
        return pd.getReadMethod().getAnnotation(AttributfeldDefinition.class) != null;
    }

    private boolean isDouble(PropertyDescriptor pd) {
        return pd.getPropertyType() == Double.class || pd.getPropertyType() == double.class;
    }

    private boolean isFloat(PropertyDescriptor pd) {
        return pd.getPropertyType() == Float.class || pd.getPropertyType() == float.class;
    }

    private boolean isLong(PropertyDescriptor pd) {
        return pd.getPropertyType() == Long.class || pd.getPropertyType() == long.class;
    }

    private boolean isInteger(PropertyDescriptor pd) {
        return pd.getPropertyType() == Integer.class || pd.getPropertyType() == int.class;
    }

    private boolean isShort(PropertyDescriptor pd) {
        return pd.getPropertyType() == Short.class || pd.getPropertyType() == short.class;
    }

    private boolean isByte(PropertyDescriptor pd) {
        return pd.getPropertyType() == Byte.class || pd.getPropertyType() == byte.class;
    }

    private boolean isString(PropertyDescriptor pd) {
        return pd.getPropertyType() == String.class;
    }

    private boolean isBoolean(PropertyDescriptor pd) {
        return pd.getPropertyType() == Boolean.class || pd.getPropertyType() == boolean.class;
    }

}
