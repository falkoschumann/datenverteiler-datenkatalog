/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

/**
 * Abstrakte Fabrik zum Erzeugen von Objekten des Metamodells.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class MetamodellFabrik {

    public Konfigurationsbereich erzeugeKonfigurationsbereich(String pid) {
        return new Konfigurationsbereich(pid);
    }

    public Konfigurationsverantwortlicher erzeugeKonfigurationsverantwortlicher(String pid) {
        return new Konfigurationsverantwortlicher(pid);
    }

    public Typ erzeugeTyp(String pid) {
        return new Typ(pid);
    }

    public DynamischerTyp erzeugeDynamischerTyp(String pid) {
        return new DynamischerTyp(pid);
    }

    public Mengentyp erzeugeMengentyp(String pid) {
        return new Mengentyp(pid);
    }

    public Attributgruppe erzeugeAttributgruppe(String pid) {
        return new Attributgruppe(pid);
    }

    public Attributgruppenverwendung erzeugeAttributgruppenverwendung(String pid) {
        return new Attributgruppenverwendung(pid);
    }

    public Aspekt erzeugeAspekt(String pid) {
        return new Aspekt(pid);
    }

    public Attributliste erzeugeAttributliste(String pid) {
        return new Attributliste(pid);
    }

    public ZeichenkettenAttributtyp erzeugeZeichenkettenAttributtyp(String pid) {
        return new ZeichenkettenAttributtyp(pid);
    }

    public ZeitstempelAttributtyp erzeugeZeitstempelAttributtyp(String pid) {
        return new ZeitstempelAttributtyp(pid);
    }

    public KommazahlAttributtyp erzeugeKommazahlAttributtyp(String pid) {
        return new KommazahlAttributtyp(pid);
    }

    public ObjektreferenzAttributtyp erzeugeObjektreferenzAttributtyp(String pid) {
        return new ObjektreferenzAttributtyp(pid);
    }

    public GanzzahlAttributtyp erzeugeGanzzahlAttributtyp(String pid) {
        return new GanzzahlAttributtyp(pid);
    }

}
