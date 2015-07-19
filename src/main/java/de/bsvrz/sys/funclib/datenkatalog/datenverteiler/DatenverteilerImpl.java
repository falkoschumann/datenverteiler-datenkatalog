/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.datenverteiler;

import de.bsvrz.dav.daf.main.*;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.datenkatalog.bind.AttributgruppenDefinition;
import de.bsvrz.sys.funclib.datenkatalog.bind.Context;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
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
    public void anmeldenAlsQuelle(Collection<SystemObject> objekte, Class<?> datumTyp, Aspect aspekt) throws DatenverteilerException {
        Objects.requireNonNull(objekte, "objekte");
        Objects.requireNonNull(datumTyp, "datumTyp");
        Objects.requireNonNull(aspekt, "aspekt");

        try {
            dav.subscribeSender(sender, objekte, getDataDescription(datumTyp, aspekt), SenderRole.source());
        } catch (OneSubscriptionPerSendData ex) {
            throw new DatenverteilerException("Doppelte Anmeldung als Quelle.", ex);
        }
    }

    private DataDescription getDataDescription(Class<?> datumTyp, Aspect asp) {
        return new DataDescription(getAttributgruppe(datumTyp), asp);
    }

    private AttributeGroup getAttributgruppe(Class<?> datumTyp) {
        return dav.getDataModel().getAttributeGroup(datumTyp.getAnnotation(AttributgruppenDefinition.class).pid());
    }

    @Override
    public void anmeldenAlsSender(Collection<SystemObject> objekte, Class<?> datumTyp, Aspect aspekt) throws DatenverteilerException {
        Objects.requireNonNull(objekte, "objekte");
        Objects.requireNonNull(datumTyp, "datumTyp");
        Objects.requireNonNull(aspekt, "aspekt");

        try {
            dav.subscribeSender(sender, objekte, getDataDescription(datumTyp, aspekt), SenderRole.sender());
        } catch (OneSubscriptionPerSendData ex) {
            throw new DatenverteilerException("Doppelte Anmeldung als Sender.", ex);
        }
    }

    @Override
    public void abmeldenAlsSender(Collection<SystemObject> objekte, Class<?> datumTyp, Aspect aspekt) {
        Objects.requireNonNull(objekte, "objekte");
        Objects.requireNonNull(datumTyp, "datumTyp");
        Objects.requireNonNull(aspekt, "aspekt");

        dav.unsubscribeSender(sender, objekte, getDataDescription(datumTyp, aspekt));
    }

    @Override
    public <T> void anmeldenAlsSenke(Consumer<Datensatz<T>> empfaenger, Collection<SystemObject> objekte, Class<T> datumTyp, Aspect aspekt, Empfaengeroption option) {
        anmeldenAlsEmpfaenger(empfaenger, objekte, datumTyp, aspekt, ReceiverRole.drain(), option);
    }

    private <T> void anmeldenAlsEmpfaenger(Consumer<Datensatz<T>> empfaenger, Collection<SystemObject> objekte, Class<T> datumTyp, Aspect aspekt, ReceiverRole role, Empfaengeroption option) {
        Objects.requireNonNull(empfaenger, "empfaenger");
        Objects.requireNonNull(objekte, "objekte");
        Objects.requireNonNull(datumTyp, "datumTyp");
        Objects.requireNonNull(aspekt, "aspekt");

        if (!empfaengerliste.containsKey(empfaenger)) {
            empfaengerliste.put(empfaenger, new Empfaenger<>(context, datumTyp));
            empfaengerliste.get(empfaenger).connectConsumer(empfaenger);
            dav.subscribeReceiver(empfaengerliste.get(empfaenger), objekte, getDataDescription(datumTyp, aspekt), getReceiverOptions(option), role);
        }
    }

    @Override
    public <T> void anmeldenAlsEmpfaenger(Consumer<Datensatz<T>> empfaenger, Collection<SystemObject> objekte, Class<T> datumTyp, Aspect aspekt, Empfaengeroption option) {
        anmeldenAlsEmpfaenger(empfaenger, objekte, datumTyp, aspekt, ReceiverRole.receiver(), option);
    }

    private ReceiveOptions getReceiverOptions(Empfaengeroption option) {
        switch (option) {
            case NORMAL:
                return ReceiveOptions.normal();
            case DELTA:
                return ReceiveOptions.delta();
            case NACHGELIEFERT:
                return ReceiveOptions.delayed();
            default:
                throw new IllegalStateException("Unbekannte Empfängeroption: " + option);
        }
    }

    @Override
    public <T> void abmeldenAlsEmpfaenger(Consumer<Datensatz<T>> empfaenger, Collection<SystemObject> objekte, Class<T> datumTyp, Aspect aspekt) {
        Objects.requireNonNull(empfaenger, "empfaenger");
        Objects.requireNonNull(objekte, "objekte");
        Objects.requireNonNull(datumTyp, "datumTyp");
        Objects.requireNonNull(aspekt, "aspekt");

        if (empfaengerliste.containsKey(empfaenger)) {
            empfaengerliste.get(empfaenger).disconnectConsumer(empfaenger);
            dav.unsubscribeReceiver(empfaengerliste.get(empfaenger), objekte, getDataDescription(datumTyp, aspekt));
            empfaengerliste.remove(empfaenger);
        }
    }

    @Override
    public <T> void anmeldenAufParameter(Consumer<Datensatz<T>> empfaenger, Collection<SystemObject> objekte, Class<T> datumTyp) {
        Objects.requireNonNull(empfaenger, "empfaenger");
        Objects.requireNonNull(objekte, "objekte");
        Objects.requireNonNull(datumTyp, "datumTyp");

        ParameterEmpfaenger<T> parameterEmpfaenger = new ParameterEmpfaenger<>(empfaenger);
        anmeldenAlsEmpfaenger(parameterEmpfaenger, objekte, datumTyp, getParameterSoll(), Empfaengeroption.NORMAL);
        parameterEmpfaenger.mutex.lock();
        try {
            parameterEmpfaenger.ersterDatensatzEmpfangen.await(30, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
            // nothing to do
        } finally {
            parameterEmpfaenger.mutex.unlock();
        }
    }

    private Aspect getParameterSoll() {
        return getAspekt("asp.parameterSoll");
    }

    @Override
    public <T> void abmeldenVonParameter(Consumer<Datensatz<T>> empfaenger, Collection<SystemObject> objekte, Class<T> datumTyp) {
        Objects.requireNonNull(empfaenger, "empfaenger");
        Objects.requireNonNull(objekte, "objekte");
        Objects.requireNonNull(datumTyp, "datumTyp");

        abmeldenAlsEmpfaenger(empfaenger, objekte, datumTyp, getParameterSoll());
    }

    @Override
    public <T> Datensatz<T> getParameter(SystemObject objekt, Class<T> datumTyp) {
        Objects.requireNonNull(objekt, "objekt");
        Objects.requireNonNull(datumTyp, "datumTyp");

        ResultData rd = dav.getData(objekt, getDataDescription(datumTyp, getParameterSoll()), 0);
        return unmarshal(rd, datumTyp);
    }

    private <T> Datensatz<T> unmarshal(ResultData rd, Class<T> datumTyp) {
        T datum = context.createUnmarshaller().unmarshal(rd.getData(), datumTyp);
        LocalDateTime zeitstempel = LocalDateTime.ofInstant(Instant.ofEpochMilli(rd.getDataTime()), ZoneId.systemDefault());
        return Datensatz.of(rd.getObject(), datum, getParameterSoll(), zeitstempel);
    }

    @Override
    public <T> T getKonfiguration(SystemObject objekt, Class<T> datumTyp) {
        Objects.requireNonNull(objekt, "objekt");
        Objects.requireNonNull(datumTyp, "datumTyp");

        Data data = objekt.getConfigurationData(getAttributgruppe(datumTyp));
        return context.createUnmarshaller().unmarshal(data, datumTyp);
    }

    @Override
    public void sendeDatensatz(Datensatz<?> datensatz) throws DatenverteilerException {
        sendeDatensaetze(Collections.singleton(datensatz));
    }

    @Override
    public void sendeDatensaetze(Collection<Datensatz<?>> datensaetze) throws DatenverteilerException {
        Objects.requireNonNull(datensaetze, "datensaetze");

        try {
            dav.sendData(datensaetze.stream().map(this::marshall).toArray(ResultData[]::new));
        } catch (SendSubscriptionNotConfirmed ex) {
            throw new DatenverteilerException("Datensatz ist nicht zum Senden angemeldet.", ex);
        }
    }

    private ResultData marshall(Datensatz<?> datensatz) {
        Data data = context.createMarshaller().marshal(datensatz.getDatum());
        long zeitstempel = datensatz.getZeitstempel().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return new ResultData(datensatz.getObjekt(), getDataDescription(datensatz.getDatum().getClass(), datensatz.getAspekt()), zeitstempel, data);
    }

    @Override
    public SystemObject getObjekt(String pid) throws DatenverteilerException {
        Objects.requireNonNull(pid, "pid");
        return dav.getDataModel().getObject(pid);
    }

    @Override
    public Aspect getAspekt(String pid) throws DatenverteilerException {
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

    private static class ParameterEmpfaenger<T> implements Consumer<Datensatz<T>> {

        private final Lock mutex = new ReentrantLock();
        private final Condition ersterDatensatzEmpfangen = mutex.newCondition();

        private final Consumer<Datensatz<T>> empfaenger;

        ParameterEmpfaenger(Consumer<Datensatz<T>> empfaenger) {
            this.empfaenger = empfaenger;
        }

        @Override
        public void accept(Datensatz<T> datensatz) {
            empfaenger.accept(datensatz);

            mutex.lock();
            ersterDatensatzEmpfangen.signal();
            mutex.unlock();
        }

    }

}
