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
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Der Generator erzeugt eine HTML-Dokumentation des Datenkatalogs nach dem Vorbild von JavaDoc.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class HtmlGenerator {

    // TODO Kopfzeile für rechten Frame (Frame und No-Frame)
    // TODO Kopfzeile für rechten Frame (Verwendung, Baum, Index, Hilfe)
    // TODO Überblicksseite für Konfigurationsverantwortlichen mit Liste der Konfigurationsbereichen

    public static final String PROP_KONFIGURATIONSBEREICH = "konfigurationsbereich";
    public static final String PROP_KONFIGURATIONSBEREICHE = "konfigurationsbereiche";
    public static final String PROP_OBJEKTE = "objekte";
    public static final String PROP_TYP = "typ";
    public static final String PROP_MENGENTYP = "mengentyp";
    public static final String PROP_PROJEKT = "projekt";

    private static final String SOURCE = "/generator/html/";
    private static final String TARGET = "target/datenkatalog/html/";

    private VelocityContext context;

    static {
        Velocity.setProperty("resource.loader", "class");
        Velocity.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        try {
            Velocity.init();
        } catch (Exception ex) {
            throw new IllegalStateException("Fehler beim Initialisieren der Template-Engine.", ex);
        }
    }

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
        generiereObjekte();
    }

    private VelocityContext erzeugeContext(Metamodell metamodell) {
        VelocityContext result = new VelocityContext();

        result.put(PROP_PROJEKT, "Datenkatalog");

        SortedSet<KonfigurationsBereich> konfigurationsBereiche = new TreeSet<>();
        konfigurationsBereiche.addAll(metamodell.getKonfigurationsbereiche());
        result.put(PROP_KONFIGURATIONSBEREICHE, konfigurationsBereiche);

        SortedSet<SystemObjekt> typen = new TreeSet<>();
        typen.addAll(metamodell.getKonfigurationsbereiche().stream().map(KonfigurationsBereich::getTypen).flatMap(Collection::stream).collect(Collectors.toSet()));

        SortedSet<SystemObjekt> mengen = new TreeSet<>();
        mengen.addAll(metamodell.getKonfigurationsbereiche().stream().map(KonfigurationsBereich::getMengen).flatMap(Collection::stream).collect(Collectors.toSet()));

        SortedSet<SystemObjekt> objekte = new TreeSet<>();
        objekte.addAll(typen);
        objekte.addAll(mengen);
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
        OutputStream out = Files.newOutputStream(Paths.get(TARGET, zieldateiname + ".html"));
        try (Writer writer = new OutputStreamWriter(out, "UTF-8")) {
            try {
                Velocity.mergeTemplate(SOURCE + template + ".vm", "UTF-8", context, writer);
            } catch (Exception ex) {
                throw new IllegalStateException("Unreachable code. Fehler beim Erzeugen der Datei " + zieldateiname + ".html mit dem Template " + template + ".vm.", ex);
            }
        }
    }

    private void generiereKonfigurationsbereiche() throws IOException {
        Set<SystemObjekt> konfigurationsbereiche = (Set<SystemObjekt>) context.get(PROP_KONFIGURATIONSBEREICHE);
        for (SystemObjekt e : konfigurationsbereiche) {
            generiereKonfigurationsbereichUeberblick((KonfigurationsBereich) e);
            generiereKonfigurationsbereichFrame((KonfigurationsBereich) e);
        }
    }

    private void generiereKonfigurationsbereichUeberblick(KonfigurationsBereich konfigurationsBereich) throws IOException {
        context.put(PROP_KONFIGURATIONSBEREICH, konfigurationsBereich);
        String pfad = konfigurationsBereich.getZustaendiger().getPid() + "/" + konfigurationsBereich.getPid() + "/";
        Files.createDirectories(Paths.get(TARGET, pfad));
        generiereDatei("konfigurationsbereich-ueberblick", pfad + "konfigurationsbereich-ueberblick");
    }

    private void generiereKonfigurationsbereichFrame(KonfigurationsBereich konfigurationsBereich) throws IOException {
        context.put(PROP_KONFIGURATIONSBEREICH, konfigurationsBereich);
        String pfad = konfigurationsBereich.getZustaendiger().getPid() + "/" + konfigurationsBereich.getPid() + "/";
        Files.createDirectories(Paths.get(TARGET, pfad));
        generiereDatei("konfigurationsbereich-frame", pfad + "konfigurationsbereich-frame");
    }

    private void generiereObjekte() throws IOException {
        Set<SystemObjekt> objekte = (Set<SystemObjekt>) context.get(PROP_OBJEKTE);
        for (SystemObjekt e : objekte)
            generiereObjekt(e);
    }

    private void generiereObjekt(SystemObjekt systemObjekt) throws IOException {
        String pfad = systemObjekt.getBereich().getZustaendiger().getPid() + "/" + systemObjekt.getBereich().getPid() + "/";
        Files.createDirectories(Paths.get(TARGET, pfad));
        if (systemObjekt instanceof MengenTyp) {
            context.put(PROP_MENGENTYP, systemObjekt);
            generiereDatei(PROP_MENGENTYP, pfad + systemObjekt.getPid());
        } else if (systemObjekt instanceof Typ) {
            context.put(PROP_TYP, systemObjekt);
            generiereDatei(PROP_TYP, pfad + systemObjekt.getPid());
        }
    }

}
