/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.generator.java;

import de.bsvrz.puk.config.configFile.datamodel.ConfigDataModel;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.*;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Ein Generator für Java-Klassen zum Zugriff auf den Datenkatalogs.
 * <p>
 * Die generierten Klassen lassen sich mit {@link de.muspellheim.datenverteiler.datenkatalog.bind.Context} nutzen.
 * </p>
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class JavaGenerator {

    public static final String PROP_OBJEKTE = "objekte";
    public static final String PROP_ATTRIBUTGRUPPE = "attributgruppe";
    public static final String PROP_ATTRIBUTLISTE = "attributliste";
    public static final String PROP_PAKETPRAEFIX = "paketpraefix";
    public static final String PROP_JAVA = "Java";
    public static final String PROP_DATEIKOPF = "dateikopf";

    static {
        Velocity.setProperty("resource.loader", "class");
        Velocity.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        try {
            Velocity.init();
        } catch (Exception ex) {
            throw new IllegalStateException("Fehler beim Initialisieren der Template-Engine.", ex);
        }
    }

    private static final String SOURCE = "/generator/java/";
    private static final String TARGET = "target/generated-sources/datenkatalog/";

    private VelocityContext context;

    public static void main(String args[]) throws IOException {
        ConfigDataModel model = new ConfigDataModel(new File("src/test/konfiguration/verwaltungsdaten.xml"));
        Metamodell metamodell = new Metamodell(model);
        try {
            new JavaGenerator().generiere(metamodell);
        } finally {
            model.close();
        }
    }

    public void generiere(Metamodell metamodell) throws IOException {
        context = erzeugeContext(metamodell);

        Files.createDirectories(Paths.get(TARGET));
        generiereObjekte();
    }

    private VelocityContext erzeugeContext(Metamodell metamodell) {
        VelocityContext result = new VelocityContext();

        result.put(PROP_JAVA, Java.class);
        result.put(PROP_DATEIKOPF, "/*\n" +
                " * Copyright (c) 2015 Falko Schumann\n" +
                " * Released under the terms of the MIT License.\n" +
                " */\n\n");
        result.put(PROP_PAKETPRAEFIX, "de.muspellheim.datenverteiler.datenkatalog");

        List<SystemObjekt> objekte = new ArrayList<>();
        objekte.addAll(metamodell.getKonfigurationsbereiche().stream().map(KonfigurationsBereich::getAlleObjekte).flatMap(Collection::stream).collect(Collectors.toSet()));
        objekte.sort(SystemObjekt::compareToNameOderPid);
        result.put(PROP_OBJEKTE, objekte);

        return result;
    }

    private void generiereDatei(String template, String zieldateiname) throws IOException {
        Path datei = Paths.get(TARGET, zieldateiname + ".java");
        if (Files.exists(datei))
            System.err.println("Warnung: Die Datei existierte bereits: " + datei);
        System.out.println("Generiere " + datei + " ...");
        OutputStream out = Files.newOutputStream(datei);
        try (Writer writer = new OutputStreamWriter(out, "UTF-8")) {
            try {
                Velocity.mergeTemplate(SOURCE + template + ".vm", "UTF-8", context, writer);
            } catch (Exception ex) {
                throw new IllegalStateException("Unreachable code. Fehler beim Erzeugen der Datei " + zieldateiname + ".html mit dem Template " + template + ".vm.", ex);
            }
        }
    }

    private void generiereObjekte() throws IOException {
        List<SystemObjekt> objekte = (List<SystemObjekt>) context.get(PROP_OBJEKTE);
        for (SystemObjekt e : objekte)
            generiereObjekt(e);
    }

    private void generiereObjekt(SystemObjekt systemObjekt) throws IOException {
        String pfad = context.get(PROP_PAKETPRAEFIX) + "/" + Java.paket(systemObjekt).replaceAll("\\.", "/");
        Files.createDirectories(Paths.get(TARGET, pfad));
        if (systemObjekt instanceof Attributgruppe) {
            context.put(PROP_ATTRIBUTGRUPPE, systemObjekt);
            generiereDatei(PROP_ATTRIBUTGRUPPE, pfad + "/" + Java.klasse(systemObjekt));
        } else if (systemObjekt instanceof AttributListenDefinition) {
            context.put(PROP_ATTRIBUTLISTE, systemObjekt);
            generiereDatei(PROP_ATTRIBUTLISTE, pfad + "/" + Java.klasse(systemObjekt));
        }
    }

}
