/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.datenkatalog.generator.html;

import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.Konfigurationsbereich;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.Konfigurationsverantwortlicher;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.Metamodell;
import de.muspellheim.datenverteiler.datenkatalog.metamodell.Systemobjekt;

import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * HTML-Proxy für das Metamodell.
 *
 * @author Falko Schumann
 * @since 3.2
 */
public class MetamodellHtmlProxy extends Metamodell {

    public MetamodellHtmlProxy(DataModel model) {
        super(model);
    }

    public SortedSet<Systemobjekt> getObjekte() {
        SortedSet<Systemobjekt> result = new TreeSet<>(Systemobjekt::compareToNameOderPid);
        result.addAll(getKonfigurationsbereiche().stream().map(Konfigurationsbereich::getModell).flatMap(Collection::stream).collect(Collectors.toSet()));
        return result;
    }

    @Override
    public Set<Konfigurationsbereich> getKonfigurationsbereiche() {
        TreeSet<Konfigurationsbereich> result = new TreeSet<>(Systemobjekt::compareToNameOderPid);
        result.addAll(super.getKonfigurationsbereiche());
        return result;
    }

    @Override
    protected Konfigurationsbereich erzeugeKonfigurationsbereich(String pid) {
        return new KonfigurationsbereichHtmlProxy(pid);
    }

    public Set<Konfigurationsverantwortlicher> getKonfigurationsverantwortliche() {
        Set<Konfigurationsverantwortlicher> result = new TreeSet<>(Systemobjekt::compareToNameOderPid);
        getKonfigurationsbereiche().forEach(kb -> result.add(kb.getZustaendiger()));
        return result;
    }

    @Override
    protected Konfigurationsverantwortlicher erzeugeKonfigurationsverantwortlicher(String pid) {
        return new KonfigurationsverantwortlicherHtmlProxy(pid);
    }

    @Override
    protected void initialisiereKonfigurationsbereich(SystemObject object, Konfigurationsbereich result) {
        super.initialisiereKonfigurationsbereich(object, result);

        KonfigurationsverantwortlicherHtmlProxy zustaendiger = (KonfigurationsverantwortlicherHtmlProxy) result.getZustaendiger();
        zustaendiger.getKonfigurationsbereiche().add(result);
    }

}
