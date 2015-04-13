/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.bind;

import de.bsvrz.dav.daf.main.Data;

class StringAttributAdapter implements AttributAdapter {

    @Override
    public void marshal(final Object propertyValue, final Data attribut) {
        attribut.asTextValue().setText((String) propertyValue);
    }

    @Override
    public Object unmarshal(final Data attribut) {
        return attribut.asTextValue().getText();
    }

}
