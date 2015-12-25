/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

/**
 * Attributgruppenverwendung definiert, welche Kombination von Attributgruppe und Aspekte verwendet werden kann.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class AttributgruppenVerwendung extends SystemObjekt {

    private Attributgruppe attributgruppe;
    private Aspekt aspekt;
    private boolean VerwendungExplizitVorgegeben;
    private DatensatzVerwendung datensatzVerwendung;

    public static AttributgruppenVerwendung erzeugeMitPid(String pid) {
        AttributgruppenVerwendung result = new AttributgruppenVerwendung();
        result.setPid(pid);
        return result;
    }

    public Attributgruppe getAttributgruppe() {
        return attributgruppe;
    }

    public void setAttributgruppe(Attributgruppe attributgruppe) {
        this.attributgruppe = attributgruppe;
    }

    public Aspekt getAspekt() {
        return aspekt;
    }

    public void setAspekt(Aspekt aspekt) {
        this.aspekt = aspekt;
    }

    public boolean isVerwendungExplizitVorgegeben() {
        return VerwendungExplizitVorgegeben;
    }

    public void setVerwendungExplizitVorgegeben(boolean verwendungExplizitVorgegeben) {
        VerwendungExplizitVorgegeben = verwendungExplizitVorgegeben;
    }

    public DatensatzVerwendung getDatensatzVerwendung() {
        return datensatzVerwendung;
    }

    public void setDatensatzVerwendung(DatensatzVerwendung datensatzVerwendung) {
        this.datensatzVerwendung = datensatzVerwendung;
    }

}
