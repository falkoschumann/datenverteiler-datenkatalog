/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.bind;

import java.beans.PropertyDescriptor;

class StandardAttributAdapterFactory {

    AttributAdapter createAdapter(PropertyDescriptor pd) {
        // Struktur
        if (Pojo.isAttributliste(pd)) return new AttributlistenAttributAdapter(pd.getPropertyType());
        if (Pojo.isAttributfeld(pd)) return new AttributfeldAttributAdapter(pd.getPropertyType(), pd.getReadMethod().getAnnotation(AttributfeldDefinition.class));

        // Attribute
        if (Pojo.isZeitstempel(pd)) return new ZeitstempelAttributAdapter(pd.getPropertyType(), pd.getReadMethod().getAnnotation(Zeitstempel.class));
        if (Pojo.isDouble(pd)) return new DoubleAttributAdapter();
        if (Pojo.isFloat(pd)) return new FloatAttributAdapter();
        if (Pojo.isLong(pd)) return new LongAttributAdapter();
        if (Pojo.isInteger(pd)) return new IntegerAttributAdapter();
        if (Pojo.isShort(pd)) return new ShortAttributAdapter();
        if (Pojo.isByte(pd)) return new ByteAttributAdapter();
        if (Pojo.isString(pd)) return new StringAttributAdapter();
        if (Pojo.isObjektreferenz(pd)) return new ObjektreferenzAttributAdapter();

        // Sonderfälle
        if (Pojo.isZustand(pd)) return new ZustandAttributAdapter(pd.getPropertyType());
        if (Pojo.isBoolean(pd)) return new BooleanAttributAdapter();

        throw new IllegalStateException("Kein AttributAdapter gefunden für " + pd + ".");
    }

}
