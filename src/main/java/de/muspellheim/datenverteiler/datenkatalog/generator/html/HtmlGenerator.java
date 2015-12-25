/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.generator.html;

import de.bsvrz.puk.config.configFile.datamodel.ConfigDataModel;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.*;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Der Generator erzeugt eine HTML-Dokumentation des Datenkatalogs nach dem Vorbild von JavaDoc.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class HtmlGenerator {

    // TODO Kopfzeile für rechten Frame erweitern: Verwendung, Baum, Index, Hilfe
    // TODO Proxyobjekte anlegen für Sortierung und Spezialisierung

    public static final String PROP_PROJEKT = "projekt";
    public static final String PROP_VERANTWORTLICHKEITEN = "verantwortlichkeiten";
    public static final String PROP_KONFIGURATIONSVERANTWORTLICHE = "konfigurationsverantwortliche";
    public static final String PROP_KONFIGURATIONSVERANTWORTLICHER = "konfigurationsverantwortlicher";
    public static final String PROP_KONFIGURATIONSBEREICH = "konfigurationsbereich";
    public static final String PROP_KONFIGURATIONSBEREICHE = "konfigurationsbereiche";
    public static final String PROP_OBJEKTE = "objekte";
    public static final String PROP_TYP = "typ";
    public static final String PROP_MENGENTYP = "mengentyp";
    public static final String PROP_ATTRIBUTGRUPPE = "attributgruppe";
    public static final String PROP_ATTRIBUTLISTE = "attributliste";
    public static final String PROP_ATTRIBUTTYP = "attributtyp";
    public static final String PROP_ZEICHENKETTE = "zeichenkette";
    public static final String PROP_ZEITSTEMPEL = "zeitstempel";
    public static final String PROP_KOMMAZAHL = "kommazahl";
    public static final String PROP_OBJEKTREFERENZ = "objektreferenz";
    public static final String PROP_GANZZAHL = "ganzzahl";

    private static final String SOURCE = "/generator/html/";
    private static final String TARGET = "target/datenkatalog/html/";

    static {
        Velocity.setProperty("resource.loader", "class");
        Velocity.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        try {
            Velocity.init();
        } catch (Exception ex) {
            throw new IllegalStateException("Fehler beim Initialisieren der Template-Engine.", ex);
        }
    }

    private VelocityContext context;
    private Map<Konfigurationsverantwortlicher, List<Konfigurationsbereich>> verantwortlichkeiten;

    public static void main(String args[]) throws IOException {
        ConfigDataModel model = new ConfigDataModel(new File("src/test/konfiguration/verwaltungsdaten.xml"));
        Metamodell metamodell = new Metamodell(model);
        try {
            new HtmlGenerator().generiere(metamodell);
        } finally {
            model.close();
        }
    }

    public void generiere(Metamodell metamodell) throws IOException {
        context = erzeugeContext(metamodell);

        Files.createDirectories(Paths.get(TARGET));
        kopiereStatischeDateien();
        generiereDatei("ueberblick");
        generiereDatei("ueberblick-frame");
        generiereDatei("alleobjekte-frame");
        generiereKonfigurationsbereiche();
        generiereKonfigurationsverantwortliche();
        generiereObjekte();
    }

    private VelocityContext erzeugeContext(Metamodell metamodell) {
        VelocityContext result = new VelocityContext();

        result.put(NumberFormat.class.getSimpleName(), NumberFormat.class);

        result.put(PROP_PROJEKT, "Datenkatalog");

        List<Konfigurationsverantwortlicher> konfigurationsVerantwortliche = new ArrayList<>();
        konfigurationsVerantwortliche.addAll(metamodell.getKonfigurationsverantwortliche());
        konfigurationsVerantwortliche.sort(Systemobjekt::compareToNameOderPid);
        result.put(PROP_KONFIGURATIONSVERANTWORTLICHE, konfigurationsVerantwortliche);

        List<Konfigurationsbereich> konfigurationsBereiche = new ArrayList<>();
        konfigurationsBereiche.addAll(metamodell.getKonfigurationsbereiche());
        konfigurationsBereiche.sort(Systemobjekt::compareToNameOderPid);
        result.put(PROP_KONFIGURATIONSBEREICHE, konfigurationsBereiche);

        verantwortlichkeiten = new LinkedHashMap<>();
        konfigurationsVerantwortliche.forEach(kv -> verantwortlichkeiten.put(kv, new ArrayList<>()));
        konfigurationsBereiche.forEach(kb -> verantwortlichkeiten.get(kb.getZustaendiger()).add(kb));
        result.put(PROP_VERANTWORTLICHKEITEN, verantwortlichkeiten);

        List<Systemobjekt> objekte = new ArrayList<>();
        objekte.addAll(metamodell.getKonfigurationsbereiche().stream().map(Konfigurationsbereich::getModell).flatMap(Collection::stream).collect(Collectors.toSet()));
        objekte.sort(Systemobjekt::compareToNameOderPid);
        result.put(PROP_OBJEKTE, objekte);

        return result;
    }

    private void kopiereStatischeDateien() throws IOException {
        Files.copy(getClass().getResourceAsStream(SOURCE + "stylesheet.css"), Paths.get(TARGET, "stylesheet.css"), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(getClass().getResourceAsStream(SOURCE + "index.html"), Paths.get(TARGET, "index.html"), StandardCopyOption.REPLACE_EXISTING);
    }

    private void generiereDatei(String name) throws IOException {
        generiereDatei(name, name);
    }

    private void generiereDatei(String template, String zieldateiname) throws IOException {
        Path datei = Paths.get(TARGET, zieldateiname + ".html");
        System.out.println("Generiere " + datei + " ...");
        OutputStream out = Files.newOutputStream(datei);
        try (Writer writer = new OutputStreamWriter(out, "UTF-8")) {
            try {
                Velocity.mergeTemplate(SOURCE + template + ".vm", "UTF-8", context, writer);
            } catch (Exception ex) {
                throw new IllegalStateException("Unreachable code. Fehler beim Erzeugen der Datei " + zieldateiname + ".html mit dem Template " + template + ".vm.", ex);
            }
        }
    }

    private void generiereKonfigurationsverantwortliche() throws IOException {
        List<Systemobjekt> konfigurationsverantwortliche = (List<Systemobjekt>) context.get(PROP_KONFIGURATIONSVERANTWORTLICHE);
        for (Systemobjekt e : konfigurationsverantwortliche) {
            generiereKonfigurationsverantwortlicherUeberblick((Konfigurationsverantwortlicher) e);
        }
    }

    private void generiereKonfigurationsverantwortlicherUeberblick(Konfigurationsverantwortlicher konfigurationsVerantwortlicher) throws IOException {
        context.put(PROP_KONFIGURATIONSVERANTWORTLICHER, konfigurationsVerantwortlicher);
        context.put(PROP_KONFIGURATIONSBEREICHE, verantwortlichkeiten.get(konfigurationsVerantwortlicher));

        String pfad = konfigurationsVerantwortlicher.getPid();
        Files.createDirectories(Paths.get(TARGET, pfad));
        generiereDatei("konfigurationsverantwortlicher-ueberblick", pfad + "/konfigurationsverantwortlicher-ueberblick");
    }

    private void generiereKonfigurationsbereiche() throws IOException {
        List<Systemobjekt> konfigurationsbereiche = (List<Systemobjekt>) context.get(PROP_KONFIGURATIONSBEREICHE);
        for (Systemobjekt e : konfigurationsbereiche) {
            context.put(PROP_KONFIGURATIONSBEREICH, e);
            generiereKonfigurationsbereichUeberblick((Konfigurationsbereich) e);
            generiereKonfigurationsbereichFrame((Konfigurationsbereich) e);
        }
    }

    private void generiereKonfigurationsbereichUeberblick(Konfigurationsbereich konfigurationsBereich) throws IOException {
        String pfad = konfigurationsBereich.getZustaendiger().getPid() + "/" + konfigurationsBereich.getPid();
        Files.createDirectories(Paths.get(TARGET, pfad));
        generiereDatei("konfigurationsbereich-ueberblick", pfad + "/konfigurationsbereich-ueberblick");
    }

    private void generiereKonfigurationsbereichFrame(Konfigurationsbereich konfigurationsBereich) throws IOException {
        String pfad = konfigurationsBereich.getZustaendiger().getPid() + "/" + konfigurationsBereich.getPid();
        Files.createDirectories(Paths.get(TARGET, pfad));
        generiereDatei("konfigurationsbereich-frame", pfad + "/konfigurationsbereich-frame");
    }

    private void generiereObjekte() throws IOException {
        List<Systemobjekt> objekte = (List<Systemobjekt>) context.get(PROP_OBJEKTE);
        for (Systemobjekt e : objekte)
            generiereObjekt(e);
    }

    private void generiereObjekt(Systemobjekt systemObjekt) throws IOException {
        String pfad = systemObjekt.getKonfigurationsbereich().getZustaendiger().getPid() + "/" + systemObjekt.getKonfigurationsbereich().getPid();
        Files.createDirectories(Paths.get(TARGET, pfad));
        if (systemObjekt instanceof Mengentyp) {
            context.put(PROP_MENGENTYP, systemObjekt);
            generiereDatei(PROP_MENGENTYP, pfad + "/" + systemObjekt.getPid());
        } else if (systemObjekt instanceof Typ) {
            context.put(PROP_TYP, systemObjekt);
            generiereDatei(PROP_TYP, pfad + "/" + systemObjekt.getPid());
        } else if (systemObjekt instanceof Attributgruppe) {
            context.put(PROP_ATTRIBUTGRUPPE, systemObjekt);
            generiereDatei(PROP_ATTRIBUTGRUPPE, pfad + "/" + systemObjekt.getPid());
        } else if (systemObjekt instanceof Attributliste) {
            context.put(PROP_ATTRIBUTLISTE, systemObjekt);
            generiereDatei(PROP_ATTRIBUTLISTE, pfad + "/" + systemObjekt.getPid());
        } else if (systemObjekt instanceof ZeichenkettenAttributtyp) {
            context.put(PROP_ATTRIBUTTYP, systemObjekt);
            generiereDatei(PROP_ZEICHENKETTE, pfad + "/" + systemObjekt.getPid());
        } else if (systemObjekt instanceof ZeitstempelAttributtyp) {
            context.put(PROP_ATTRIBUTTYP, systemObjekt);
            generiereDatei(PROP_ZEITSTEMPEL, pfad + "/" + systemObjekt.getPid());
        } else if (systemObjekt instanceof KommazahlAttributtyp) {
            context.put(PROP_ATTRIBUTTYP, systemObjekt);
            generiereDatei(PROP_KOMMAZAHL, pfad + "/" + systemObjekt.getPid());
        } else if (systemObjekt instanceof ObjektreferenzAttributtyp) {
            context.put(PROP_ATTRIBUTTYP, systemObjekt);
            generiereDatei(PROP_OBJEKTREFERENZ, pfad + "/" + systemObjekt.getPid());
        } else if (systemObjekt instanceof GanzzahlAttributtyp) {
            context.put(PROP_ATTRIBUTTYP, systemObjekt);
            generiereDatei(PROP_GANZZAHL, pfad + "/" + systemObjekt.getPid());
        }
    }

}
