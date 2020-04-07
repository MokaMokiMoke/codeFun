package de.oth.sw.services.interfaces;

import de.oth.sw.entities.DetailWettervorhersage;
import de.oth.sw.entities.Ort;
import de.oth.sw.entities.RegistrierterNutzer;
import de.oth.sw.entities.Veranstaltung;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author Maximilian Fries <maximilian.fries@st.oth-regensburg.de>
 */
public interface WetterServiceInterface {

    // Methode für Registriete Nutzer und Festival Service (Andi)
    public DetailWettervorhersage holeDetailWettervorhersage(RegistrierterNutzer nutzer, Ort ort, Calendar datum);

    // Methode zum abfragen von Veranstaltungen von Markus (Veranstaltung Service)
    public List<Veranstaltung> holeVeranstaltungen(DetailWettervorhersage wettervorhersage);

    public void wettervorhersageEintragen(DetailWettervorhersage vorhersage);

}
