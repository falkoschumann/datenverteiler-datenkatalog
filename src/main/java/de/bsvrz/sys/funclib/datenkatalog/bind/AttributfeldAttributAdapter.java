/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.bind;

import de.bsvrz.dav.daf.main.Data;

import java.util.*;
import java.util.stream.Collectors;

class AttributfeldAttributAdapter implements AttributAdapter {

    private final Class<?> clazz;
    private final AttributfeldDefinition attributfeldDefinition;

    public AttributfeldAttributAdapter(Class<?> clazz, AttributfeldDefinition attributfeldDefinition) {
        this.clazz = clazz;
        this.attributfeldDefinition = attributfeldDefinition;
    }

    private static long[] dateArrayToLongArray(Date[] array) {
        return Arrays.asList(array).stream().mapToLong(Date::getTime).toArray();
    }

    private static Date[] longArrayToDateArray(long[] array) {
        return Arrays.stream(array).boxed().map(Date::new).collect(Collectors.toList()).toArray(new Date[array.length]);
    }

    private static boolean isDouble(Class<?> clazz) {
        return clazz == Double.class || clazz == double.class;
    }

    private static boolean isFloat(Class<?> clazz) {
        return clazz == Float.class || clazz == float.class;
    }

    private static boolean isLong(Class<?> clazz) {
        return clazz == Long.class || clazz == long.class;
    }

    private static boolean isInteger(Class<?> clazz) {
        return clazz == Integer.class || clazz == int.class;
    }

    private static boolean isShort(Class<?> clazz) {
        return clazz == Short.class || clazz == short.class;
    }

    private static boolean isByte(Class<?> clazz) {
        return clazz == Byte.class || clazz == byte.class;
    }

    private static boolean isString(Class<?> clazz) {
        return clazz == String.class;
    }

    private static boolean isDate(Class<?> clazz) {
        return clazz == Date.class;
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
            AttributAdapter adapter = new AttributlistenAttributAdapter(e.getClass());
            adapter.marshal(e, attribut.asArray().getItem(i++));
        }
    }

    private void marshalArray(Object propertyValue, Data attribut) {
        if (isDouble(clazz.getComponentType())) attribut.asScaledArray().set((double[]) propertyValue);
        else if (isFloat(clazz.getComponentType())) attribut.asScaledArray().set((float[]) propertyValue);
        else if (isLong(clazz.getComponentType())) attribut.asUnscaledArray().set((long[]) propertyValue);
        else if (isInteger(clazz.getComponentType())) attribut.asUnscaledArray().set((int[]) propertyValue);
        else if (isShort(clazz.getComponentType())) attribut.asUnscaledArray().set((short[]) propertyValue);
        else if (isByte(clazz.getComponentType())) attribut.asUnscaledArray().set((byte[]) propertyValue);
        else if (isString(clazz.getComponentType())) attribut.asTextArray().set((String[]) propertyValue);
        else if (isDate(clazz.getComponentType()))
            attribut.asTimeArray().setMillis(dateArrayToLongArray((Date[]) propertyValue));
        else throw new IllegalStateException("unreachable code");
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
        List<Object> result = new ArrayList<>();
        for (int i = 0; i < attribut.asArray().getLength(); i++) {
            result.add(new AttributlistenAttributAdapter(attributfeldDefinition.elementtyp()).unmarshal(attribut.asArray().getItem(i)));
        }
        return result;
    }

    private Object unmarshalArray(Data attribut) {
        if (isDouble(clazz.getComponentType())) return attribut.asScaledArray().getDoubleArray();
        else if (isFloat(clazz.getComponentType())) return attribut.asScaledArray().getFloatArray();
        else if (isLong(clazz.getComponentType())) return attribut.asUnscaledArray().getLongArray();
        else if (isInteger(clazz.getComponentType())) return attribut.asUnscaledArray().getIntArray();
        else if (isShort(clazz.getComponentType())) return attribut.asUnscaledArray().getShortArray();
        else if (isByte(clazz.getComponentType())) return attribut.asUnscaledArray().getByteArray();
        else if (isString(clazz.getComponentType())) return attribut.asTextArray().getTextArray();
        else if (isDate(clazz.getComponentType())) return longArrayToDateArray(attribut.asTimeArray().getMillisArray());
        else throw new IllegalStateException("unreachable code");
    }

}
