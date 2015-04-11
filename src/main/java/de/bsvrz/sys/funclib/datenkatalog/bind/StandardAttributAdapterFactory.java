/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.bind;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.SystemObject;

import java.beans.PropertyDescriptor;

class StandardAttributAdapterFactory {

    AttributAdapter createAdapter(PropertyDescriptor pd) {
        if (isZustand(pd)) return new ZustandAttributAdapter(pd.getPropertyType());
        if (isObjektreferenz(pd)) return new ObjektreferenzAttributAdapter();

        throw new IllegalStateException("Kein AttributAdapter gefunden für " + pd + ".");
    }

    private boolean isZustand(PropertyDescriptor pd) {
        return pd.getPropertyType().getAnnotation(Zustand.class) != null;
    }

    private boolean isObjektreferenz(PropertyDescriptor pd) {
        return pd.getPropertyType().isAssignableFrom(SystemObject.class);
    }

    private static class ZustandAttributAdapter implements AttributAdapter {

        private final Class<?> enumType;

        ZustandAttributAdapter(Class<?> enumType) {
            this.enumType = enumType;
        }

        @Override
        public void marshal(Object propertyValue, Data attribut) {
            attribut.asUnscaledValue().setText(propertyValue.toString());
        }

        @Override
        public Object unmarshal(Data attribut) {
            final String name = attribut.asTextValue().getText();
            for (final Object e : enumType.getEnumConstants()) {
                if (name.equals(e.toString()))
                    return e;
            }

            throw new IllegalStateException("Das Enum " + enumType + " kennt den Zustand " + name + " nicht.");
        }

    }

    private static final class ObjektreferenzAttributAdapter implements AttributAdapter {

        @Override
        public void marshal(final Object propertyValue, final Data attribut) {
            attribut.asReferenceValue().setSystemObject((SystemObject) propertyValue);
        }

        @Override
        public Object unmarshal(final Data attribut) {
            return attribut.asReferenceValue().getSystemObject();
        }

    }

}
