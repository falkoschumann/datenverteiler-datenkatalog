/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.datenverteiler;

import de.bsvrz.dav.daf.main.*;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.datenkatalog.bind.AttributgruppenDefinition;
import de.bsvrz.sys.funclib.datenkatalog.bind.Context;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Standardimplementierung der Schnittstelle {@link Datenverteiler}.
 *
 * @author Falko Schumann
 * @since 1.2
 */
public class DatenverteilerImpl implements Datenverteiler {

    private final Map<Consumer, Empfaenger> empfaengerliste = new LinkedHashMap<>();
    private final ClientSenderInterface sender = new Sender();

    private final ClientDavInterface dav;
    private final Context context;

    public DatenverteilerImpl(ClientDavInterface dav) {
        this.dav = dav;
        context = new Context(dav.getDataModel());
    }

    @Override
    public void anmeldenAlsQuelle(Class<?> datumTyp, Aspect aspekt, SystemObject... objekte) throws DatenverteilerException {
        Objects.requireNonNull(datumTyp, "datumTyp");
        Objects.requireNonNull(aspekt, "aspekt");
        Objects.requireNonNull(objekte, "objekte");

        try {
            dav.subscribeSender(sender, objekte, dataDescription(datumTyp, aspekt), SenderRole.source());
        } catch (OneSubscriptionPerSendData ex) {
            throw new DatenverteilerException("Doppelte Anmeldung als Quelle.", ex);
        }
    }

    private DataDescription dataDescription(Class<?> datumTyp, Aspect asp) {
        return new DataDescription(attributgruppe(datumTyp), asp);
    }

    private AttributeGroup attributgruppe(Class<?> datumTyp) {
        return dav.getDataModel().getAttributeGroup(datumTyp.getAnnotation(AttributgruppenDefinition.class).pid());
    }

    @Override
    public void anmeldenAlsSender(Class<?> datumTyp, Aspect aspekt, SystemObject... objekte) throws DatenverteilerException {
        Objects.requireNonNull(datumTyp, "datumTyp");
        Objects.requireNonNull(aspekt, "aspekt");
        Objects.requireNonNull(objekte, "objekte");

        try {
            dav.subscribeSender(sender, objekte, dataDescription(datumTyp, aspekt), SenderRole.sender());
        } catch (OneSubscriptionPerSendData ex) {
            throw new DatenverteilerException("Doppelte Anmeldung als Sender.", ex);
        }
    }

    @Override
    public void abmeldenAlsSender(Class<?> datumTyp, Aspect aspekt, SystemObject... objekte) {
        Objects.requireNonNull(datumTyp, "datumTyp");
        Objects.requireNonNull(aspekt, "aspekt");
        Objects.requireNonNull(objekte, "objekte");

        dav.unsubscribeSender(sender, objekte, dataDescription(datumTyp, aspekt));
    }

    @Override
    public <T> void anmeldenAlsSenke(Consumer<Datensatz<T>> empfaenger, Class<T> datumTyp, Aspect aspekt, SystemObject... objekte) {
        anmeldenAlsEmpfaenger(empfaenger, datumTyp, aspekt, objekte, ReceiverRole.drain());
    }

    private <T> void anmeldenAlsEmpfaenger(Consumer<Datensatz<T>> empfaenger, Class<T> datumTyp, Aspect aspekt, SystemObject[] objekte, ReceiverRole role) {
        Objects.requireNonNull(empfaenger, "empfaenger");
        Objects.requireNonNull(datumTyp, "datumTyp");
        Objects.requireNonNull(aspekt, "aspekt");
        Objects.requireNonNull(objekte, "objekte");

        if (!empfaengerliste.containsKey(empfaenger)) {
            empfaengerliste.put(empfaenger, new Empfaenger<>(context, datumTyp));
            empfaengerliste.get(empfaenger).connectConsumer(empfaenger);
            dav.subscribeReceiver(empfaengerliste.get(empfaenger), objekte, dataDescription(datumTyp, aspekt), ReceiveOptions.normal(), role);
        }
    }

    @Override
    public <T> void anmeldenAlsEmpfaenger(Consumer<Datensatz<T>> empfaenger, Class<T> datumTyp, Aspect aspekt, SystemObject... objekte) {
        anmeldenAlsEmpfaenger(empfaenger, datumTyp, aspekt, objekte, ReceiverRole.receiver());
    }

    @Override
    public <T> void abmeldenAlsEmpfaenger(Consumer<Datensatz<T>> empfaenger, Class<T> datumTyp, Aspect aspekt, SystemObject... objekte) {
        Objects.requireNonNull(empfaenger, "empfaenger");
        Objects.requireNonNull(datumTyp, "datumTyp");
        Objects.requireNonNull(aspekt, "aspekt");
        Objects.requireNonNull(objekte, "objekte");

        if (empfaengerliste.containsKey(empfaenger)) {
            empfaengerliste.get(empfaenger).disconnectConsumer(empfaenger);
            dav.unsubscribeReceiver(empfaengerliste.get(empfaenger), objekte, dataDescription(datumTyp, aspekt));
            empfaengerliste.remove(empfaenger);
        }
    }

    @Override
    public <T> void anmeldenAufParameter(Consumer<Datensatz<T>> empfaenger, Class<T> datumTyp, SystemObject... objekte) {
        Objects.requireNonNull(empfaenger, "empfaenger");
        Objects.requireNonNull(datumTyp, "datumTyp");
        Objects.requireNonNull(objekte, "objekte");

        anmeldenAlsEmpfaenger(empfaenger, datumTyp, parameterSoll(), objekte);
    }

    private Aspect parameterSoll() {
        return aspekt("asp.parameterSoll");
    }

    @Override
    public <T> void abmeldenVonParameter(Consumer<Datensatz<T>> empfaenger, Class<T> datumTyp, SystemObject... objekte) {
        Objects.requireNonNull(empfaenger, "empfaenger");
        Objects.requireNonNull(datumTyp, "datumTyp");
        Objects.requireNonNull(objekte, "objekte");

        abmeldenAlsEmpfaenger(empfaenger, datumTyp, parameterSoll(), objekte);
    }

    @Override
    public <T> Datensatz<T> parameter(Class<T> datumTyp, SystemObject objekt) {
        Objects.requireNonNull(datumTyp, "datumTyp");
        Objects.requireNonNull(objekt, "objekt");

        ResultData rd = dav.getData(objekt, dataDescription(datumTyp, parameterSoll()), 0);
        return unmarshal(rd, datumTyp);
    }

    private <T> Datensatz<T> unmarshal(ResultData rd, Class<T> datumTyp) {
        T datum = context.createUnmarshaller().unmarshal(rd.getData(), datumTyp);
        LocalDateTime zeitstempel = LocalDateTime.ofInstant(Instant.ofEpochMilli(rd.getDataTime()), ZoneId.systemDefault());
        return Datensatz.of(rd.getObject(), datum, parameterSoll(), zeitstempel);
    }

    @Override
    public <T> T konfiguration(Class<T> datumTyp, SystemObject objekt) {
        Objects.requireNonNull(datumTyp, "datumTyp");
        Objects.requireNonNull(objekt, "objekt");

        Data data = ((ConfigurationObject) objekt).getConfigurationData(attributgruppe(datumTyp));
        return context.createUnmarshaller().unmarshal(data, datumTyp);
    }

    @Override
    public void sendeDatensatz(Datensatz<?>... datensaetze) throws DatenverteilerException {
        Objects.requireNonNull(datensaetze, "datensaetze");

        try {
            dav.sendData(Arrays.asList(datensaetze).stream().map(this::marshall).toArray(ResultData[]::new));
        } catch (SendSubscriptionNotConfirmed ex) {
            throw new DatenverteilerException("Datensatz ist nicht zum Senden angemeldet.", ex);
        }
    }

    private ResultData marshall(Datensatz<?> datensatz) {
        Data data = context.createMarshaller().marshal(datensatz.getDatum());
        long zeitstempel = datensatz.getZeitstempel().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return new ResultData(datensatz.getObjekt(), dataDescription(datensatz.getDatum().getClass(), datensatz.getAspekt()), zeitstempel, data);
    }

    @Override
    public SystemObject objekt(String pid) {
        Objects.requireNonNull(pid, "pid");
        return dav.getDataModel().getObject(pid);
    }

    @Override
    public Aspect aspekt(String pid) {
        Objects.requireNonNull(pid, "pid");
        return dav.getDataModel().getAspect(pid);
    }

    private static class Sender implements ClientSenderInterface {

        @Override
        public void dataRequest(SystemObject object, DataDescription dataDescription, byte state) {
            // do nothing: Sendesteuerung wird nicht verwendet
        }

        @Override
        public boolean isRequestSupported(SystemObject object, DataDescription dataDescription) {
            return false;
        }

    }

}
