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

import static org.mockito.Mockito.mock;

public class CodeBeispieleInDokumentationIT extends AbstractDatenkatalogIT {

    @Test
    public void testUsage_Empfaenger() {
        Datenverteiler dav = mock(Datenverteiler.class);
        Aspect aspekt = dav.aspekt("asp.messWertErsetzung");
        SystemObject umfelddatensensor = dav.objekt("ufds.helligkeit");
        dav.anmeldenAlsEmpfaenger(this::verarbeiteDatensatz, UfdsHelligkeit.class, aspekt, umfelddatensensor);
    }

    private void verarbeiteDatensatz(Datensatz<UfdsHelligkeit> datensatz) {
        // ...
    }

    @Test
    public void testUsage_Parameter() {
        Datenverteiler dav = mock(Datenverteiler.class);
        SystemObject umfelddatensensor = dav.objekt("ufds.helligkeit");
        dav.anmeldenAufParameter(d -> System.out.println(d), UfdsHelligkeitFuzzy.class, umfelddatensensor);
        // ... oder den Parameter direkt abrufen mit ...
        Datensatz<UfdsHelligkeitFuzzy> datensatz = dav.parameter(UfdsHelligkeitFuzzy.class, umfelddatensensor);
    }

    @Test
    public void testUsage_Konfiguration() {
        Datenverteiler dav = mock(Datenverteiler.class);
        SystemObject messquerschnitt = dav.objekt("mq.a10.1");
        MessQuerschnittAllgemein datum = dav.konfiguration(MessQuerschnittAllgemein.class, messquerschnitt);
    }

    @Test
    public void testUsage_DatenSenden() {
        Datenverteiler dav = mock(Datenverteiler.class);
        SystemObject stau = dav.objekt("stau.1");
        StauVerlauf datum = new StauVerlauf();
        datum.setDauer(Duration.ofMinutes(30));
        // datum.set...
        Aspect aspekt = dav.aspekt("asp.prognoseNormal");
        dav.anmeldenAlsQuelle(StauVerlauf.class, aspekt, stau);
        dav.sendeDatensatz(Datensatz.of(stau, datum, aspekt));
    }

    private class UfdsHelligkeitFuzzy {

        // Stub

    }

}
