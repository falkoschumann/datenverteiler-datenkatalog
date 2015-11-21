/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.generator.diagramm;

import de.muspellheim.datenverteiler.datenkatalog.metamodell.KonfigurationsBereich;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.MengenTyp;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.MengenVerwendung;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.Typ;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

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

    private DiagrammGenerator() {
        // main class
    }

    public static void main(String args[]) throws Exception {
        Velocity.setProperty("resource.loader", "class");
        Velocity.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init();

        VelocityContext context = new VelocityContext();
        context.put("konfigurationsbereich", getTmVerkehrGlobal());

        Template template = null;
        try {
            template = Velocity.getTemplate("/generator/diagramm/datenkatalog.vm");
        } catch (ResourceNotFoundException rnfe) {
            // couldn't find the template
        } catch (ParseErrorException pee) {
            // syntax error: problem parsing the template
        } catch (MethodInvocationException mie) {
            // something invoked in the template
            // threw an exception
        } catch (Exception e) {
            // other
        }

        //StringWriter writer = new StringWriter();
        Writer writer = new OutputStreamWriter(new FileOutputStream("target/diagramm.dot"), "UTF-8");
        template.merge(context, writer);
        writer.close();
        //System.out.println(writer);
    }

    public static KonfigurationsBereich getTmVerkehrGlobal() {
        KonfigurationsBereich tmVerkehrGlobal = new KonfigurationsBereich();
        tmVerkehrGlobal.setName("TmVerkehrGlobal");
        tmVerkehrGlobal.getTypen().add(getNetz());
        tmVerkehrGlobal.getTypen().add(getAeusseresStrassenSegment());
        return tmVerkehrGlobal;
    }

    public static Typ getNetz() {
        Typ netz = new Typ();
        netz.setName("Netz");

        Typ netzBestandTeil = new Typ();
        netzBestandTeil.setName("NetzBestandTeil");
        netz.getSuperTypen().add(netzBestandTeil);

        MengenVerwendung menge = new MengenVerwendung();
        menge.setMengenName("NetzBestandTeile");
        MengenTyp mengenTyp = new MengenTyp();
        mengenTyp.getObjektTypen().add(netzBestandTeil);
        menge.setMengenTyp(mengenTyp);
        netz.getMengen().add(menge);

        return netz;
    }

    public static Typ getAeusseresStrassenSegment() {
        Typ aeusseresStrassenSegment = new Typ();
        aeusseresStrassenSegment.setName("ÄußeresStraßenSegment");

        Typ netzBestandTeil = new Typ();
        netzBestandTeil.setName("NetzBestandTeil");
        aeusseresStrassenSegment.getSuperTypen().add(netzBestandTeil);

        Typ strassenSegment = new Typ();
        strassenSegment.setName("StraßenSegment");
        aeusseresStrassenSegment.getSuperTypen().add(strassenSegment);

        return aeusseresStrassenSegment;
    }

}
