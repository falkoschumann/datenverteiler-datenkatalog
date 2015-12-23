/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.metamodell;

/**
 * Definiert ein Attribut mit seinen Eigenschaften.
 * <p>Definiert ein Attribut mit seinen Eigenschaften, das in beliebigen Attributlisten und -gruppen verwendet werden
 * kann. Ausserdem kann ein Attribut innerhalb einer Attributliste (oder -gruppe) durch die Vergabe eines Namens (oder
 * Subjekts) mehrfach verwendet werden. Dies ist ein abstrakter Typ, von dem die konkreten Attributtypen
 * GanzzahlAttributTyp, KommazahlAttributTyp, ObjektReferenzAttributTyp, ZeichenkettenAttributTyp und
 * ZeitstempelAttributTyp abgeleitet sind.</p>
 *
 * @author Falko Schumann
 * @since 3.2
 */
public interface AttributTyp {

    // tagging interface

}
