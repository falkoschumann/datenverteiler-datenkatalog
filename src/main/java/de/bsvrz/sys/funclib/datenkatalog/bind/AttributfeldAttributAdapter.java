/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.bind;

import de.bsvrz.dav.daf.main.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class AttributfeldAttributAdapter implements AttributAdapter {

    private final Class<?> elementtyp;

    public AttributfeldAttributAdapter(AttributfeldDefinition attributfeldDefinition) {
        elementtyp = attributfeldDefinition.elementtyp();
    }

    @Override
    public void marshal(final Object propertyValue, final Data attribut) {
        Collection<?> list = (Collection) propertyValue;
        attribut.asArray().setLength(list.size());
        int i = 0;
        for (Object e : list) {
            AttributAdapter adapter = new AttributlistenAttributAdapter(e.getClass());
            adapter.marshal(e, attribut.asArray().getItem(i++));
        }
    }

    @Override
    public Object unmarshal(final Data attribut) {
        List<Object> result = new ArrayList<>();
        for (int i = 0; i < attribut.asArray().getLength(); i++) {
            result.add(new AttributlistenAttributAdapter(elementtyp).unmarshal(attribut.asArray().getItem(i)));
        }
        return result;
    }

}
