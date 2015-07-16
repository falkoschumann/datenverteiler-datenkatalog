/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.bind;

import de.bsvrz.dav.daf.main.Data;

import java.time.*;
import java.util.Date;

class ZeitstempelAttributAdapter implements AttributAdapter {

    // TODO Die Zeitstempel werden in mehreren Klassen hin- und hergerechnet -> Refaktorieren

    private final Class<?> clazz;
    private final Zeitstempel zeitstempel;

    ZeitstempelAttributAdapter(Class<?> clazz) {
        this(clazz, null);
    }

    ZeitstempelAttributAdapter(Class<?> clazz, Zeitstempel zeitstempel) {
        this.clazz = clazz;
        this.zeitstempel = zeitstempel;
    }

    @Override
    public void marshal(final Object propertyValue, final Data attribut) {
        if (clazz == LocalDateTime.class) {
            attribut.asTimeValue().setMillis(((LocalDateTime) propertyValue).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        } else if (clazz == Duration.class) {
            attribut.asTimeValue().setMillis(((Duration) propertyValue).toMillis());
        } else if (clazz == Date.class) {
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
        if (clazz == LocalDateTime.class) {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(attribut.asTimeValue().getMillis()), ZoneId.systemDefault());
        } else         if (clazz == Duration.class) {
            return Duration.ofMillis(attribut.asTimeValue().getMillis());
        } else if (clazz == Date.class) {
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
