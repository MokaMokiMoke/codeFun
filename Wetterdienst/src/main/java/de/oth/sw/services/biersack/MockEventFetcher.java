package de.oth.sw.services.biersack;

import de.oth.sw.entities.DetailWettervorhersage;
import de.oth.sw.entities.Ort;
import de.oth.sw.entities.Veranstaltung;
import de.oth.sw.services.biersack.interfaces.EventFetcherInterface;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import org.apache.log4j.Logger;

/**
 *
 * @author Maximilian Fries <maximilian.fries@st.oth-regensburg.de>
 */
@Alternative
@SessionScoped
public class MockEventFetcher implements EventFetcherInterface, Serializable {

    @Inject
    private transient Logger logger;

    /**
     * Mock Methode, welche immer die gleichen Dummy Daten liefert zum Testen,
     * solange der EventEvaluator Service noch (nicht) vorhanden ist. Benutzung
     * über @Alternative Annotation in WetterService nutzbar.
     *
     * @param wettervorhersage Wettervorhersage als Container für Ort/Datum, an
     * dem die Veranstaltung geholt werden soll.
     * @return Eine Liste von Veranstaltungen
     */
    @Override
    public List<Veranstaltung> holeVeranstaltungen(DetailWettervorhersage wettervorhersage) {

        List<Veranstaltung> veranstaltungen = new ArrayList<>();

        Ort pfefferland = new Ort();
        Ort salzland = new Ort();
        pfefferland.setOrtName("Pfefferland");
        salzland.setOrtName("Salzland");

        Calendar start = Calendar.getInstance();
        Calendar ende = Calendar.getInstance();
        start.set(2017, 10, 23);
        ende.set(2018, 10, 23);

        Veranstaltung mockA = new Veranstaltung();
        Veranstaltung mockB = new Veranstaltung();
        mockA.setBeschreibung("Some Beschreibung");
        mockA.setBis(start);
        mockA.setVon(ende);
        mockA.setName("Some Random Title");
        mockA.setOrt(pfefferland);

        mockB.setBeschreibung("Another Beschreibung");
        mockB.setVon(start);
        mockB.setBis(ende);
        mockB.setName("Another Random Title");
        mockB.setOrt(salzland);

        veranstaltungen.add(mockA);
        veranstaltungen.add(mockB);

        logger.info("holeVeranstaltungen: Zwei Mock Veranstaltungen zur Liste hinzugefügt");
        return veranstaltungen;

    }
}
