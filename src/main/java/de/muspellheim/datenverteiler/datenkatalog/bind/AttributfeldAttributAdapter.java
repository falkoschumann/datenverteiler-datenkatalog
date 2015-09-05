/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.bind;

import de.bsvrz.dav.daf.main.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

class AttributfeldAttributAdapter implements AttributAdapter {

    private final Class<?> clazz;
    private final AttributfeldDefinition attributfeldDefinition;

    AttributfeldAttributAdapter(Class<?> clazz, AttributfeldDefinition attributfeldDefinition) {
        this.clazz = clazz;
        this.attributfeldDefinition = attributfeldDefinition;
    }

    @Override
    public void marshal(final Object propertyValue, final Data attribut) {
        if (Collection.class.isAssignableFrom(clazz)) {
            marshalCollection((Collection) propertyValue, attribut);
        } else if (clazz.isArray()) {
            marshalArray(propertyValue, attribut);
        } else {
            throw new IllegalStateException("unreachable code");
        }
    }

    private void marshalCollection(Collection<?> propertyValue, Data attribut) {
        attribut.asArray().setLength(propertyValue.size());
        int i = 0;
        for (Object e : propertyValue) {
            AttributAdapter adapter = new StandardAttributAdapterFactory().createAdapter(attributfeldDefinition.elementtyp());
            adapter.marshal(e, attribut.asArray().getItem(i++));
        }
    }

    private void marshalArray(Object propertyValue, Data attribut) {
        if (Pojo.isDouble(clazz.getComponentType())) attribut.asScaledArray().set((double[]) propertyValue);
        else if (Pojo.isFloat(clazz.getComponentType())) attribut.asScaledArray().set((float[]) propertyValue);
        else if (Pojo.isLong(clazz.getComponentType())) attribut.asUnscaledArray().set((long[]) propertyValue);
        else if (Pojo.isInteger(clazz.getComponentType())) attribut.asUnscaledArray().set((int[]) propertyValue);
        else if (Pojo.isShort(clazz.getComponentType())) attribut.asUnscaledArray().set((short[]) propertyValue);
        else if (Pojo.isByte(clazz.getComponentType())) attribut.asUnscaledArray().set((byte[]) propertyValue);
        else if (Pojo.isString(clazz.getComponentType())) attribut.asTextArray().set((String[]) propertyValue);
        else if (isDate(clazz.getComponentType())) attribut.asTimeArray().setMillis(dateArrayToLongArray((Date[]) propertyValue));
        else if (isLocalDateTime(clazz.getComponentType())) attribut.asTimeArray().setMillis(localDateTimeArrayToLongArray((LocalDateTime[]) propertyValue));
        else throw new IllegalStateException("unreachable code");
    }

    private static boolean isDate(Class<?> clazz) {
        return clazz == Date.class;
    }

    private static long[] dateArrayToLongArray(Date... array) {
        return Arrays.asList(array).stream().mapToLong(Date::getTime).toArray();
    }


    private static boolean isLocalDateTime(Class<?> clazz) {
        return clazz == LocalDateTime.class;
    }

    private static long[] localDateTimeArrayToLongArray(LocalDateTime... array) {
        return Arrays.asList(array).stream().mapToLong(t -> t.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()).toArray();
    }


    @Override
    public Object unmarshal(final Data attribut) {
        if (Collection.class.isAssignableFrom(clazz)) {
            return unmarshalCollection(attribut);
        } else if (clazz.isArray()) {
            return unmarshalArray(attribut);
        } else {
            throw new IllegalStateException("unreachable code");
        }
    }

    private Collection<?> unmarshalCollection(Data attribut) {
        Collection<Object> result = createCollection();
        for (int i = 0; i < attribut.asArray().getLength(); i++) {
            Data e = attribut.asArray().getItem(i);
            AttributAdapter adapter = new StandardAttributAdapterFactory().createAdapter(attributfeldDefinition.elementtyp());
            result.add(adapter.unmarshal(e));
        }
        return result;
    }

    private Collection<Object> createCollection() {
        if (Set.class.isAssignableFrom(clazz)) return new LinkedHashSet<>();
        if (SortedSet.class.isAssignableFrom(clazz)) return new TreeSet<>();
        return new ArrayList<>();
    }

    private Object unmarshalArray(Data attribut) {
        if (Pojo.isDouble(clazz.getComponentType())) return attribut.asScaledArray().getDoubleArray();
        else if (Pojo.isFloat(clazz.getComponentType())) return attribut.asScaledArray().getFloatArray();
        else if (Pojo.isLong(clazz.getComponentType())) return attribut.asUnscaledArray().getLongArray();
        else if (Pojo.isInteger(clazz.getComponentType())) return attribut.asUnscaledArray().getIntArray();
        else if (Pojo.isShort(clazz.getComponentType())) return attribut.asUnscaledArray().getShortArray();
        else if (Pojo.isByte(clazz.getComponentType())) return attribut.asUnscaledArray().getByteArray();
        else if (Pojo.isString(clazz.getComponentType())) return attribut.asTextArray().getTextArray();
        else if (isDate(clazz.getComponentType())) return longArrayToDateArray(attribut.asTimeArray().getMillisArray());
        else if (isLocalDateTime(clazz.getComponentType())) return longArrayToLocalDateTimeArray(attribut.asTimeArray().getMillisArray());
        else throw new IllegalStateException("unreachable code");
    }

    private static Date[] longArrayToDateArray(long... array) {
        return Arrays.stream(array).boxed().map(Date::new).toArray(Date[]::new);
    }

    private static LocalDateTime[] longArrayToLocalDateTimeArray(long... array) {
        return Arrays.stream(array).boxed().map(t -> LocalDateTime.ofInstant(Instant.ofEpochMilli(t), ZoneId.systemDefault())).toArray(LocalDateTime[]::new);
    }


}
