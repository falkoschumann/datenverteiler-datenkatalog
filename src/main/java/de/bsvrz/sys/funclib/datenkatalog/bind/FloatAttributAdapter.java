/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.bind;

import de.bsvrz.dav.daf.main.Data;

class FloatAttributAdapter implements AttributAdapter {

    @Override
    public void marshal(final Object propertyValue, final Data attribut) {
        attribut.asScaledValue().set((float) propertyValue);
    }

    @Override
    public Object unmarshal(final Data attribut) {
        return attribut.asScaledValue().floatValue();
    }

}
