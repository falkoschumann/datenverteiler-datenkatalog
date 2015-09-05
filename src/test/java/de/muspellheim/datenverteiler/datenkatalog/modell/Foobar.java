/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.modell;

import de.muspellheim.datenverteiler.datenkatalog.bind.AttributgruppenDefinition;

@AttributgruppenDefinition(pid = "atg.foobar")
public class Foobar {

    private int foo;
    private String bar;

    public int getFoo() {
        return foo;
    }

    public void setFoo(int foo) {
        this.foo = foo;
    }

    public String getBar() {
        return bar;
    }

    public void setBar(String bar) {
        this.bar = bar;
    }

}
