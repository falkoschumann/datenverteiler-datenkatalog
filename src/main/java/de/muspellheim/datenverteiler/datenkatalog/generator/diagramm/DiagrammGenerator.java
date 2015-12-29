/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.generator.diagramm;

import de.bsvrz.puk.config.configFile.datamodel.ConfigDataModel;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.Konfigurationsbereich;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.Metamodell;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Ein Generator für Überblicksdiagramme des Datenkatalogs.
 * <p>
 * Der Generator erzeugt eine DOT-Datei, die mit <a href="http://www.graphviz.org">GraphViz</a> dargestellt, gedruckt
 * oder in andere Dateiformate umgewandelt werden kann.
 * </p>
 *
 * @author Falko Schumann
 * @since 3.2
 */
public final class DiagrammGenerator {

    // TODO Filter für Typen parametrieren
    // TODO Vererbung parametrieren
    // TODO Mengen parametrieren
    // TODO Referenzen parametrieren

    private String templates = "/generator/diagramm/";
    private String zielverzeichnis;
    private File konfiguration;
    private boolean zeigeVererbung = true;
    private boolean zeigeMengen = true;
    private boolean zeigeAttributgruppen = true;

    /**
     * Main-Methode des Diagrammgenerators.
     * <p>Die folgenden Aufrufargumente werden unterstützt:</p>
     * <ul>
     * <li><code>-zielverzeichnis=html/</code> (optional) gibt das Zielverzeichnis für die generierten Diagramme an.</li>
     * <li><code>-konfiguration</code> gibt die Verwaltungsdatendatei der Konfiguration an, für die die Diagramme generiert werden soll.</li>
     * </ul>
     */
    public static void main(String args[]) throws IOException {
        ArgumentList argumentList = new ArgumentList(args);
        final String zielverzeichnis = argumentList.fetchArgument("-zielverzeichnis=diagramm/").asString();
        final File konfiguration = argumentList.fetchArgument("-konfiguration").asExistingFile();
        final boolean vererbung = argumentList.fetchArgument("-vererbung=ja").booleanValue();
        final boolean mengen = argumentList.fetchArgument("-mengen=ja").booleanValue();
        final boolean attributgruppen = argumentList.fetchArgument("-attributgruppen=ja").booleanValue();
        argumentList.ensureAllArgumentsUsed();

        initialisiereVelocity();

        DiagrammGenerator generator = new DiagrammGenerator();
        generator.setZielverzeichnis(zielverzeichnis);
        generator.setKonfiguration(konfiguration);
        generator.setZeigeVererbung(vererbung);
        generator.setZeigeMengen(mengen);
        generator.setZeigeAttributgruppen(attributgruppen);
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

    public boolean isZeigeVererbung() {
        return zeigeVererbung;
    }

    public void setZeigeVererbung(boolean zeigeVererbung) {
        this.zeigeVererbung = zeigeVererbung;
    }

    public boolean isZeigeMengen() {
        return zeigeMengen;
    }

    public void setZeigeMengen(boolean zeigeMengen) {
        this.zeigeMengen = zeigeMengen;
    }

    public boolean isZeigeAttributgruppen() {
        return zeigeAttributgruppen;
    }

    public void setZeigeAttributgruppen(boolean zeigeAttributgruppen) {
        this.zeigeAttributgruppen = zeigeAttributgruppen;
    }

    public void generiere() throws IOException {
        ConfigDataModel model = new ConfigDataModel(konfiguration);
        try {
            Metamodell metamodell = new MetamodellDiagrammProxy(model);
            for (Konfigurationsbereich e : metamodell.getKonfigurationsbereiche())
                generiere(e);
        } finally {
            model.close();
        }
    }

    public void generiere(Konfigurationsbereich bereich) throws IOException {
        VelocityContext context = erzeugeContext(bereich);
        Files.createDirectories(Paths.get(zielverzeichnis));
        generiereDiagramm(bereich, context);
    }

    private VelocityContext erzeugeContext(Konfigurationsbereich bereich) {
        VelocityContext context = new VelocityContext();
        context.put("diagrammtitel", bereich.getName());
        context.put("konfigurationsbereich", bereich);
        context.put("zeigeVererbung", zeigeVererbung);
        context.put("zeigeMengen", zeigeMengen);
        context.put("zeigeAttributgruppen", zeigeAttributgruppen);
        return context;
    }

    private void generiereDiagramm(Konfigurationsbereich bereich, VelocityContext context) throws IOException {
        Path datei = Paths.get(zielverzeichnis, bereich.getPid() + ".dot");
        System.out.println("Generiere " + datei + " ...");
        OutputStream out = Files.newOutputStream(datei);
        try (Writer writer = new OutputStreamWriter(out, "UTF-8")) {
            try {
                Velocity.mergeTemplate(templates + "datenkatalog" + ".vm", "UTF-8", context, writer);
            } catch (Exception ex) {
                throw new IllegalStateException("Unreachable code. Fehler beim Erzeugen der Datei " + bereich.getPid() + ".dot mit dem Template " + "datenkatalog" + ".vm.", ex);
            }
        }
    }

}
