/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.bind;

import de.bsvrz.dav.daf.main.Data;

class BooleanAttributAdapter implements AttributAdapter {

    @Override
    public void marshal(final Object propertyValue, final Data attribut) {
        boolean b = (boolean) propertyValue;
        attribut.asUnscaledValue().setText(b ? "Ja" : "Nein");
    }

    @Override
    public Object unmarshal(final Data attribut) {
        return "Ja".equals(attribut.asUnscaledValue().getState().getName());
    }

}
