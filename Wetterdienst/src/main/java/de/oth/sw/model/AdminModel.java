package de.oth.sw.model;

import de.oth.sw.entities.Ort;
import de.oth.sw.entities.RegistrierterNutzer;
import de.oth.sw.entities.enums.BundeslandEnum;
import de.oth.sw.entities.enums.LandEnum;
import de.oth.sw.entity.converter.RegistrierterNutzerConverter;
import de.oth.sw.services.AdminService;
import de.oth.sw.services.WetterService;
import java.io.Serializable;
import java.util.Date;
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
public class AdminModel implements Serializable {

    @Inject
    private AdminService adminService;

    @Inject
    private WetterService wetterService;

    @Inject
    private transient Logger logger;

    @Inject
    @Getter
    @Setter
    private RegistrierterNutzerConverter registrierterNutzerConverter;

    @Getter
    @Setter
    private RegistrierterNutzer nutzerFuerBearbeiten;

    @Getter
    @Setter
    private Ort ortFuerBearbeiten;

    @Getter
    @Setter
    private Ort neuErstellterOrt;

    @Getter
    @Setter
    private BundeslandEnum neuerOrtBundesland;

    @Getter
    @Setter
    private LandEnum neuerOrtLand;

    @Getter
    @Setter
    private String neuerOrtName;

    @Getter
    @Setter
    private Date datumStart;

    @Getter
    @Setter
    private Date datumEnde;

    @Getter
    @Setter
    private boolean neuerOrtKeinBundesland;

    public String ortHinzufuegen() {

        Ort neuerOrt = new Ort();

        neuerOrt.setBundesland(neuerOrtBundesland);
        neuerOrt.setLand(neuerOrtLand);
        neuerOrt.setOrtName(neuerOrtName);

        neuerOrt = adminService.ortHinzufuegen(neuerOrt);

        // Fehler oder Ort schon vorhanden
        if (neuerOrt == null) {
            logger.info("ortHinzufuegen: Ort wurde nicht erfolgreich hinzugefügt");

            neuerOrtBundesland = null;
            neuerOrtLand = null;
            neuerOrtName = null;
            datumStart = datumEnde = null;
            return "/AdminBereich/OrtHinzufuegenAblehnung.xhtml";
        }

        neuErstellterOrt = neuerOrt;
        wetterService.fuelleWetterDatenbankFuerOrt(datumStart, datumEnde, neuerOrt);
        logger.info("ortHinzufuegen: Ort + Wetterdaten wurde erfolgreich hinzugefügt = " + neuerOrt);

        neuerOrtBundesland = null;
        neuerOrtLand = null;
        neuerOrtName = null;

        return "/AdminBereich/OrtHinzufuegenErfolgreich.xhtml";
    }

    public String nutzerEntsperren() {

        logger.info("Nutzer Entsperren: " + nutzerFuerBearbeiten);

        RegistrierterNutzer entsperren;
        entsperren = nutzerFuerBearbeiten;
        entsperren = adminService.sperreNutzerkonto(entsperren, false);

        if (entsperren != null) {
            logger.info("nutzerEntsperren: Nutzerkonto wurde erfolgreich entsperren = " + entsperren);
            return "/AdminBereich/NutzerEntsperrenErfolgreich.xhtml";
        } else {
            logger.info("nutzerEntsperren: Nutzerkonto wurde nicht erfolgreich entsperren = " + entsperren);
            return "/AdminBereich/NutzerEntsperrenAblehnung.xhtml";
        }
    }

    public String nutzerSperren() {

        logger.info("Nutzer Sperre: " + nutzerFuerBearbeiten);

        RegistrierterNutzer sperren;
        sperren = nutzerFuerBearbeiten;
        sperren = adminService.sperreNutzerkonto(sperren, true);

        if (sperren != null) {
            logger.info("nutzerSperren: Nutzerkonto wurde erfolgreich gesperrt = " + sperren);
            return "/AdminBereich/NutzerSperrenErfolgreich.xhtml";
        } else {
            logger.info("nutzerSperren: Nutzerkonto wurde nicht erfolgreich gesperrt = " + sperren);
            return "/AdminBereich/NutzerSperrenAblehnung.xhtml";
        }

    }

    public String nutzerLoeschen() {

        adminService.loescheNutzerkonto(nutzerFuerBearbeiten);
        logger.info("nutzerSperren: Nutzerkonto wurde erfolgreich gelöscht = " + nutzerFuerBearbeiten);
        return "/AdminBereich/NutzerLoeschenErfolgreich.xhtml";
    }

    public BundeslandEnum[] getBundesLaender() {
        return BundeslandEnum.values();
    }

    public LandEnum[] getLaender() {
        return LandEnum.values();
    }

}
