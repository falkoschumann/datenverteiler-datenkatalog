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

import java.time.ZoneId;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Standardimplementierung der Schnittstelle {@link Datenverteiler}.
 *
 * @author Falko Schumann
 * @since 1.2
 */
public class DatenverteilerImpl implements Datenverteiler {

    private final Map<Class<?>, Empfaenger<?>> empfaengerliste = new LinkedHashMap<>();
    private final ClientSenderInterface sender = new Sender();

    private final ClientDavInterface dav;
    private final Context context;

    public DatenverteilerImpl(ClientDavInterface dav) {
        this.dav = dav;
        context = new Context(dav.getDataModel());
    }

    @Override
    public void anmeldenAlsQuelle(Class<?> datumTyp, Aspect aspekt, SystemObject... objekte) throws DatenverteilerException {
        try {
            dav.subscribeSender(sender, objekte, dataDescription(datumTyp, aspekt), SenderRole.source());
        } catch (OneSubscriptionPerSendData ex) {
            throw new DatenverteilerException("Doppelte Anmeldung als Quelle.", ex);
        }
    }

    private DataDescription dataDescription(Class<?> datumTyp, Aspect asp) {
        AttributeGroup atg = dav.getDataModel().getAttributeGroup(datumTyp.getAnnotation(AttributgruppenDefinition.class).pid());
        return new DataDescription(atg, asp);
    }

    @Override
    public void abmeldenAlsQuelle(Class<?> datumTyp, Aspect aspekt, SystemObject... objekte) {
        dav.unsubscribeSender(sender, objekte, dataDescription(datumTyp, aspekt));
    }

    @Override
    public <T> void anmeldenAlsEmpfaenger(Consumer<T> empfaenger, Class<?> datumTyp, Aspect aspekt, SystemObject... objekte) {
        if (!empfaengerliste.containsKey(datumTyp))
            empfaengerliste.put(datumTyp, new Empfaenger<>(context, datumTyp));
        dav.subscribeReceiver(empfaengerliste.get(datumTyp), objekte, dataDescription(datumTyp, aspekt), ReceiveOptions.normal(), ReceiverRole.receiver());
    }

    @Override
    public <T> void abmeldenAlsEmpfaenger(Consumer<T> empfaenger, Class<?> datumTyp, Aspect aspekt, SystemObject... objekte) {
        if (empfaengerliste.containsKey(datumTyp))
            dav.unsubscribeReceiver(empfaengerliste.get(datumTyp), objekte, dataDescription(datumTyp, aspekt));
    }

    @Override
    public void sendeDatensatz(Datensatz<?>... datensaetze) throws DatenverteilerException {
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
    public Aspect aspekt(String pid) {
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
