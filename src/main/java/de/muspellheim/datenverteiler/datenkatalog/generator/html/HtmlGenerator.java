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

/**
 * Der Generator erzeugt eine HTML-Dokumentation des Datenkatalogs nach dem Vorbild von JavaDoc.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class HtmlGenerator {

    // TODO Kopfzeile für rechten Frame erweitern: Verwendung, Baum, Index, Hilfe
    // TODO Proxyobjekte anlegen für Sortierung und Spezialisierung
    // TODO Aspekt generieren

    private String source = "/generator/html/";
    private String target = "target/datenkatalog/html/";

    private MetamodellHtmlProxy metamodell;
    private VelocityContext context;

    public static void main(String args[]) throws IOException {
        initialisiereVelocity();

        ConfigDataModel model = new ConfigDataModel(new File("src/test/konfiguration/verwaltungsdaten.xml"));
        MetamodellHtmlProxy metamodell = new MetamodellHtmlProxy(model);
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

    public void generiere(MetamodellHtmlProxy metamodell) throws IOException {
        this.metamodell = metamodell;
        context = erzeugeContext(metamodell);

        Files.createDirectories(Paths.get(target));
        kopiereStatischeDateien();

        generiereDatei("ueberblick");
        generiereDatei("ueberblick-frame");
        generiereDatei("alleobjekte-frame");
        generiereObjekte();
    }

    private VelocityContext erzeugeContext(MetamodellHtmlProxy metamodell) {
        VelocityContext result = new VelocityContext();
        result.put(NumberFormat.class.getSimpleName(), NumberFormat.class);
        result.put("projekt", "Datenkatalog");
        result.put("metamodell", metamodell);
        return result;
    }

    private void kopiereStatischeDateien() throws IOException {
        Files.copy(getClass().getResourceAsStream(source + "stylesheet.css"), Paths.get(target, "stylesheet.css"), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(getClass().getResourceAsStream(source + "index.html"), Paths.get(target, "index.html"), StandardCopyOption.REPLACE_EXISTING);
    }

    private void generiereDatei(String name) throws IOException {
        generiereDatei(name, "", name + ".html");
    }

    private void generiereDatei(String template, String zielpfadname, String zieldateiname) throws IOException {
        Path pfad = Paths.get(target, zielpfadname);
        Files.createDirectories(pfad);
        Path datei = pfad.resolve(zieldateiname);
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

    private void generiereObjekte() throws IOException {
        for (Systemobjekt e : metamodell.getObjekte())
            generiereObjekt(e);
    }

    private void generiereObjekt(Systemobjekt objekt) throws IOException {
        if (objekt instanceof Konfigurationsbereich) {
            context.put("konfigurationsbereich", objekt);
            generiereKonfigurationsbereichUeberblick((Konfigurationsbereich) objekt);
            generiereKonfigurationsbereichFrame((Konfigurationsbereich) objekt);
            return;
        } else if (objekt instanceof Konfigurationsverantwortlicher) {
            context.put("konfigurationsverantwortlicher", objekt);
            generiereKonfigurationsverantwortlicherUeberblick((Konfigurationsverantwortlicher) objekt);
            return;
        }

        String pfad = objekt.getKonfigurationsbereich().getZustaendiger().getPid() + "/" + objekt.getKonfigurationsbereich().getPid();
        if (objekt instanceof Mengentyp) {
            context.put("mengentyp", objekt);
            generiereDatei("mengentyp", pfad, objekt.getPid() + ".html");
        } else if (objekt instanceof Typ) {
            context.put("typ", objekt);
            generiereDatei("typ", pfad, objekt.getPid() + ".html");
        } else if (objekt instanceof Attributgruppe) {
            context.put("attributgruppe", objekt);
            generiereDatei("attributgruppe", pfad, objekt.getPid() + ".html");
        } else if (objekt instanceof Attributliste) {
            context.put("attributliste", objekt);
            generiereDatei("attributliste", pfad, objekt.getPid() + ".html");
        } else if (objekt instanceof ZeichenkettenAttributtyp) {
            context.put("attributtyp", objekt);
            generiereDatei("zeichenkette", pfad, objekt.getPid() + ".html");
        } else if (objekt instanceof ZeitstempelAttributtyp) {
            context.put("attributtyp", objekt);
            generiereDatei("zeitstempel", pfad, objekt.getPid() + ".html");
        } else if (objekt instanceof KommazahlAttributtyp) {
            context.put("attributtyp", objekt);
            generiereDatei("kommazahl", pfad, objekt.getPid() + ".html");
        } else if (objekt instanceof ObjektreferenzAttributtyp) {
            context.put("attributtyp", objekt);
            generiereDatei("objektreferenz", pfad, objekt.getPid() + ".html");
        } else if (objekt instanceof GanzzahlAttributtyp) {
            context.put("attributtyp", objekt);
            generiereDatei("ganzzahl", pfad, objekt.getPid() + ".html");
        }
    }

    private void generiereKonfigurationsbereichUeberblick(Konfigurationsbereich konfigurationsBereich) throws IOException {
        String pfad = konfigurationsBereich.getZustaendiger().getPid() + "/" + konfigurationsBereich.getPid();
        generiereDatei("konfigurationsbereich-ueberblick", pfad, "konfigurationsbereich-ueberblick.html");
    }

    private void generiereKonfigurationsbereichFrame(Konfigurationsbereich konfigurationsBereich) throws IOException {
        String pfad = konfigurationsBereich.getZustaendiger().getPid() + "/" + konfigurationsBereich.getPid();
        generiereDatei("konfigurationsbereich-frame", pfad, "konfigurationsbereich-frame.html");
    }

    private void generiereKonfigurationsverantwortlicherUeberblick(Konfigurationsverantwortlicher konfigurationsVerantwortlicher) throws IOException {
        String pfad = konfigurationsVerantwortlicher.getPid();
        generiereDatei("konfigurationsverantwortlicher-ueberblick", pfad, "konfigurationsverantwortlicher-ueberblick.html");
    }

}
