/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.datenverteiler;

import de.bsvrz.dav.daf.main.*;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.debug.Debug;
import de.muspellheim.datenverteiler.datenkatalog.bind.AttributgruppenDefinition;
import de.muspellheim.datenverteiler.datenkatalog.bind.Context;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Consumer;

/**
 * Standardimplementierung der Schnittstelle {@link Datenverteiler}.
 *
 * @author Falko Schumann
 * @since 1.2
 */
public class DatenverteilerImpl implements Datenverteiler {

    private static final Debug log = Debug.getLogger();

    private final Map<Empfaengeranmeldung, Empfaenger> empfaengerliste = new LinkedHashMap<>();
    private final ClientSenderInterface sender = new Sender();

    private final ClientDavInterface dav;
    private final Context context;

    public DatenverteilerImpl(ClientDavInterface dav) {
        this.dav = dav;
        context = new Context(dav.getDataModel());
    }

    @Override
    public void anmeldenAlsQuelle(Collection<SystemObject> objekte, Class<?> datumTyp, Aspect aspekt) throws DatenverteilerException {
        anmeldenAlsSender(objekte, datumTyp, aspekt, SenderRole.source());
    }

    @Override
    public void anmeldenAlsSender(Collection<SystemObject> objekte, Class<?> datumTyp, Aspect aspekt) throws DatenverteilerException {
        anmeldenAlsSender(objekte, datumTyp, aspekt, SenderRole.sender());
    }

    private void anmeldenAlsSender(Collection<SystemObject> objekte, Class<?> datumTyp, Aspect aspekt, SenderRole rolle) {
        Objects.requireNonNull(objekte, "objekte");
        Objects.requireNonNull(datumTyp, "datumTyp");
        Objects.requireNonNull(aspekt, "aspekt");

        DataDescription datenbeschreibung = getDataDescription(datumTyp, aspekt);
        log.fine("Anmeldung als " + rolle + " für " + datenbeschreibung, objekte);
        try {
            dav.subscribeSender(sender, objekte, datenbeschreibung, rolle);
        } catch (OneSubscriptionPerSendData ex) {
            throw new DatenverteilerException("Doppelte Anmeldung als Quelle.", ex);
        }
    }

    private DataDescription getDataDescription(Class<?> datumTyp, Aspect asp) {
        return new DataDescription(getAttributeGroup(datumTyp), asp);
    }

    private AttributeGroup getAttributeGroup(Class<?> datumTyp) {
        return dav.getDataModel().getAttributeGroup(datumTyp.getAnnotation(AttributgruppenDefinition.class).pid());
    }

    @Override
    public void abmeldenAlsSender(Collection<SystemObject> objekte, Class<?> datumTyp, Aspect aspekt) {
        Objects.requireNonNull(objekte, "objekte");
        Objects.requireNonNull(datumTyp, "datumTyp");
        Objects.requireNonNull(aspekt, "aspekt");

        DataDescription datenbeschreibung = getDataDescription(datumTyp, aspekt);
        log.fine("Abmeldung als Sender für  " + datenbeschreibung, objekte);
        dav.unsubscribeSender(sender, objekte, datenbeschreibung);
    }

    @Override
    public <T> void anmeldenAlsSenke(Consumer<Datensatz<T>> empfaenger, Collection<SystemObject> objekte, Class<T> datumTyp, Aspect aspekt, Empfaengeroption option) {
        anmeldenAlsEmpfaenger(empfaenger, objekte, datumTyp, aspekt, ReceiverRole.drain(), option);
    }

    @Override
    public <T> void anmeldenAlsEmpfaenger(Consumer<Datensatz<T>> empfaenger, Collection<SystemObject> objekte, Class<T> datumTyp, Aspect aspekt, Empfaengeroption option) {
        anmeldenAlsEmpfaenger(empfaenger, objekte, datumTyp, aspekt, ReceiverRole.receiver(), option);
    }

    private <T> void anmeldenAlsEmpfaenger(Consumer<Datensatz<T>> empfaenger, Collection<SystemObject> objekte, Class<T> datumTyp, Aspect aspekt, ReceiverRole role, Empfaengeroption option) {
        Objects.requireNonNull(empfaenger, "empfaenger");
        Objects.requireNonNull(objekte, "objekte");
        Objects.requireNonNull(datumTyp, "datumTyp");
        Objects.requireNonNull(aspekt, "aspekt");

        Empfaengeranmeldung anmeldung = Empfaengeranmeldung.of(objekte, datumTyp, aspekt);
        if (!empfaengerliste.containsKey(anmeldung)) {
            empfaengerliste.put(anmeldung, new Empfaenger<>(context, datumTyp));
            empfaengerliste.get(anmeldung).connectConsumer(empfaenger);
            DataDescription datenbeschreibung = getDataDescription(datumTyp, aspekt);
            ReceiveOptions options = getReceiverOptions(option);
            log.fine("Anmeldung als " + role + " für " + datenbeschreibung + " mit Option " + options, objekte);
            dav.subscribeReceiver(empfaengerliste.get(anmeldung), objekte, datenbeschreibung, options, role);
        } else {
            empfaengerliste.get(anmeldung).connectConsumer(empfaenger);
        }
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

        Empfaengeranmeldung anmeldung = Empfaengeranmeldung.of(objekte, datumTyp, aspekt);
        if (empfaengerliste.containsKey(anmeldung)) {
            empfaengerliste.get(anmeldung).disconnectConsumer(empfaenger);
            if (!empfaengerliste.get(anmeldung).hasConsumer()) {
                DataDescription datenbeschreibung = getDataDescription(datumTyp, aspekt);
                log.fine("Abmeldung als Empfänger für " + datenbeschreibung, objekte);
                dav.unsubscribeReceiver(empfaengerliste.get(anmeldung), objekte, datenbeschreibung);
                empfaengerliste.remove(anmeldung);
            }
        }
    }

    @Override
    public <T> void anmeldenAufParameter(Consumer<Datensatz<T>> empfaenger, Collection<SystemObject> objekte, Class<T> datumTyp) {
        anmeldenAlsEmpfaenger(empfaenger, objekte, datumTyp, getParameterSoll(), Empfaengeroption.NORMAL);
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
        log.fine("Parameter empfangen", rd);
        return unmarshal(rd, datumTyp);
    }

    @Override
    public <T> Datensatz<T> unmarshal(ResultData rd, Class<T> datumTyp) {
        T datum = context.createUnmarshaller().unmarshal(rd.getData(), datumTyp);
        LocalDateTime zeitstempel = konvertiereZeitstempelVomDatenverteilerZeitstempel(rd.getDataTime());
        return Datensatz.of(rd.getObject(), datum, rd.getDataDescription().getAspect(), zeitstempel);
    }

    @Override
    public <T> T getKonfiguration(SystemObject objekt, Class<T> datumTyp) {
        Objects.requireNonNull(objekt, "objekt");
        Objects.requireNonNull(datumTyp, "datumTyp");

        Data data = objekt.getConfigurationData(getAttributeGroup(datumTyp));
        log.fine("Konfigurationsdaten abgerufen für " + objekt, data);
        return context.createUnmarshaller().unmarshal(data, datumTyp);
    }

    @Override
    public void sendeDatensatz(Datensatz<?> datensatz) throws DatenverteilerException {
        sendeDatensaetze(Collections.singleton(datensatz));
    }

    @Override
    public void sendeDatensaetze(Collection<? extends Datensatz<?>> datensaetze) throws DatenverteilerException {
        Objects.requireNonNull(datensaetze, "datensaetze");

        try {
            ResultData[] results = datensaetze.stream().map(this::marshal).toArray(ResultData[]::new);
            log.fine("Sende Datensätze", results);
            dav.sendData(results);
        } catch (SendSubscriptionNotConfirmed ex) {
            throw new DatenverteilerException("Datensatz ist nicht zum Senden angemeldet.", ex);
        }
    }

    @Override
    public ResultData marshal(Datensatz<?> datensatz) {
        Data data = context.createMarshaller().marshal(datensatz.getDatum());
        long zeitstempel = konvertiereZeitstempelNachDatenverteiler(datensatz.getZeitstempel());
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

    @Override
    public LocalDateTime getAktuellenZeitstempel() {
        return konvertiereZeitstempelVomDatenverteilerZeitstempel(dav.getTime());
    }

    /**
     * Testseam.
     */
    static LocalDateTime konvertiereZeitstempelVomDatenverteilerZeitstempel(long zeitstempel) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(zeitstempel), ZoneId.systemDefault());
    }

    /**
     * Testseam.
     */
    static long konvertiereZeitstempelNachDatenverteiler(LocalDateTime zeitstempel) {
        return zeitstempel.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
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
