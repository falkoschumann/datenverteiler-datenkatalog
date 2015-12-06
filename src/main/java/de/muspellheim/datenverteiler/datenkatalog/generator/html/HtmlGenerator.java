/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.generator.html;

import de.bsvrz.puk.config.configFile.datamodel.ConfigDataModel;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.KonfigurationsBereich;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.Metamodell;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.SystemObjekt;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.Typ;
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

    public static final String KONFIGURATIONSBEREICHE = "konfigurationsbereiche";
    public static final String OBJEKTE = "objekte";
    public static final String TYP = "typ";

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
        generiereDatei("uebersicht");
        generiereDatei("uebersicht-frame");
        generiereDatei("alleobjekte-frame");
        Set<SystemObjekt> objekte = (Set<SystemObjekt>) context.get(OBJEKTE);
        for (SystemObjekt e : objekte)
            generiereObjekt(e);
    }

    private VelocityContext erzeugeContext(Metamodell metamodell) {
        VelocityContext result = new VelocityContext();

        SortedSet<KonfigurationsBereich> konfigurationsBereiche = new TreeSet<>();
        konfigurationsBereiche.addAll(metamodell.getKonfigurationsbereiche());
        result.put(KONFIGURATIONSBEREICHE, konfigurationsBereiche);

        SortedSet<SystemObjekt> objekte = new TreeSet<>();
        objekte.addAll(metamodell.getKonfigurationsbereiche().stream().map(KonfigurationsBereich::getTypen).flatMap(Collection::stream).collect(Collectors.toSet()));
        result.put(OBJEKTE, objekte);

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

    private void generiereObjekt(SystemObjekt systemObjekt) throws IOException {
        if (systemObjekt instanceof Typ) {
            context.put(TYP, systemObjekt);
            String pfad = systemObjekt.getBereich().getZustaendiger().getPid() + "/" + systemObjekt.getBereich().getPid() + "/";
            Files.createDirectories(Paths.get(TARGET, pfad));
            generiereDatei("typ", pfad + systemObjekt.getPid());
        }
    }

}
