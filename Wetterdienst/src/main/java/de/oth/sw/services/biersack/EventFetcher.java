package de.oth.sw.services.biersack;

import de.biersaecke.oth.event_evaluator.persistence.services.Eintrag;
import de.biersaecke.oth.event_evaluator.persistence.services.KalenderService;
import de.biersaecke.oth.event_evaluator.persistence.services.KalenderServiceService;
import de.oth.sw.entities.DetailWettervorhersage;
import de.oth.sw.entities.Veranstaltung;
import de.oth.sw.entities.util.NumberCruncher;
import de.oth.sw.services.biersack.interfaces.EventFetcherInterface;
import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceRef;
import org.apache.log4j.Logger;

/**
 *
 * @author Maximilian Fries <maximilian.fries@st.oth-regensburg.de>
 */
@SessionScoped
public class EventFetcher implements EventFetcherInterface, Serializable {

    @Inject
    private transient NumberCruncher utilCalc;

    @Inject
    private transient Logger logger;

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/im-lamport_8080/event_evaluator/KalenderService.wsdl")
    private KalenderServiceService service;

    /**
     * Produktive Methode, die tatsächliche Events/Veranstaltungen vom
     * EventEvaluator holt und diese als Liste weitergibt. Mock Methode kann via
     *
     * @Alternative in der Klasse WetterService genutzt werden.
     *
     * @param wettervorhersage Wettervorhersage als Container für Ort/Datum, an
     * dem die Veranstaltung geholt werden soll.
     * @return Eine Liste von Veranstaltungen
     */
    @Override
    public List<Veranstaltung> holeVeranstaltungen(DetailWettervorhersage wettervorhersage) {
        List<Veranstaltung> veranstaltungen = null;
        logger.info("holeVeranstaltungen: Suche Veranstaltung für " + wettervorhersage.getOrt().getOrtName()
                + " am " + utilCalc.calendar2Date(wettervorhersage.getDatum()));

        try {
            // Fremd-Quelle: https://stackoverflow.com/a/835963/5818129
            GregorianCalendar tmpDate = (GregorianCalendar) wettervorhersage.getDatum();
            tmpDate.setTime(utilCalc.calendar2Date(wettervorhersage.getDatum()));
            KalenderService port = service.getKalenderServicePort();
            XMLGregorianCalendar veranstaltungDatum = DatatypeFactory.newInstance().newXMLGregorianCalendar(tmpDate);
            // INFO: Normalize and set to midnight
            veranstaltungDatum.setTimezone(0);
            String veranstaltungOrtName = wettervorhersage.getOrt().getOrtName();
            List<Eintrag> result = port.holenOeffentlicheEintraegeFuerTag(veranstaltungDatum, veranstaltungOrtName);
            veranstaltungen = utilCalc.eintraege2veranstaltungen(result);

            logger.info("holeVeranstaltungen: Anzahl der Veranstaltungen = " + result.size());
        } catch (Exception ex) {
            logger.error("holeVeranstaltungen: Exception beim holen der Veranstaltungen beim Markus geflogen :(");
        }

        return veranstaltungen;
    }

}
