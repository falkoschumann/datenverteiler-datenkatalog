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
import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
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

    private String source = "/generator/html/";
    private String target = "target/datenkatalog/html/";

    private VelocityContext context;

    public static void main(String args[]) throws IOException {
        initialisiereVelocity();

        ConfigDataModel model = new ConfigDataModel(new File("src/test/konfiguration/verwaltungsdaten.xml"));
        Metamodell metamodell = new Metamodell(new MetamodellFabrik(), model);
        try {
            new HtmlGenerator().generiere(metamodell);
        } finally {
            model.close();
        }
    }

    private static void initialisiereVelocity() {
        Velocity.setProperty("resource.loader", "class");
        Velocity.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        try {
            Velocity.init();
        } catch (Exception ex) {
            throw new IllegalStateException("Fehler beim Initialisieren der Template-Engine.", ex);
        }
    }

    public void generiere(Metamodell metamodell) throws IOException {
        context = erzeugeContext(metamodell);

        Files.createDirectories(Paths.get(target));
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

        result.put("projekt", "Datenkatalog");
        result.put("konfigurationsverantwortliche", metamodell.gibKonfigurationsverantwortliche());
        result.put("konfigurationsbereiche", metamodell.gibKonfigurationsbereiche());

        verantwortlichkeiten = new TreeMap<>(Systemobjekt::compareToNameOderPid);
        metamodell.gibKonfigurationsverantwortliche().forEach(kv -> verantwortlichkeiten.put(kv, new TreeSet<>(Systemobjekt::compareToNameOderPid)));
        metamodell.gibKonfigurationsbereiche().forEach(kb -> verantwortlichkeiten.get(kb.getZustaendiger()).add(kb));
        result.put("verantwortlichkeiten", verantwortlichkeiten);

        Set<Systemobjekt> objekte = new TreeSet<>(Systemobjekt::compareToNameOderPid);
        objekte.addAll(metamodell.gibKonfigurationsbereiche().stream().map(Konfigurationsbereich::getModell).flatMap(Collection::stream).collect(Collectors.toSet()));
        result.put("objekte", objekte);

        return result;
    }

    private void kopiereStatischeDateien() throws IOException {
        Files.copy(getClass().getResourceAsStream(source + "stylesheet.css"), Paths.get(target, "stylesheet.css"), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(getClass().getResourceAsStream(source + "index.html"), Paths.get(target, "index.html"), StandardCopyOption.REPLACE_EXISTING);
    }

    private void generiereDatei(String name) throws IOException {
        generiereDatei(name, name);
    }

    private void generiereDatei(String template, String zieldateiname) throws IOException {
        Path datei = Paths.get(target, zieldateiname + ".html");
        System.out.println("Generiere " + datei + " ...");
        OutputStream out = Files.newOutputStream(datei);
        try (Writer writer = new OutputStreamWriter(out, "UTF-8")) {
            try {
                Velocity.mergeTemplate(source + template + ".vm", "UTF-8", context, writer);
            } catch (Exception ex) {
                throw new IllegalStateException("Unreachable code. Fehler beim Erzeugen der Datei " + zieldateiname + ".html mit dem Template " + template + ".vm.", ex);
            }
        }
    }

    private void generiereKonfigurationsverantwortliche() throws IOException {
        Set<Systemobjekt> konfigurationsverantwortliche = (Set<Systemobjekt>) context.get("konfigurationsverantwortliche");
        for (Systemobjekt e : konfigurationsverantwortliche) {
            generiereKonfigurationsverantwortlicherUeberblick((Konfigurationsverantwortlicher) e);
        }
    }

    private void generiereKonfigurationsverantwortlicherUeberblick(Konfigurationsverantwortlicher konfigurationsVerantwortlicher) throws IOException {
        context.put("konfigurationsverantwortlicher", konfigurationsVerantwortlicher);
        context.put("konfigurationsbereiche", verantwortlichkeiten.get(konfigurationsVerantwortlicher));

        String pfad = konfigurationsVerantwortlicher.getPid();
        Files.createDirectories(Paths.get(target, pfad));
        generiereDatei("konfigurationsverantwortlicher-ueberblick", pfad + "/konfigurationsverantwortlicher-ueberblick");
    }

    private void generiereKonfigurationsbereiche() throws IOException {
        Set<Systemobjekt> konfigurationsbereiche = (Set<Systemobjekt>) context.get("konfigurationsbereiche");
        for (Systemobjekt e : konfigurationsbereiche) {
            context.put("konfigurationsbereich", e);
            generiereKonfigurationsbereichUeberblick((Konfigurationsbereich) e);
            generiereKonfigurationsbereichFrame((Konfigurationsbereich) e);
        }
    }

    private void generiereKonfigurationsbereichUeberblick(Konfigurationsbereich konfigurationsBereich) throws IOException {
        String pfad = konfigurationsBereich.getZustaendiger().getPid() + "/" + konfigurationsBereich.getPid();
        Files.createDirectories(Paths.get(target, pfad));
        generiereDatei("konfigurationsbereich-ueberblick", pfad + "/konfigurationsbereich-ueberblick");
    }

    private void generiereKonfigurationsbereichFrame(Konfigurationsbereich konfigurationsBereich) throws IOException {
        String pfad = konfigurationsBereich.getZustaendiger().getPid() + "/" + konfigurationsBereich.getPid();
        Files.createDirectories(Paths.get(target, pfad));
        generiereDatei("konfigurationsbereich-frame", pfad + "/konfigurationsbereich-frame");
    }

    private void generiereObjekte() throws IOException {
        Set<Systemobjekt> objekte = (Set<Systemobjekt>) context.get("objekte");
        for (Systemobjekt e : objekte)
            generiereObjekt(e);
    }

    private void generiereObjekt(Systemobjekt systemObjekt) throws IOException {
        String pfad = systemObjekt.getKonfigurationsbereich().getZustaendiger().getPid() + "/" + systemObjekt.getKonfigurationsbereich().getPid();
        Files.createDirectories(Paths.get(target, pfad));
        if (systemObjekt instanceof Mengentyp) {
            context.put("mengentyp", systemObjekt);
            generiereDatei("mengentyp", pfad + "/" + systemObjekt.getPid());
        } else if (systemObjekt instanceof Typ) {
            context.put("typ", systemObjekt);
            generiereDatei("typ", pfad + "/" + systemObjekt.getPid());
        } else if (systemObjekt instanceof Attributgruppe) {
            context.put("attributgruppe", systemObjekt);
            generiereDatei("attributgruppe", pfad + "/" + systemObjekt.getPid());
        } else if (systemObjekt instanceof Attributliste) {
            context.put("attributliste", systemObjekt);
            generiereDatei("attributliste", pfad + "/" + systemObjekt.getPid());
        } else if (systemObjekt instanceof ZeichenkettenAttributtyp) {
            context.put("attributtyp", systemObjekt);
            generiereDatei("zeichenkette", pfad + "/" + systemObjekt.getPid());
        } else if (systemObjekt instanceof ZeitstempelAttributtyp) {
            context.put("attributtyp", systemObjekt);
            generiereDatei("zeitstempel", pfad + "/" + systemObjekt.getPid());
        } else if (systemObjekt instanceof KommazahlAttributtyp) {
            context.put("attributtyp", systemObjekt);
            generiereDatei("kommazahl", pfad + "/" + systemObjekt.getPid());
        } else if (systemObjekt instanceof ObjektreferenzAttributtyp) {
            context.put("attributtyp", systemObjekt);
            generiereDatei("objektreferenz", pfad + "/" + systemObjekt.getPid());
        } else if (systemObjekt instanceof GanzzahlAttributtyp) {
            context.put("attributtyp", systemObjekt);
            generiereDatei("ganzzahl", pfad + "/" + systemObjekt.getPid());
        }
    }

}
