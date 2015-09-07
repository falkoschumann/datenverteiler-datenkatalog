/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.bind;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.SystemObject;

class ObjektreferenzAttributAdapter implements AttributAdapter {

    @Override
    public void marshal(final Object propertyValue, final Data attribut) {
        attribut.asReferenceValue().setSystemObject((SystemObject) propertyValue);
    }

    @Override
    public Object unmarshal(final Data attribut) {
        return attribut.asReferenceValue().getSystemObject();
    }

}
