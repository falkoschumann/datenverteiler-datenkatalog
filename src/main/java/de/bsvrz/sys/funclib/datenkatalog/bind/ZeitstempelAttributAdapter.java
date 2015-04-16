/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.bind;

import de.bsvrz.dav.daf.main.Data;

import java.util.Date;

class ZeitstempelAttributAdapter implements AttributAdapter {

    private final Class<?> clazz;
    private final Zeitstempel zeitstempel;

    ZeitstempelAttributAdapter(Class<?> clazz, Zeitstempel zeitstempel) {
        this.clazz = clazz;
        this.zeitstempel = zeitstempel;
    }

    @Override
    public void marshal(final Object propertyValue, final Data attribut) {
        if (clazz == Date.class) {
            attribut.asTimeValue().setMillis(((Date) propertyValue).getTime());
        } else {
            switch (zeitstempel.genauigkeit()) {
                case SEKUNDEN:
                    attribut.asTimeValue().setSeconds((long) propertyValue);
                    break;
                case MILLISEKUNDEN:
                    attribut.asTimeValue().setMillis((long) propertyValue);
                    break;
                default:
                    throw new IllegalStateException("unreachable code");
            }
        }
    }

    @Override
    public Object unmarshal(final Data attribut) {
        if (clazz == Date.class) {
            return new Date(attribut.asTimeValue().getMillis());
        } else {
            switch (zeitstempel.genauigkeit()) {
                case SEKUNDEN:
                    return attribut.asTimeValue().getSeconds();
                case MILLISEKUNDEN:
                    return attribut.asTimeValue().getMillis();
                default:
                    throw new IllegalStateException("unreachable code");
            }
        }
    }

}
