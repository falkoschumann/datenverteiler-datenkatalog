/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.generator.html;

import de.bsvrz.puk.config.configFile.datamodel.ConfigDataModel;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.KonfigurationsBereich;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.Metamodell;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Der Generator erzeugt eine HTML-Dokumentation des Datenkatalogs nach dem Vorbild von JavaDoc.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class HtmlGenerator {

    private final String source = "/generator/html/";
    private final String target = "target/datenkatalog-html/";

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

        Files.createDirectories(Paths.get(target));
        kopiereStatischeDateien();
        generiereDatei("uebersicht");
        generiereDatei("uebersicht-frame");
    }

    private VelocityContext erzeugeContext(Metamodell metamodell) {
        VelocityContext result = new VelocityContext();
        SortedSet<KonfigurationsBereich> konfigurationsBereiche = new TreeSet<>((kb1, kb2) -> kb1.getName().compareToIgnoreCase(kb2.getName()));
        konfigurationsBereiche.addAll(metamodell.getKonfigurationsbereiche());
        result.put("konfigurationsbereiche", konfigurationsBereiche);
        return result;
    }

    private void kopiereStatischeDateien() throws IOException {
        Files.copy(getClass().getResourceAsStream(source + "stylesheet.css"), Paths.get(target, "stylesheet.css"), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(getClass().getResourceAsStream(source + "index.html"), Paths.get(target, "index.html"), StandardCopyOption.REPLACE_EXISTING);
    }

    private void generiereDatei(String name) throws IOException {
        OutputStream out = Files.newOutputStream(Paths.get(target, name + ".html"));
        try (Writer writer = new OutputStreamWriter(out, "UTF-8")) {
            try {
                Velocity.mergeTemplate(source + name + ".vm", "UTF-8", context, writer);
            } catch (Exception ex) {
                throw new IllegalStateException("Unreachable code. Fehler beim Erzeugen der Datei " + name + ".html.", ex);
            }
        }
    }

}
