package de.oth.sw.model;

import de.oth.sw.entities.DetailWettervorhersage;
import de.oth.sw.entities.Ort;
import de.oth.sw.entities.RegistrierterNutzer;
import de.oth.sw.entities.Veranstaltung;
import de.oth.sw.entities.util.NumberCruncher;
import de.oth.sw.entity.converter.OrtConverter;
import de.oth.sw.services.NutzerService;
import de.oth.sw.services.WetterService;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;

/**
 *
 * @author Maximilian Fries <maximilian.fries@st.oth-regensburg.de>
 */
@Named
@SessionScoped
public class NutzerModel implements Serializable {

    @Inject
    private transient NumberCruncher utilCalc;

    @Inject
    private transient Logger logger;

    @Inject
    private NutzerService nutzerService;

    @Inject
    private WetterService wetterService;

    @Inject
    @Getter
    @Setter
    private OrtConverter ortConveter;

    @Getter
    @Setter
    private String nutzername;

    @Getter
    @Setter
    private String passwort;

    @Getter
    @Setter
    private String neuesPasswort;

    @Getter
    @Setter
    private String challengePasswort;

    @Getter
    @Setter
    private String prettyDate;

    @Getter
    @Setter
    private String neuerAdminToken;

    @Getter
    @Setter
    private Ort heimatort;

    @Getter
    @Setter
    private Ort ortFuerWetterabfrage;

    @Getter
    @Setter
    private Date datumFuerWetterabfrage;

    @Getter
    @Setter
    private DetailWettervorhersage heimatwetter;

    @Getter
    @Setter
    private DetailWettervorhersage abfragewetter;

    @Getter
    @Setter
    private RegistrierterNutzer regNutzer;

    @Getter
    @Setter
    List<Veranstaltung> veranstaltungen;

    public String pruefeAnmeldung() {

        RegistrierterNutzer neu = new RegistrierterNutzer();
        neu.setNutzername(nutzername.trim());
        neu.setPasswort(passwort.trim());
        logger.info("pruefeAnmeldung, Nutzer will sich anmelden: " + neu);

        regNutzer = nutzerService.einloggen(neu);

        if (regNutzer == null) {
            nutzername = null;
            passwort = null;
            return "/Anmelden/AnmeldenAblehung.xhtml";
        } else {
            holeHeimatwetter();
            return "/Anmelden/AnmeldenErfolgreich.xhtml";
        }
    }

    public String abmelden() {

        // Reset RegistrierterNutzers
        regNutzer = null;

        // Reset Strings
        nutzername = passwort = neuerAdminToken = null;

        // Reset Orte (nicht Ort für Wetterabfrage)
        heimatort = null;

        // Reset DetailWettervorhersagen
        heimatwetter = null;

        return "/Abmelden/AbmeldungErfolgreich.xhtml";
    }

    public String aenderePasswort() {

        RegistrierterNutzer antrag = new RegistrierterNutzer();
        antrag = regNutzer;
        antrag.setPasswort(challengePasswort);

        RegistrierterNutzer tmpNutzer = nutzerService.aendereEigenesPasswort(antrag, neuesPasswort);

        if (tmpNutzer != null) {
            regNutzer = tmpNutzer;
            logger.info("aenderePasswort: Passwort wurde erfolgreich geändert für: " + antrag);
            return "PasswortAendernErfolgreich.xhtml";
        } else {
            logger.info("aenderePasswort: Passwort wurde nicht erfolgreich geändert für: " + antrag);
            return "PasswortAendernAblehung.xhtml";
        }
    }

    public String loescheKonto() {

        RegistrierterNutzer antrag = new RegistrierterNutzer();
        antrag = regNutzer;
        antrag.setPasswort(challengePasswort);
        boolean ergebnis = nutzerService.eigenesNutzerkontoLoeschen(antrag);

        if (ergebnis) {
            // Reset der Objekte im Model
            regNutzer = null;
            nutzername = passwort = neuesPasswort = challengePasswort = null;
            heimatort = null;
            logger.info("loescheKonto: Konto wurde erfolgreich gelöscht");
            return "KontoLoeschenErfolgreich.xhtml";
        } else {
            logger.info("loescheKonto: Konto wurde nicht erfolgreich gelöscht");
            return "KontoLoeschenAblehnung.xhtml";
        }
    }

    public String upgradeZumAdmin() {

        RegistrierterNutzer upgrade = new RegistrierterNutzer();
        upgrade = nutzerService.upgradeNutzerkonto(regNutzer, neuerAdminToken);

        if (upgrade != null) {
            regNutzer = upgrade;
            logger.info("upgradeZumAdmin: Konto wurde erfolgreich geupgraded");
            return "UpgradeZumAdminErfolgreich.xhtml";
        } else {
            logger.info("upgradeZumAdmin: Konto wurde nicht erfolgreich geupgraded");
            return "UpgradeZumAdminAblehnung.xhtml";
        }
    }

    public String pruefeRegistrierung() {

        RegistrierterNutzer neu = new RegistrierterNutzer();
        neu.setNutzername(nutzername.trim());
        neu.setPasswort(passwort.trim());
        neu.setHeimatort(heimatort);
        neu.setAdminToken("");
        neu.setGesperrt(false);
        neu.setIstAdmin(false);

        regNutzer = nutzerService.registrieren(neu);

        if (regNutzer == null) {
            return "RegistrierenAblehung.xhtml";
        } else {
            regNutzer = nutzerService.einloggen(regNutzer);
            holeHeimatwetter();
            return "RegistrierenErfolgreich.xhtml";
        }
    }

    public boolean hatNutzer() {
        return !(regNutzer == null);
    }

    public List<Ort> holeAlleOrte() {
        return wetterService.holeAlleOrte();
    }

    public List<RegistrierterNutzer> holeAlleNutzer() {
        return nutzerService.holeAlleNutzer();
    }

    public String holeWetterdaten() {

        abfragewetter = wetterService.holeDetailWettervorhersage(regNutzer, ortFuerWetterabfrage,
                utilCalc.date2Calendar(datumFuerWetterabfrage));

        if (abfragewetter == null) {
            return "./WetterabfrageAblehnung.xhtml";
        } else {
            holeVeranstaltungen();
            return "./WetterabfrageErfolgreich.xhtml";
        }

    }

    private void holeHeimatwetter() {

        Date heute = new Date();
        logger.info("holeHeimatwetter: von " + regNutzer.getNutzername() + " am " + heute + " für " + regNutzer.getHeimatort());

        heimatwetter = wetterService.holeDetailWettervorhersage(regNutzer, regNutzer.getHeimatort(), utilCalc.date2Calendar(heute));
        logger.info("holeHeimatwetter: Heimatwetter fetched = " + heimatwetter);
    }

    private void holeVeranstaltungen() {

        veranstaltungen = wetterService.holeVeranstaltungen(abfragewetter);

        if (veranstaltungen != null && veranstaltungen.size() > 0) {
            logger.info("holeVeranstaltungen: Veranstaltungsliste hat x Einträge: " + veranstaltungen.size());
        } else {
            logger.info("holeVeranstaltungen: Es war keine Veranstaltung am entsprechenden Tag vorhanden");
        }
    }
}
