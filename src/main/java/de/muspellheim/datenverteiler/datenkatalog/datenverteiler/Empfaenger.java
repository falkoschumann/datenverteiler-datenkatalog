/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.datenverteiler;

import de.bsvrz.dav.daf.main.ClientReceiverInterface;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.sys.funclib.debug.Debug;
import de.muspellheim.datenverteiler.datenkatalog.bind.Context;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Empfängt einen Onlinedatensatz eines bestimmten Typs und verteilt den Datensatz weiter.
 *
 * @param <T> der Typ der empfangenen Daten.
 * @author Falko Schumann
 * @since 1.2
 */
public class Empfaenger<T> implements ClientReceiverInterface {

    private static final Debug log = Debug.getLogger();

    private final List<Consumer<Datensatz<T>>> consumers = new CopyOnWriteArrayList<>();

    private final Context context;
    private final Class<T> datumTyp;

    /**
     * Initialisiert den Empfänger mit dem Kontext des Databindings und des Typs der Daten.
     */
    public Empfaenger(Context context, Class<T> datumTyp) {
        this.context = context;
        this.datumTyp = datumTyp;
    }

    /**
     * Meldet einen Verbraucher für empfangene Datensätze an.
     */
    public void connectConsumer(Consumer<Datensatz<T>> c) {
        consumers.add(c);
    }

    /**
     * Meldet einen Verbraucher für empfangene Datensätze wieder ab.
     */
    public void disconnectConsumer(Consumer<Datensatz<T>> c) {
        consumers.remove(c);
    }

    /**
     * Prüft, ob der Empfänger noch Verbraucher kennt.
     */
    public boolean hasConsumer() {
        return consumers.isEmpty();
    }

    @Override
    public void update(ResultData[] results) {
        for (ResultData rd : results)
            for (Consumer<Datensatz<T>> c : consumers)
                veroeffentlicheDatensatz(c, rd);
    }

    private void veroeffentlicheDatensatz(Consumer<Datensatz<T>> c, ResultData rd) {
        log.fine("Datensatz empfangen", rd);

        T datum = context.createUnmarshaller().unmarshal(rd.getData(), datumTyp);
        LocalDateTime zeitstempel = LocalDateTime.ofInstant(Instant.ofEpochMilli(rd.getDataTime()), ZoneId.systemDefault());
        Datensatz<T> datensatz = Datensatz.of(rd.getObject(), datum, rd.getDataDescription().getAspect(), zeitstempel);
        c.accept(datensatz);
    }

}
