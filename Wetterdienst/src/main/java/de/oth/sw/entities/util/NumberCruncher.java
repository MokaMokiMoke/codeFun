package de.oth.sw.entities.util;

import de.biersaecke.oth.event_evaluator.persistence.services.Eintrag;
import de.oth.sw.entities.Veranstaltung;
import de.oth.sw.entities.enums.Monat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import org.apache.log4j.Logger;

/**
 *
 * @author Maximilian Fries <maximilian.fries@st.oth-regensburg.de>
 */
@Named
@ApplicationScoped
public class NumberCruncher {

    @Inject
    private transient Logger logger;

    @Getter
    private transient Collection<String> pwList;

    @Getter
    private transient Collection<String> adminTokenList;

    @Getter
    private transient List<MonatsWetterparameter> monatsListe;

    public NumberCruncher() {
    }

    /**
     * Erstellt basierend auf einen START und END Wert vom Typ Date eine Liste
     * aller Tage zwischen Start und Ende.
     *
     * @param startdate Start Datum für die Berechnung
     * @param enddate End Datum für die Berechnung
     * @link https://stackoverflow.com/a/2893790/5818129
     * @return Liste mit Dates aller Tage zwischen Start und Ende.
     */
    public List<Date> holeTageZwischen(Date startdate, Date enddate) {
        if (enddate.before(startdate)) {
            logger.info("holeTageZwischen: Enddatum liegt vor Startdatum!");
        }
        List<Date> dates = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startdate);

        while (calendar.getTime().before(enddate)) {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }

        // Add Enddate to List
        dates.add(enddate);
        return dates;
    }

    /**
     * Konvertiert DATE zu CALENDAR
     *
     * @param datum Datum vom Typ Date
     * @return Datum vom Typ Calendar
     */
    public Calendar date2Calendar(Date datum) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(datum);
        return cal;

    }

    /**
     * Konvertiert CALENDAR zu DATE
     *
     * @param cal Datum vom Typ Calendar
     * @return Datum vom Typ Date
     */
    public Date calendar2Date(Calendar cal) {
        return cal.getTime();
    }

    /**
     * Abbildung von Datum auf EnumTyp Monat
     *
     * @param datum Tag, für dem der Monatswert berechnet werden soll
     * @return MonatsTyp vom EnumTyp Monat
     */
    public Monat getMonatByDate(Date datum) {

        switch (datum.getMonth()) {
            case 0:
                return Monat.Januar;
            case 1:
                return Monat.Februar;
            case 2:
                return Monat.März;
            case 3:
                return Monat.April;
            case 4:
                return Monat.Mai;
            case 5:
                return Monat.Juni;
            case 6:
                return Monat.Juli;
            case 7:
                return Monat.August;
            case 8:
                return Monat.September;
            case 9:
                return Monat.Oktober;
            case 10:
                return Monat.November;
            case 11:
                return Monat.Dezember;
            default:
                logger.error("getMonatByDate: Monat ist ungültig " + datum.getMonth());
                return Monat.Januar;
        }
    }

    public int getIntByMonat(Date datum) {
        return datum.getMonth();
    }

    /**
     * Rundet Floats auf n-Deziamlstellen
     *
     * @param number Zu rundender Float
     * @param decimalPlace Rundung auf n-Dezimalstellen
     * @link https://www.quora.com/How-do-I-truncate-float-up-to-two-decimal-points-in-Java
     * @return
     */
    public float round(float number, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(number));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    /**
     * Konvertiert Objekte vom Typ Eintrag (von EventEvaluator by Markus) in
     * Objekte vom Typ Veranstaltung (nativ) um
     *
     * @param eintraege Liste mit Objekten vom Typ EINTRAG
     * @return Liste mit Objekten vom Typ VERANSTALTUNG
     */
    public List<Veranstaltung> eintraege2veranstaltungen(List<Eintrag> eintraege) {

        Veranstaltung v;
        List<Veranstaltung> vList = new ArrayList<>();

        for (Eintrag e : eintraege) {

            v = new Veranstaltung();
            v.setName(e.getTitel());
            v.setBeschreibung(e.getDetails());
            v.setVon(e.getStart().toGregorianCalendar());
            v.setBis(e.getEnde().toGregorianCalendar());

            vList.add(v);
            logger.info("eintraege2veranstaltungen: Veranstaltung hinzugefuegt" + v);
        }

        return vList;
    }

    @PostConstruct
    private void init() {
        baueTopPasswordListe();
        baueAdminTokenListe();
        baueMonatsListe();
    }

    /**
     * Generiert eine Collection mit allen TOP Passwörtern für den Passwort
     * Check. Da das Auslesen einer lokalen Klartextdatei immer wieder Probleme
     * unter Linux bereitet, wird die Datei direkt im RAW-Format von einen
     * öffentlichen GitHub Repository geholt.
     * @link https://docs.oracle.com/javase/tutorial/networking/urls/readingURL.html
     */
    private void baueTopPasswordListe() {

        pwList = new HashSet<>();
        String gitHubUrl = "https://raw.githubusercontent.com/danielmiessler/SecLists/master/Passwords/10_million_password_list_top_1000.txt";

        URL githubPwList;
        try {
            githubPwList = new URL(gitHubUrl);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(githubPwList.openStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                pwList.add(inputLine);
            }
            in.close();
        } catch (MalformedURLException ex) {
            //java.util.logging.Logger.getLogger(NumberCruncher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            //java.util.logging.Logger.getLogger(NumberCruncher.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Falls GitHub offline, keine InternetVerbindung des App. Servers, usw.        
        // Problem Handling: Lieber eine schlechte PW-List als gar keine PW-List 
        if (pwList.isEmpty()) {
            pwList.add("12345");
        }

    }

    /**
     * Generiert eine Liste mit gültigen Admin-Tokens für die "UpgradeZumAdmin"
     * Use-Cases
     */
    private void baueAdminTokenListe() {
        adminTokenList = new HashSet<>();
        adminTokenList.add("ABC-DEF-GHI");
        adminTokenList.add("123-456-789");
        adminTokenList.add("BAT-MAN-OOO");

        logger.info("buildAdminTokenList: AdminTokenList erfolgreich aufgebaut");
    }

    /**
     * Generiert die MonatsListe für die Meteorologischen Wetterparameter für
     * die pseudo-realistischen Wettervorhersagen.
     */
    private void baueMonatsListe() {
        monatsListe = new ArrayList<>();

        // Monat, minTemp, maxTemp, maxWindspeed, maxRegenfall, maxRegenwahrscheinlihckiet
        MonatsWetterparameter jan = new MonatsWetterparameter(Monat.Januar,
                -5f, 6f, 13f, 66f, 0.95f);
        MonatsWetterparameter feb = new MonatsWetterparameter(Monat.Februar,
                -12f, 12f, 10f, 58f, 0.90f);
        MonatsWetterparameter mar = new MonatsWetterparameter(Monat.März,
                -3f, 15f, 11f, 62f, 0.85f);
        MonatsWetterparameter apr = new MonatsWetterparameter(Monat.April,
                -2f, 14f, 9f, 70f, 0.60f);
        MonatsWetterparameter mai = new MonatsWetterparameter(Monat.Mai,
                -1f, 22f, 15f, 90f, 0.99f);
        MonatsWetterparameter jun = new MonatsWetterparameter(Monat.Juni,
                2f, 24f, 8f, 110f, 0.45f);
        MonatsWetterparameter jul = new MonatsWetterparameter(Monat.Juli,
                9f, 29f, 6f, 90f, 0.30f);
        MonatsWetterparameter aug = new MonatsWetterparameter(Monat.August,
                11f, 37f, 8f, 100f, 0.1f);
        MonatsWetterparameter sep = new MonatsWetterparameter(Monat.September,
                2f, 25f, 8f, 30f, 0.2f);
        MonatsWetterparameter okt = new MonatsWetterparameter(Monat.Oktober,
                0f, 20f, 15f, 140f, 0.4f);
        MonatsWetterparameter nov = new MonatsWetterparameter(Monat.November,
                -5f, 18f, 10f, 90f, 0.6f);
        MonatsWetterparameter dez = new MonatsWetterparameter(Monat.Dezember,
                -10f, 15f, 5f, 60f, 0.75f);

        monatsListe.add(jan);
        monatsListe.add(feb);
        monatsListe.add(mar);
        monatsListe.add(apr);
        monatsListe.add(mai);
        monatsListe.add(jun);
        monatsListe.add(jul);
        monatsListe.add(aug);
        monatsListe.add(sep);
        monatsListe.add(okt);
        monatsListe.add(nov);
        monatsListe.add(dez);

        logger.info("baueMonatsListe: Liste erfolgreich aufgebaut");
    }

}
