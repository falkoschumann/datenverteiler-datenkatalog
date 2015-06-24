/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.datenverteiler;

import de.bsvrz.dav.daf.main.ClientReceiverInterface;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.sys.funclib.datenkatalog.bind.Context;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

/**
 * Empfängt einen Onlinedatensatz eines bestimmten Typs und verteilt den Datensatz weiter.
 *
 * <p>Um die Verteilung der Datensätze durch die Datenverteiler-Applikationsfunktionen nicht zu blockieren, werden die
 * empfangenen Datensätze in einer Warteschlange eingereiht und in einem eigenen Thread weiterverarbeitet.</p>
 *
 * @author Falko Schumann
 * @param <T> der Typ der empfangenen Daten.
 * @since 1.2
 */
public class Empfaenger<T> implements ClientReceiverInterface {

    private final List<Consumer<Datensatz<T>>> consumer = new CopyOnWriteArrayList<>();
    private final BlockingQueue<ResultData> warteschlange = new LinkedBlockingQueue<>();

    private final Context context;
    private final Class<T> clazz;

    public Empfaenger(Context context, Class<T> clazz) {
        this.context = context;
        this.clazz = clazz;

        // TODO Mehrere Threads in einem Threadpool nutzen?
        Thread t = new Thread(this::veroeffentlicheNeueDatensaetze, "Empfänger für Daten vom Typ " + clazz.getName());
        t.setDaemon(true);
        t.start();
    }

    public void connectConsumer(Consumer<Datensatz<T>> c) {
        consumer.add(c);
    }

    public void disconnectConsumer(Consumer<Datensatz<T>> c) {
        consumer.remove(c);
    }

    @Override
    public void update(ResultData[] results) {
        warteschlange.addAll(Arrays.asList(results));
    }

    private void veroeffentlicheNeueDatensaetze() {
        while (true) {
            try {
                ResultData rd = warteschlange.take();
                consumer.stream().forEach(c -> veroeffentlicheDatensatz(c, rd));
            } catch (InterruptedException ex) {
                break;
            }
        }
    }

    private void veroeffentlicheDatensatz(Consumer<Datensatz<T>> c, ResultData rd) {
        T datum = context.createUnmarshaller().unmarshal(rd.getData(), clazz);
        LocalDateTime zeitstempel = LocalDateTime.ofInstant(Instant.ofEpochMilli(rd.getDataTime()), ZoneId.systemDefault());
        Datensatz<T> datensatz = Datensatz.of(rd.getObject(), datum, rd.getDataDescription().getAspect(), zeitstempel);
        c.accept(datensatz);
    }

}
