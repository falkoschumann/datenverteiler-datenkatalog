/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.generator.html;

import de.bsvrz.puk.config.configFile.datamodel.ConfigDataModel;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;
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
    // TODO Konfiguration parametrierbar machen
    // TODO Als Mojo ausführbar machen

    private String templates = "/generator/html/";
    private String zielverzeichnis;
    private File konfiguration;

    private MetamodellHtmlProxy metamodell;
    private VelocityContext context;

    /**
     * Main-Methode des HTML-Generators.
     * <p>Die folgenden Aufrufargumente werden unterstützt:</p>
     * <ul>
     * <li><code>-zielverzeichnis=html/</code> (optional) gibt das Zielverzeichnis für die generierten HTML-Dateien an.</li>
     * <li><code>-konfiguration</code> gibt die Verwaltungsdatendatei der Konfiguration an, für die die HTML-Dokumentation generiert werden soll.</li>
     * </ul>
     */
    public static void main(String args[]) throws IOException {
        ArgumentList argumentList = new ArgumentList(args);
        final String zielverzeichnis = argumentList.fetchArgument("-zielverzeichnis=html/").asString();
        final File konfiguration = argumentList.fetchArgument("-konfiguration").asExistingFile();
        argumentList.ensureAllArgumentsUsed();

        initialisiereVelocity();

        HtmlGenerator generator = new HtmlGenerator();
        generator.setZielverzeichnis(zielverzeichnis);
        generator.setKonfiguration(konfiguration);
        generator.generiere();
    }

    public static void initialisiereVelocity() {
        Velocity.setProperty("resource.loader", "class");
        Velocity.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        try {
            Velocity.init();
        } catch (Exception ex) {
            throw new IllegalStateException("Fehler beim Initialisieren der Template-Engine.", ex);
        }
    }

    public String getTemplates() {
        return templates;
    }

    public void setTemplates(String templates) {
        this.templates = templates;
    }

    public String getZielverzeichnis() {
        return zielverzeichnis;
    }

    public void setZielverzeichnis(String zielverzeichnis) {
        this.zielverzeichnis = zielverzeichnis;
    }

    public File getKonfiguration() {
        return konfiguration;
    }

    public void setKonfiguration(File konfiguration) {
        this.konfiguration = konfiguration;
    }

    public void generiere() throws IOException {
        ConfigDataModel model = new ConfigDataModel(konfiguration);
        try {
            generiere(new MetamodellHtmlProxy(model));
        } finally {
            model.close();
        }
    }

    public void generiere(MetamodellHtmlProxy metamodell) throws IOException {
        this.metamodell = metamodell;
        context = erzeugeContext(metamodell);

        Files.createDirectories(Paths.get(zielverzeichnis));
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
        Files.copy(getClass().getResourceAsStream(templates + "stylesheet.css"), Paths.get(zielverzeichnis, "stylesheet.css"), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(getClass().getResourceAsStream(templates + "index.html"), Paths.get(zielverzeichnis, "index.html"), StandardCopyOption.REPLACE_EXISTING);
    }

    private void generiereDatei(String name) throws IOException {
        generiereDatei(name, "", name + ".html");
    }

    private void generiereDatei(String template, String zielpfadname, String zieldateiname) throws IOException {
        Path pfad = Paths.get(zielverzeichnis, zielpfadname);
        Files.createDirectories(pfad);
        Path datei = pfad.resolve(zieldateiname);
        System.out.println("Generiere " + datei + " ...");
        OutputStream out = Files.newOutputStream(datei);
        try (Writer writer = new OutputStreamWriter(out, "UTF-8")) {
            try {
                Velocity.mergeTemplate(templates + template + ".vm", "UTF-8", context, writer);
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
        if (objekt instanceof Aspekt) {
            context.put("aspekt", objekt);
            generiereDatei("aspekt", pfad, objekt.getPid() + ".html");
        } else if (objekt instanceof Mengentyp) {
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
