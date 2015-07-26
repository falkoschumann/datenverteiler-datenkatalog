/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.modell;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.sys.funclib.datenkatalog.bind.AttributAdapter;

public class MultiplikatorAdapter implements AttributAdapter {

    @Override
    public void marshal(final Object propertyValue, final Data attribut) {
        attribut.asUnscaledValue().set(((int) propertyValue) / 3);
    }

    @Override
    public Object unmarshal(final Data attribut) {
        return attribut.asUnscaledValue().intValue() * 3;
    }

}
