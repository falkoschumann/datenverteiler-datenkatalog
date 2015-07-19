/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.datenverteiler;

import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.datenkatalog.AbstractDatenkatalogIT;
import de.bsvrz.sys.funclib.datenkatalog.modell.MessQuerschnittAllgemein;
import de.bsvrz.sys.funclib.datenkatalog.modell.StauVerlauf;
import de.bsvrz.sys.funclib.datenkatalog.modell.UfdsHelligkeit;
import org.junit.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.Set;

import static org.mockito.Mockito.mock;

public class CodeBeispieleInDokumentationIT extends AbstractDatenkatalogIT {

    @Test
    public void testUsage_Empfaenger() {
        Datenverteiler dav = mock(Datenverteiler.class);
        Aspect aspekt = dav.getAspekt("asp.messWertErsetzung");
        SystemObject umfelddatensensor = dav.getObjekt("ufds.helligkeit");
        dav.anmeldenAlsEmpfaenger(this::verarbeiteDatensatz, Collections.singleton(umfelddatensensor), UfdsHelligkeit.class, aspekt, Empfaengeroption.NORMAL);
    }

    private void verarbeiteDatensatz(Datensatz<UfdsHelligkeit> datensatz) {
        // ...
    }

    @Test
    public void testUsage_Parameter() {
        Datenverteiler dav = mock(Datenverteiler.class);
        SystemObject umfelddatensensor = dav.getObjekt("ufds.helligkeit");
        dav.anmeldenAufParameter(d -> System.out.println(d), Collections.singleton(umfelddatensensor), UfdsHelligkeitFuzzy.class);
        // ... oder den Parameter direkt abrufen mit ...
        Datensatz<UfdsHelligkeitFuzzy> datensatz = dav.getParameter(umfelddatensensor, UfdsHelligkeitFuzzy.class);
    }

    @Test
    public void testUsage_Konfiguration() {
        Datenverteiler dav = mock(Datenverteiler.class);
        SystemObject messquerschnitt = dav.getObjekt("mq.a10.1");
        MessQuerschnittAllgemein datum = dav.getKonfiguration(messquerschnitt, MessQuerschnittAllgemein.class);
    }

    @Test
    public void testUsage_DatenSenden() {
        Datenverteiler dav = mock(Datenverteiler.class);
        SystemObject stau = dav.getObjekt("stau.1");
        StauVerlauf datum = new StauVerlauf();
        datum.setDauer(Duration.ofMinutes(30));
        // datum.set...
        Aspect aspekt = dav.getAspekt("asp.prognoseNormal");
        dav.anmeldenAlsQuelle(Collections.singleton(stau), StauVerlauf.class, aspekt);
        dav.sendeDatensatz(Datensatz.of(stau, datum, aspekt));
        // ... oder als Collection ...
        Set<Datensatz<?>> datensaetze = Collections.singleton(Datensatz.of(stau, datum, aspekt));
        dav.sendeDatensaetze(datensaetze);
    }

    private class UfdsHelligkeitFuzzy {

        // Stub

    }

}
