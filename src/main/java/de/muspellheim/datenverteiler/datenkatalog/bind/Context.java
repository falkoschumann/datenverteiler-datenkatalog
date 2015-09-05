/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.bind;

import de.bsvrz.dav.daf.communication.dataRepresentation.AttributeBaseValueDataFactory;
import de.bsvrz.dav.daf.communication.dataRepresentation.AttributeHelper;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Der Kontext für das Binding und abstrakte Fabrik für {@link Marshaller} und {@link Unmarshaller}.
 *
 * @author Falko Schumann
 * @since 1.0
 */
public class Context {

    private final DatumCache datumCache = new DatumCache();
    private final DataModel model;

    public Context(DataModel model) {
        Objects.requireNonNull(model, "model");
        this.model = model;
    }

    Data createData(AttributeGroup atg) {
        // TODO Feature Request: Methode von ClientDavInterface nach DataModel verschieben
        return datumCache.getDatum(atg);
    }

    /**
     * Erzeugt einen Marshaller.
     *
     * @return der Marshaller.
     */
    public Marshaller createMarshaller() {
        return new MarshallerImpl();
    }

    /**
     * Erzeugt einen Unmarshaller.
     *
     * @return der Unmarshaller.
     */
    public Unmarshaller createUnmarshaller() {
        return new UnmarshallerImpl();
    }

    private static class DatumCache {

        private final Map<AttributeGroup, Data> datumCache = new LinkedHashMap<>();

        synchronized Data getDatum(AttributeGroup atg) {
            if (datumNichtImCache(atg)) cacheDatum(atg);
            Data prototyp = datumCache.get(atg);
            return prototyp.createModifiableCopy();
        }

        private boolean datumNichtImCache(AttributeGroup atg) {
            return !datumCache.containsKey(atg);
        }

        private void cacheDatum(AttributeGroup atg) {
            Data result = AttributeBaseValueDataFactory.createAdapter(atg, AttributeHelper.getAttributesValues(atg));
            result.setToDefault();
            datumCache.put(atg, result.createUnmodifiableCopy());
        }

    }

    private static class UnmarshallerImpl implements Unmarshaller {

        @Override
        @SuppressWarnings("unchecked")
        public <T> T unmarshal(Data data, Class<T> datumTyp) {
            Objects.requireNonNull(datumTyp, "datumTyp");

            if (data == null) return null;

            return (T) new AttributlistenAttributAdapter(datumTyp).unmarshal(data);
        }

    }

    private class MarshallerImpl implements Marshaller {

        @Override
        public Data marshal(Object datum) {
            if (datum == null) return null;

            Data result = createData(getAttributeGroup(datum));
            new AttributlistenAttributAdapter(datum.getClass()).marshal(datum, result);
            return result;
        }

        private AttributeGroup getAttributeGroup(Object datum) {
            AttributgruppenDefinition definition = datum.getClass().getAnnotation(AttributgruppenDefinition.class);
            if (definition == null)
                throw new DataBindingException("Der Klasse " + datum.getClass().getName()
                        + " fehlt die Annotation @" + AttributgruppenDefinition.class.getSimpleName() + ".");

            AttributeGroup result = model.getAttributeGroup(definition.pid());
            if (result == null)
                throw new DataBindingException("Eine Attributgruppe mit der PID " + definition.pid()
                        + " existiert nicht.");
            return result;
        }

    }


}
