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
import java.util.*;
import java.util.stream.Collectors;

/**
 * Der Generator erzeugt eine HTML-Dokumentation des Datenkatalogs nach dem Vorbild von JavaDoc.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class HtmlGenerator {

    // TODO Attributlisten generieren
    // TODO Kopfzeile für rechten Frame erweitern: Verwendung, Baum, Index, Hilfe

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
    private Map<KonfigurationsVerantwortlicher, List<KonfigurationsBereich>> verantwortlichkeiten;

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

        result.put(PROP_PROJEKT, "Datenkatalog");

        List<KonfigurationsVerantwortlicher> konfigurationsVerantwortliche = new ArrayList<>();
        konfigurationsVerantwortliche.addAll(metamodell.getKonfigurationsverantwortliche());
        konfigurationsVerantwortliche.sort(SystemObjekt::compareToNameOderPid);
        result.put(PROP_KONFIGURATIONSVERANTWORTLICHE, konfigurationsVerantwortliche);

        List<KonfigurationsBereich> konfigurationsBereiche = new ArrayList<>();
        konfigurationsBereiche.addAll(metamodell.getKonfigurationsbereiche());
        konfigurationsBereiche.sort(SystemObjekt::compareToNameOderPid);
        result.put(PROP_KONFIGURATIONSBEREICHE, konfigurationsBereiche);

        verantwortlichkeiten = new LinkedHashMap<>();
        konfigurationsVerantwortliche.forEach(kv -> verantwortlichkeiten.put(kv, new ArrayList<>()));
        konfigurationsBereiche.forEach(kb -> verantwortlichkeiten.get(kb.getZustaendiger()).add(kb));
        result.put(PROP_VERANTWORTLICHKEITEN, verantwortlichkeiten);

        List<SystemObjekt> objekte = new ArrayList<>();
        objekte.addAll(metamodell.getKonfigurationsbereiche().stream().map(KonfigurationsBereich::getAlleObjekte).flatMap(Collection::stream).collect(Collectors.toSet()));
        objekte.sort(SystemObjekt::compareToNameOderPid);
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
        List<SystemObjekt> konfigurationsverantwortliche = (List<SystemObjekt>) context.get(PROP_KONFIGURATIONSVERANTWORTLICHE);
        for (SystemObjekt e : konfigurationsverantwortliche) {
            generiereKonfigurationsverantwortlicherUeberblick((KonfigurationsVerantwortlicher) e);
        }
    }

    private void generiereKonfigurationsverantwortlicherUeberblick(KonfigurationsVerantwortlicher konfigurationsVerantwortlicher) throws IOException {
        context.put(PROP_KONFIGURATIONSVERANTWORTLICHER, konfigurationsVerantwortlicher);
        context.put(PROP_KONFIGURATIONSBEREICHE, verantwortlichkeiten.get(konfigurationsVerantwortlicher));

        String pfad = konfigurationsVerantwortlicher.getPid();
        Files.createDirectories(Paths.get(TARGET, pfad));
        generiereDatei("konfigurationsverantwortlicher-ueberblick", pfad + "/konfigurationsverantwortlicher-ueberblick");
    }

    private void generiereKonfigurationsbereiche() throws IOException {
        List<SystemObjekt> konfigurationsbereiche = (List<SystemObjekt>) context.get(PROP_KONFIGURATIONSBEREICHE);
        for (SystemObjekt e : konfigurationsbereiche) {
            context.put(PROP_KONFIGURATIONSBEREICH, e);
            generiereKonfigurationsbereichUeberblick((KonfigurationsBereich) e);
            generiereKonfigurationsbereichFrame((KonfigurationsBereich) e);
        }
    }

    private void generiereKonfigurationsbereichUeberblick(KonfigurationsBereich konfigurationsBereich) throws IOException {
        String pfad = konfigurationsBereich.getZustaendiger().getPid() + "/" + konfigurationsBereich.getPid();
        Files.createDirectories(Paths.get(TARGET, pfad));
        generiereDatei("konfigurationsbereich-ueberblick", pfad + "/konfigurationsbereich-ueberblick");
    }

    private void generiereKonfigurationsbereichFrame(KonfigurationsBereich konfigurationsBereich) throws IOException {
        String pfad = konfigurationsBereich.getZustaendiger().getPid() + "/" + konfigurationsBereich.getPid();
        Files.createDirectories(Paths.get(TARGET, pfad));
        generiereDatei("konfigurationsbereich-frame", pfad + "/konfigurationsbereich-frame");
    }

    private void generiereObjekte() throws IOException {
        List<SystemObjekt> objekte = (List<SystemObjekt>) context.get(PROP_OBJEKTE);
        for (SystemObjekt e : objekte)
            generiereObjekt(e);
    }

    private void generiereObjekt(SystemObjekt systemObjekt) throws IOException {
        String pfad = systemObjekt.getKonfigurationsBereich().getZustaendiger().getPid() + "/" + systemObjekt.getKonfigurationsBereich().getPid();
        Files.createDirectories(Paths.get(TARGET, pfad));
        if (systemObjekt instanceof MengenTyp) {
            context.put(PROP_MENGENTYP, systemObjekt);
            generiereDatei(PROP_MENGENTYP, pfad + "/" + systemObjekt.getPid());
        } else if (systemObjekt instanceof Typ) {
            context.put(PROP_TYP, systemObjekt);
            generiereDatei(PROP_TYP, pfad + "/" + systemObjekt.getPid());
        } else if (systemObjekt instanceof Attributgruppe) {
            context.put(PROP_ATTRIBUTGRUPPE, systemObjekt);
            generiereDatei(PROP_ATTRIBUTGRUPPE, pfad + "/" + systemObjekt.getPid());
        } else if (systemObjekt instanceof AttributListenDefinition) {
            context.put(PROP_ATTRIBUTLISTE, systemObjekt);
            generiereDatei(PROP_ATTRIBUTLISTE, pfad + "/" + systemObjekt.getPid());
        }
    }

}
