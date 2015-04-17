/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog.modell;

import de.bsvrz.sys.funclib.datenkatalog.bind.AttributgruppenDefinition;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

@AttributgruppenDefinition(pid = "atg.verkehrsDatenLangZeitMSV")
public class VerkehrsDatenLangZeitMSV {

    private int _01spitzenStundeQKfzGesamt;
    private Date _01spitzenStundeQKfzGesamtZeitPunkte[];
    private int _30SpitzenStundeQKfzGesamt;
    private Date _30SpitzenStundeQKfzGesamtZeitPunkte[];
    private int _50SpitzenStundeQKfzGesamt;
    private Date _50SpitzenStundeQKfzGesamtZeitPunkte[];

    public int get01SpitzenStundeQKfzGesamt() {
        return _01spitzenStundeQKfzGesamt;
    }

    public void set01SpitzenStundeQKfzGesamt(int _01spitzenStundeQKfzGesamt) {
        this._01spitzenStundeQKfzGesamt = _01spitzenStundeQKfzGesamt;
    }

    public Date[] get01SpitzenStundeQKfzGesamtZeitPunkte() {
        return _01spitzenStundeQKfzGesamtZeitPunkte;
    }

    public void set01SpitzenStundeQKfzGesamtZeitPunkte(Date[] _01spitzenStundeQKfzGesamtZeitPunkte) {
        this._01spitzenStundeQKfzGesamtZeitPunkte = _01spitzenStundeQKfzGesamtZeitPunkte;
    }

    public int get30SpitzenStundeQKfzGesamt() {
        return _30SpitzenStundeQKfzGesamt;
    }

    public void set30SpitzenStundeQKfzGesamt(int _30SpitzenStundeQKfzGesamt) {
        this._30SpitzenStundeQKfzGesamt = _30SpitzenStundeQKfzGesamt;
    }

    public Date[] get30SpitzenStundeQKfzGesamtZeitPunkte() {
        return _30SpitzenStundeQKfzGesamtZeitPunkte;
    }

    public void set30SpitzenStundeQKfzGesamtZeitPunkte(Date[] _30SpitzenStundeQKfzGesamtZeitPunkte) {
        this._30SpitzenStundeQKfzGesamtZeitPunkte = _30SpitzenStundeQKfzGesamtZeitPunkte;
    }

    public int get50SpitzenStundeQKfzGesamt() {
        return _50SpitzenStundeQKfzGesamt;
    }

    public void set50SpitzenStundeQKfzGesamt(int _50SpitzenStundeQKfzGesamt) {
        this._50SpitzenStundeQKfzGesamt = _50SpitzenStundeQKfzGesamt;
    }

    public Date[] get50SpitzenStundeQKfzGesamtZeitPunkte() {
        return _50SpitzenStundeQKfzGesamtZeitPunkte;
    }

    public void set50SpitzenStundeQKfzGesamtZeitPunkte(Date[] _50SpitzenStundeQKfzGesamtZeitPunkte) {
        this._50SpitzenStundeQKfzGesamtZeitPunkte = _50SpitzenStundeQKfzGesamtZeitPunkte;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VerkehrsDatenLangZeitMSV that = (VerkehrsDatenLangZeitMSV) o;
        return Objects.equals(_01spitzenStundeQKfzGesamt, that._01spitzenStundeQKfzGesamt) &&
                Objects.equals(_30SpitzenStundeQKfzGesamt, that._30SpitzenStundeQKfzGesamt) &&
                Objects.equals(_50SpitzenStundeQKfzGesamt, that._50SpitzenStundeQKfzGesamt) &&
                Arrays.equals(_01spitzenStundeQKfzGesamtZeitPunkte, that._01spitzenStundeQKfzGesamtZeitPunkte) &&
                Arrays.equals(_30SpitzenStundeQKfzGesamtZeitPunkte, that._30SpitzenStundeQKfzGesamtZeitPunkte) &&
                Arrays.equals(_50SpitzenStundeQKfzGesamtZeitPunkte, that._50SpitzenStundeQKfzGesamtZeitPunkte);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_01spitzenStundeQKfzGesamt, _01spitzenStundeQKfzGesamtZeitPunkte, _30SpitzenStundeQKfzGesamt, _30SpitzenStundeQKfzGesamtZeitPunkte, _50SpitzenStundeQKfzGesamt, _50SpitzenStundeQKfzGesamtZeitPunkte);
    }

    @Override
    public String toString() {
        return "VerkehrsDatenLangZeitMSV{" +
                "01spitzenStundeQKfzGesamt=" + _01spitzenStundeQKfzGesamt +
                ", 01spitzenStundeQKfzGesamtZeitPunkte=" + Arrays.toString(_01spitzenStundeQKfzGesamtZeitPunkte) +
                ", 30SpitzenStundeQKfzGesamt=" + _30SpitzenStundeQKfzGesamt +
                ", 30SpitzenStundeQKfzGesamtZeitPunkte=" + Arrays.toString(_30SpitzenStundeQKfzGesamtZeitPunkte) +
                ", 50SpitzenStundeQKfzGesamt=" + _50SpitzenStundeQKfzGesamt +
                ", 50SpitzenStundeQKfzGesamtZeitPunkte=" + Arrays.toString(_50SpitzenStundeQKfzGesamtZeitPunkte) +
                '}';
    }

}
