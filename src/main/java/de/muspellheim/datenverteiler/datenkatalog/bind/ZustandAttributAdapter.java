/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.bind;

import de.bsvrz.dav.daf.main.Data;

class ZustandAttributAdapter implements AttributAdapter {

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
