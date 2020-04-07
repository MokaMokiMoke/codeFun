package de.oth.sw.services;

import de.biersaecke.oth.event_evaluator.persistence.services.KalenderServiceService;
import de.oth.sw.entities.DetailWettervorhersage;
import de.oth.sw.entities.Ort;
import de.oth.sw.entities.RegistrierterNutzer;
import de.oth.sw.entities.Veranstaltung;
import de.oth.sw.entities.Wettervorhersage;
import de.oth.sw.entities.enums.Monat;
import de.oth.sw.entities.enums.WolkenTypEnum;
import de.oth.sw.entities.util.MonatsWetterparameter;
import de.oth.sw.entities.util.NumberCruncher;
import de.oth.sw.services.biersack.interfaces.EventFetcherInterface;
import de.oth.sw.services.interfaces.WetterServiceInterface;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.xml.ws.WebServiceRef;
import org.apache.log4j.Logger;

/**
 *
 * @author Maximilian Fries <maximilian.fries@st.oth-regensburg.de>
 */
@RequestScoped
@WebService
@Transactional
public class WetterService implements WetterServiceInterface {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/im-lamport_8080/event_evaluator/KalenderService.wsdl")
    private KalenderServiceService service;

    @Inject
    private transient NumberCruncher utilCalc;

    @Inject
    private transient Logger logger;

    @Inject
    AdminService adminService;

    @Inject
    NutzerService nutzerService;

    @Inject
    EventFetcherInterface eventFetcher;

    @PersistenceContext(unitName = "swPU")
    private EntityManager em;

    /**
     * Holt für einen angegebenen Ort und ein Datum eine Wettervorhersage aus
     * der Datenbank (wenn vorhanden).
     *
     * @param nutzer Übergebener Nutzer für die Abfrage (Service Nutzung von
     * außen) für die Protokollierung.
     * @param ort Ort-Entity für die Wetterabfrage
     * @param datum Calendar Objekt für die Abfrage (Datum)
     * @return
     */
    @Override
    public DetailWettervorhersage holeDetailWettervorhersage(RegistrierterNutzer nutzer, Ort ort, Calendar datum) {

        Ort dbOrt = adminService.findeOrtBeiName(ort);
        logger.info("holeDetailWettervorhersage: Vorhersage für " + dbOrt.getOrtName() + " am " + utilCalc.calendar2Date(datum) + " wird abgerufen von " + nutzer);

        DetailWettervorhersage vorhersage = new DetailWettervorhersage();
        vorhersage.setOrt(dbOrt);
        vorhersage.setDatum(datum);

        vorhersage = findeDetailWettervorhersageBeiOrtundDatum(vorhersage);

        return vorhersage;
    }

    /**
     * Method, die den Service "EventEvaluator" kapselt. Holt eine Liste der
     * Veranstaltungen für einen Ort/Tag. Wenn @Alternative für eventFetcher
     * gesetzt ist, wird immer die selbe Liste an Dummy-Events zurückgeliefert.
     *
     * @param wettervorhersage Wettervorhersage-Entity als Ort/Zeit Container.
     * @return Liste aller Veranstaltungen am Tag/Ort.
     */
    @Override
    @WebMethod(exclude = true)
    public List<Veranstaltung> holeVeranstaltungen(@WebParam(name = "detailWetter") DetailWettervorhersage wettervorhersage) {

        return eventFetcher.holeVeranstaltungen(wettervorhersage);
    }

    /**
     * Liefert eine Liste aller Orte aus der Datenbank.
     *
     * @return Liste aller Orte
     */
    @WebMethod(exclude = true)
    public List<Ort> holeAlleOrte() {

        TypedQuery<Ort> query = em.createQuery("SELECT ortName FROM Ort AS ortName", Ort.class);
        List<Ort> ortListe = query.getResultList();

        if (ortListe != null) {
            logger.info("holeAlleOrte: Liste wurde geholt mit x Einträgen = " + ortListe.size());
        } else {
            logger.info("holeAlleOrte: Die Ortliste ist leer");
        }

        return ortListe;
    }

    /**
     * Erstellt Pseudo-realistische Wetterdaten abhängig vom Monat.
     *
     * @param tag Datum, für den die Vorhersage generiert werden soll.
     * @param ort Ort, für den die Vorhersage generiert werden soll.
     * @return Generierte Vorhersage für den Tag/Ort. Abhängig von statistischen
     * Merkmalen für jeden Monat; gemischt mit Zufallsparametern.
     */
    private DetailWettervorhersage baueWettervorhersage(Date tag, Ort ort) {

        List<MonatsWetterparameter> wetterParam = utilCalc.getMonatsListe();
        DetailWettervorhersage neu = new DetailWettervorhersage();
        Random rand = new Random();
        final int numberOfDigits = 1;

        neu.setDatum(utilCalc.date2Calendar(tag));
        neu.setOrt(ort);

        // Wetterdaten für aktuellen Monat finden
        Monat aktuellerMonat = utilCalc.getMonatByDate(tag);
        int aktuellerMonatInt = utilCalc.getIntByMonat(tag);
        MonatsWetterparameter param = wetterParam.get(aktuellerMonatInt);

        Float medTemp = (param.getMaxTemp() + param.getMinTemp()) / 2f;
        // Quelle für Mittelwert-Berechnung: https://stackoverflow.com/a/3680648/5818129
        Float minTemp = param.getMinTemp() + (medTemp - param.getMinTemp()) * rand.nextFloat();
        Float maxTemp = medTemp + (param.getMaxTemp() - medTemp) * rand.nextFloat();
        Float avgTemp = minTemp + (maxTemp - minTemp) * rand.nextFloat();

        Float regenwahrscheinlichkeit = rand.nextFloat() * param.getMaxRegenwahrscheinlichkeit();
        Float regenfall = 0f;
        if (regenwahrscheinlichkeit > 0.01f) {
            regenfall = rand.nextFloat() * param.getMaxRegenfall();
        }

        Float warnwahrscheinlichkeit = rand.nextFloat() - 0.9f;
        boolean wetterwarnung = false;
        if (warnwahrscheinlichkeit >= 0.1f) {
            wetterwarnung = true;
        }

        if (regenwahrscheinlichkeit == 0) {
            regenfall = 0f;
        }

        Float windgeschwindigkeit = rand.nextFloat() * param.getMaxWindgeschwindigkeit();

        // INFO: Wolkentyp anahnd von Wetterdaten ableiten
        WolkenTypEnum wolke = WolkenTypEnum.Wolkenfrei;

        if (regenwahrscheinlichkeit < 0.05f && regenfall < 1f) {
            wolke = WolkenTypEnum.Schaefchenwolke;
        }

        if (regenwahrscheinlichkeit > 0.2f) {
            wolke = WolkenTypEnum.Regenwolke;
        }

        if (regenwahrscheinlichkeit > 0.9f && regenfall > 40 && minTemp > 0f) {
            wolke = WolkenTypEnum.Gewitterwolke;
        }

        if (regenwahrscheinlichkeit > 0.2f && minTemp < 0f) {
            wolke = WolkenTypEnum.Schneewolke;
        }

        neu.setWolkenTyp(wolke);

        // Wetterobjekt befüllen
        neu.setMinTemperatur(utilCalc.round(minTemp, numberOfDigits));

        neu.setMaxTemperatur(utilCalc.round(maxTemp, numberOfDigits));
        neu.setAvgTemperatur(utilCalc.round(avgTemp, numberOfDigits));
        neu.setRegenwahrscheinlichkeit(utilCalc.round(regenwahrscheinlichkeit, 2));
        neu.setRegenfall(utilCalc.round(regenfall, numberOfDigits));
        neu.setWetterwarnung(wetterwarnung);
        neu.setWindgeschwindigkeit(utilCalc.round(windgeschwindigkeit, numberOfDigits));

        return neu;
    }

    /**
     * Aufbau Methode zum Befüllen der Wetterdatenbank für größere
     * Zeitintervalle während der Entwicklungszeit. Metode erstellt von START
     * bis ENDE für ALLE Orte eine pseudo-realistische Wettervorhersage und
     * persistiert diese in der Datenbank.
     *
     * @param start Startdatum für die Vorhersagen
     * @param ende Enddatum für die Vorhersagen
     */
    @WebMethod(exclude = true)
    public void fuelleWetterDatenbank(@WebParam(name = "start") Date start, @WebParam(name = "ende") Date ende) {

        List<Ort> orte = holeAlleOrte();
        List<Date> tage = utilCalc.holeTageZwischen(start, ende);

        logger.info("fuelleWetterDatenbank: Listen für Orte, Tage und Wetterparameter geholt");

        DetailWettervorhersage vorhersage;
        List<DetailWettervorhersage> vorhersagenList = new ArrayList<>();

        // Wetterdaten erstellen
        for (Ort aktuellerOrt : orte) {
            for (Date aktuellerTag : tage) {
                vorhersagenList.add(baueWettervorhersage(aktuellerTag, aktuellerOrt));
                logger.info("fuelleWetterDatenbank: Wetter gebaut für : "
                        + aktuellerOrt.getOrtName() + " am : " + aktuellerTag);
            }
        }

        // Wetterdaten in Datenbank schreiben
        for (DetailWettervorhersage aktuelleVorhersage : vorhersagenList) {

            if (isWettervorhersageVorhanden(aktuelleVorhersage)) {
                logger.info("fuelleWetterDatenbank: Wettervorhersage ist bereits vorhanden " + aktuelleVorhersage);
                continue;
            }

            em.persist(aktuelleVorhersage);
            logger.info("fuelleWetterDatenbank: Wetter eingetragen für : " + aktuelleVorhersage.getOrt().getOrtName()
                    + " am : " + utilCalc.calendar2Date(aktuelleVorhersage.getDatum()));
        }

        logger.info("fuelleWetterDatenbank: Wetter fertig eingetragen");
    }

    /**
     * Erstellt Wettervorhersage explizit für EINEN Ort von START datum bis END
     * Datum.
     *
     * @param start Startdatum für die generierten Wetterdaten.
     * @param ende Enddatum für die generierten Wetterdaten.
     * @param ort Orte, für den die Wetterdaten generiert werden sollen.
     */
    @WebMethod(exclude = true)
    public void fuelleWetterDatenbankFuerOrt(@WebParam(name = "start") Date start, @WebParam(name = "ende") Date ende, @WebParam(name = "ort") Ort ort) {

        List<Date> tage = utilCalc.holeTageZwischen(start, ende);

        DetailWettervorhersage vorhersage;
        List<DetailWettervorhersage> vorhersagenList = new ArrayList<>();

        // Wetterdaten erstellen
        for (Date aktuellerTag : tage) {
            vorhersagenList.add(baueWettervorhersage(aktuellerTag, ort));
            logger.info("fuelleWetterDatenbankFuerOrt: Wetter gebaut für : " + ort.getOrtName() + " am : " + aktuellerTag);
        }

        // Wetterdaten in Datenbank schreiben
        for (DetailWettervorhersage aktuelleVorhersage : vorhersagenList) {

            if (isWettervorhersageVorhanden(aktuelleVorhersage)) {
                logger.info("fuelleWetterDatenbankFuerOrt: Wettervorhersage ist bereits vorhanden " + aktuelleVorhersage);
                continue;
            }

            em.persist(aktuelleVorhersage);
            logger.info("fuelleWetterDatenbankFuerOrt: Wetter eingetragen für : " + aktuelleVorhersage.getOrt().getOrtName()
                    + " am : " + utilCalc.calendar2Date(aktuelleVorhersage.getDatum()));
        }

        logger.info("fuelleWetterDatenbankFuerOrt: Wetter fertig eingetragen");
    }

    /**
     * Prüft die Existenz einer Vorhersage abhängig von Ort und Datum.
     *
     * @param vorhersage Vorhersage, die auf Existenz geprüft werden soll
     * @return Wahrheitswert über die Existenz der Vorhersage in der Datenbank.
     */
    private boolean isWettervorhersageVorhanden(DetailWettervorhersage vorhersage) {

        if (vorhersage == null) {
            return false;
        }

        DetailWettervorhersage dbVorhersage = findeDetailWettervorhersageBeiOrtundDatum(vorhersage);
        if (dbVorhersage == null) {
            logger.info("isWettervorhersageVorhanden: Vorhersage ist nicht vorhanden =" + vorhersage);
            return false;
        }

        logger.info("isWettervorhersageVorhanden: Vorhersage ist vorhanden " + vorhersage);
        return true;
    }

    /**
     * Sucht Wettervorhersage-Objekte in der Datenbank abhängig von Ort und
     * Datum.
     *
     * @param suche Vorhersage, die gesucht werden soll
     * @return Gefundene Wettervorhersage aus der Datenbank (ggf. Null).
     */
    @WebMethod(exclude = true)
    public DetailWettervorhersage findeDetailWettervorhersageBeiOrtundDatum(Wettervorhersage suche) {

        // INFO: Helfer, damit Andi (FestivalPlaner) "Freitextsuche" anwenden kann
        suche.setOrt(adminService.findeOrtBeiName(suche.getOrt()));

        TypedQuery<DetailWettervorhersage> query = em.createQuery("SELECT s "
                + "FROM DetailWettervorhersage as s "
                + "WHERE s.ort.id = :ortId "
                + "AND s.datum = :datum", DetailWettervorhersage.class);

        query.setParameter("ortId", suche.getOrt().getId());
        query.setParameter("datum", suche.getDatum());

        DetailWettervorhersage fund = null;

        try {
            fund = query.getSingleResult();
            logger.info("findeWettervorhersageBeiOrtundDatum: " + suche.getOrt() + " am "
                    + utilCalc.calendar2Date(suche.getDatum()) + " wurde gesucht und gefunden");
        } catch (NoResultException e) {
            logger.info("findeWettervorhersageBeiOrtundDatum: " + suche.getOrt() + " am "
                    + utilCalc.calendar2Date(suche.getDatum()) + " wurde gesucht aber nicht gefunden");
        }

        return fund;
    }

    /**
     * Persistiert eine einzelne Wettervorhersage in der Datenbank.
     *
     * @param neueVorhersage Wettervorhersage, die eingetragen werden soll
     */
    @Override
    @WebMethod(exclude = true)
    public void wettervorhersageEintragen(@WebParam(name = "neuesWetter") DetailWettervorhersage neueVorhersage) {

        if (isWettervorhersageVorhanden(neueVorhersage)) {
            logger.info("wettervorhersageEintragen: Wettervorhersage bereits vorhanden");
            return;
        }

        em.persist(neueVorhersage);
        logger.info("wettervorhersageEintragen: Vorhersage eingragen " + neueVorhersage);
    }

    /**
     * Persistiert Wettervorhersagen für alle Orte explizit an einen Tag. Wird
     * z.B. genutzt bei der Eintragung neuer Vorhersagen um Mitternacht
     * (CronService)
     *
     * @param tag Tag, an welchem für alle Orte eine Vorhersage generiert und
     * persistiert werden soll
     */
    @WebMethod(exclude = true)
    public void vorhersageFürTagEintragen(Date tag) {

        List<Ort> orte = holeAlleOrte();

        for (Ort ort : orte) {
            DetailWettervorhersage vorhersage = baueWettervorhersage(tag, ort);
            wettervorhersageEintragen(vorhersage);
        }

        logger.info("tageswetterEintragen: Wetter eintragen abgeschlossen für " + tag);
    }

}
