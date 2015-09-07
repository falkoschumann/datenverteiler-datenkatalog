/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.bind;

import de.bsvrz.dav.daf.main.Data;

class DoubleAttributAdapter implements AttributAdapter {

    @Override
    public void marshal(final Object propertyValue, final Data attribut) {
        attribut.asScaledValue().set((double) propertyValue);
    }

    @Override
    public Object unmarshal(final Data attribut) {
        return attribut.asScaledValue().doubleValue();
    }

}
