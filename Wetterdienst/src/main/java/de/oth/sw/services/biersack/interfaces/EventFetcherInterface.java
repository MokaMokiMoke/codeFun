package de.oth.sw.services.biersack.interfaces;

import de.oth.sw.entities.DetailWettervorhersage;
import de.oth.sw.entities.Veranstaltung;
import java.util.List;
import javax.jws.WebParam;

/**
 *
 * @author Maximilian Fries <maximilian.fries@st.oth-regensburg.de>
 */
public interface EventFetcherInterface {

    public List<Veranstaltung> holeVeranstaltungen(@WebParam(name = "detailWetter") DetailWettervorhersage wettervorhersage);

}
