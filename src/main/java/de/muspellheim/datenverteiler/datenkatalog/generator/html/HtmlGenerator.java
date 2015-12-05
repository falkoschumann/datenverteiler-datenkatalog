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
        VelocityContext context = new VelocityContext();
        SortedSet<KonfigurationsBereich> konfigurationsBereiche = new TreeSet<>((kb1, kb2) -> kb1.getName().compareToIgnoreCase(kb2.getName()));
        konfigurationsBereiche.addAll(metamodell.getKonfigurationsbereiche());
        context.put("konfigurationsbereiche", konfigurationsBereiche);

        String source = "/generator/html/";
        String target = "target/datenkatalog-html/";
        Files.createDirectories(Paths.get(target));
        Files.copy(getClass().getResourceAsStream(source + "stylesheet.css"), Paths.get(target, "stylesheet.css"), StandardCopyOption.REPLACE_EXISTING);

        OutputStream out = Files.newOutputStream(Paths.get(target, "uebersicht.html"));
        try (Writer writer = new OutputStreamWriter(out, "UTF-8")) {
            try {
                Velocity.mergeTemplate(source + "uebersicht.vm", "UTF-8", context, writer);
            } catch (Exception ex) {
                throw new IllegalStateException("Unreachable code. Fehler beim Erzeugen der Übersicht.", ex);
            }
        }
    }

}
