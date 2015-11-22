/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.generator.diagramm;

import de.bsvrz.puk.config.configFile.datamodel.ConfigDataModel;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.KonfigurationsBereich;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.MengenVerwendung;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.Metamodell;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.Typ;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.*;

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
        VelocityContext context = new VelocityContext();
        context.put("diagrammtitel", bereich.getName());
        context.put("konfigurationsbereich", bereich);

        Template template = null;
        try {
            template = Velocity.getTemplate("/generator/diagramm/datenkatalog.vm");
        } catch (Exception ex) {
            throw new IllegalStateException("Fehler beim Laden des Templates.", ex);
        }

        OutputStream out = new FileOutputStream("target/" + bereich.getName() + ".dot");
        try (Writer writer = new OutputStreamWriter(out, "UTF-8")) {
            template.merge(context, writer);
        }
    }

}
