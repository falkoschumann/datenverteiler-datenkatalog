/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.util;

import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dav.daf.main.config.SystemObjectType;

import java.util.List;
import java.util.Objects;

/**
 * Fabrik zur Bestimmung eines Objekts eines bestimmten Typs. Häufig benötigt eine Applikation ein Objekt zur Verwaltung
 * oder ähnlichen. Diese Klasse ist für den folgenden Anwendungsfall zugeschnitten:
 *
 * <ul>
 * <li>Ein zu verwendendes Objekt wird in Form seiner PID der Applikation als Aufrufparameter übergeben.</li>
 * <li>Das Objekt zur PID muss einem bestimmten Typ entsprechen.</li>
 * <li>Ist der Aufrufparameter nicht angegeben wird das einzige existierenden Objekt dieses Typs verwendet.</li>
 * <li>Gibt es kein Objekt oder mehrere Objekte zu dem Typ muss das Objekt mit seiner PID als Aufrufparameter übergeben
 * werden. Die automatische Bestimmung wird dann nicht angewandt.</li>
 * </ul>
 *
 * @author Falko Schumann
 * @since 1.4
 */
public class SingleObjektFabrik {

    private SystemObjectType objektTyp;
    private String objektPid;
    private String aufrufparameter;
    private DataModel model;

    /**
     * Initialisiert die Fabrik mit dem Datenmodell.
     *
     * @param model das Datenmodell.
     */
    public SingleObjektFabrik(final DataModel model) {
        this.model = model;
    }

    /**
     * Bestimmt ein Objekt zum angegebenen Typ. Entspricht {@code bestimmeObjekt(objekttypPid, ""; aufrufparameter}.
     *
     * @param objekttypPid    die PID des Typs des gesuchten Objekts.
     * @param aufrufparameter der Aufrufparameter mit dem die PID des gesuchten Objekts der Applikation übergeben wird.
     *                        Der Aufrufparameter wird für eine mögliche Fehlermeldung benötigt.
     * @return das gefundene Objekt, nie {@code null}.
     * @throws IllegalArgumentException der Objekttyp nicht existiert, kein Objekt zur Objekt-PID existiert oder Objekt
     *                                  und Objekttyp nicht zueinander passen.
     * @throws IllegalStateException    wenn keine ObjektPID angegeben ist und kein oder mehr als ein Objekt zum Typ
     *                                  existiert.
     */
    public SystemObject bestimmeObjekt(final String objekttypPid,
                                       @SuppressWarnings("hiding") final String aufrufparameter) {
        return bestimmeObjekt(objekttypPid, "", aufrufparameter);
    }

    /**
     * Bestimmt ein Objekt zum angegebenen Typ.
     *
     * @param objekttypPid    die PID des Typs des gesuchten Objekts.
     * @param objektPid       die PID des gesuchten Objekts oder ein leerer String, wenn es anhand des Typs gesucht
     *                        werden soll.
     * @param aufrufparameter der Aufrufparameter mit dem die PID des gesuchten Objekts der Applikation übergeben wird.
     *                        Der Aufrufparameter wird für eine mögliche Fehlermeldung benötigt.
     * @return das gefundene Objekt, nie {@code null}.
     * @throws IllegalArgumentException der Objekttyp nicht existiert, kein Objekt zur Objekt-PID existiert oder Objekt
     *                                  und Objekttyp nicht zueinander passen.
     * @throws IllegalStateException    wenn keine ObjektPID angegeben ist und kein oder mehr als ein Objekt zum Typ
     *                                  existiert.
     */
    public SystemObject bestimmeObjekt(final String objekttypPid, @SuppressWarnings("hiding") final String objektPid,
                                       @SuppressWarnings("hiding") final String aufrufparameter) {
        setObjektTyp(objekttypPid);
        setObjektPid(objektPid);
        setAufrufparameter(aufrufparameter);

        SystemObject result;
        if (objektPid.isEmpty()) {
            final List<SystemObject> objekte = objektTyp.getElements();
            validiereGenauEinObjektGefunden(objekte);
            result = objekte.get(0);
        } else {
            result = model.getObject(objektPid);
        }
        validiereObjekt(result);
        return result;
    }

    private void setObjektTyp(final String objekttypPid) {
        Objects.requireNonNull(objekttypPid, "objekttypPid");
        objektTyp = model.getType(objekttypPid);
        if (objektTyp == null)
            throw new IllegalArgumentException(
                    "Der Typ mit der PID " + objekttypPid + " ist nicht in der Konfiguration versorgt.");
    }

    private void setObjektPid(final String objektPid) {
        Objects.requireNonNull(objektPid, "objektPid");
        this.objektPid = objektPid;
    }

    private void setAufrufparameter(final String aufrufparameter) {
        Objects.requireNonNull(aufrufparameter, "aufrufparameter");
        this.aufrufparameter = aufrufparameter;
    }

    private void validiereGenauEinObjektGefunden(final List<SystemObject> objekte) {
        if (objekte.isEmpty())
            throw new IllegalStateException("Es ist kein(e) " + objektTyp + " in der Konfiguration versorgt.");
        if (objekte.size() > 1)
            throw new IllegalStateException("Es ist mehr als ein(e) " + objektTyp
                    + " in der Konfiguration versorgt. Geben Sie das zu verwendete Objekt mit dem Arufrufparameter "
                    + aufrufparameter + " an.");
    }

    private void validiereObjekt(final SystemObject objekt) {
        if (objekt == null)
            throw new IllegalArgumentException("Es existiert kein(e) " + objektTyp + " zur PID " + objektPid + ".");
        else if (!objekt.isOfType(objektTyp))
            throw new IllegalArgumentException(
                    "Das Objekt mit der PID " + objektPid + " ist kein(e) " + objektTyp + ".");
    }

}
