/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.generator.diagramm;

import de.bsvrz.puk.config.configFile.datamodel.ConfigDataModel;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.KonfigurationsBereich;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.Metamodell;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.*;
import java.nio.file.Files;
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

    static {
        Velocity.setProperty("resource.loader", "class");
        Velocity.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        try {
            Velocity.init();
        } catch (Exception ex) {
            throw new IllegalStateException("Fehler beim Initialisieren der Template-Engine.", ex);
        }
    }

    private static final String SOURCE = "/generator/diagramm/";
    private static final String TARGET = "target/datenkatalog/diagramm/";
    private static final String TEMPLATE = "datenkatalog";

    public static void main(String args[]) throws IOException {
        ConfigDataModel model = new ConfigDataModel(new File("src/test/konfiguration/verwaltungsdaten.xml"));
        Metamodell metamodell = new Metamodell(model);
        try {
            new DiagrammGenerator().generiere(metamodell.getKonfigurationsbereich("kb.tmVerkehrGlobal"));
        } finally {
            model.close();
        }
    }

    public void generiere(KonfigurationsBereich bereich) throws IOException {
        VelocityContext context = erzeugeContext(bereich);
        Files.createDirectories(Paths.get(TARGET));
        generiereDiagramm(bereich, context);
    }

    private VelocityContext erzeugeContext(KonfigurationsBereich bereich) {
        VelocityContext context = new VelocityContext();
        context.put("diagrammtitel", bereich.getName());
        context.put("konfigurationsbereich", bereich);
        return context;
    }

    private void generiereDiagramm(KonfigurationsBereich bereich, VelocityContext context) throws IOException {
        OutputStream out = Files.newOutputStream(Paths.get(TARGET, bereich.getPid() + ".dot"));
        try (Writer writer = new OutputStreamWriter(out, "UTF-8")) {
            try {
                Velocity.mergeTemplate(SOURCE + TEMPLATE + ".vm", "UTF-8", context, writer);
            } catch (Exception ex) {
                throw new IllegalStateException("Unreachable code. Fehler beim Erzeugen der Datei " + bereich.getPid() + ".dot mit dem Template " + TEMPLATE + ".vm.", ex);
            }
        }
    }

}
