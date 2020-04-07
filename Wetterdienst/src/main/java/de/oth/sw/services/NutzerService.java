package de.oth.sw.services;

import de.oth.sw.entities.RegistrierterNutzer;
import de.oth.sw.entities.util.NumberCruncher;
import de.oth.sw.services.interfaces.NutzerServiceInterface;
import java.util.List;
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
import org.apache.log4j.Logger;

/**
 *
 * @author Maximilian Fries <maximilian.fries@st.oth-regensburg.de>
 */
@RequestScoped
@WebService
public class NutzerService implements NutzerServiceInterface {

    @PersistenceContext(unitName = "swPU")
    private EntityManager em;

    @Inject
    private transient NumberCruncher utilCalc;

    @Inject
    private transient Logger logger;

    /**
     * Sucht Benutzer abhängig vom Feld "nutzerName" des Ojekts
     * RegistrierterNutzer.
     *
     * @param sucheNutzer
     * @return Liefert den Eintrag, aus der Datenbank, wenn der Nutzer bekannt
     * ist. Ansonsten null.
     */
    @Transactional
    @WebMethod(exclude = true)
    public RegistrierterNutzer findeNutzerBeiNutzername(RegistrierterNutzer sucheNutzer) {

        TypedQuery<RegistrierterNutzer> query = em.createQuery("SELECT s "
                + "FROM RegistrierterNutzer as s "
                + "WHERE s.nutzername = :nutzername", RegistrierterNutzer.class);
        query.setParameter("nutzername", sucheNutzer.getNutzername());

        RegistrierterNutzer fund = null;

        try {
            fund = query.getSingleResult();
            logger.info("findeNutzerBeiNutzername: " + sucheNutzer.getNutzername() + " wurde gesucht und gefunden");
        } catch (NoResultException e) {
            logger.info("findeNutzerBeiNutzername: " + sucheNutzer.getNutzername() + " wurde gesucht, aber nicht gefunden");
        }

        return fund;
    }

    /**
     * Prüft das gewünschte Passwort beim Registrieren auf Güte. Ist das
     * Passwort zu kurz/unsichert, wird die Registrierung abgelehnt.
     *
     * @param antrag NutzerAntrag zur Registrierung (inkl. Passwort)
     * @return Wahrheitswert, ob das Passwort den Richtlinien des Dienstes
     * entspricht.
     */
    private boolean checkPassword(RegistrierterNutzer antrag) {

        if (antrag.getPasswort().length() < 3) {
            logger.info("checkPassword: Passwort ist leer = " + antrag);
            return false;
        }

        if (antrag.getPasswort().equals(antrag.getNutzername())) {
            logger.info("checkPassword: Passwort gleich Nutzername = " + antrag);
            return false;
        }

        if (utilCalc.getPwList().contains(antrag.getPasswort())) {
            logger.info("checkPassword: Passwort ist in top-password-list = " + antrag.getPasswort());
            return false;
        } else {
            logger.info("checkPassword: Passwort ist nicht in top-password-list = " + antrag.getPasswort());
        }

        logger.info("checkPassword: Passwort ist OK = " + antrag.getNutzername() + "|" + antrag.getPasswort());
        return true;

    }

    /**
     * Prüft Passwort und Existenz von Nutzer-Antrag. Wenn beides Ok, wird der
     * Nutzer in der Datenbank angelegt.
     *
     * @param antrag Nutzer-"Formular" für eine neue Nutzer Entity. Nutzername
     * und Passwort müssen ausgefüllt sein.
     * @return Neu angelegtes Datenbank-Entity Objekt für den Nutzer, wenn die
     * Registrierung erfolgreich ist. Ansonsten null;
     */
    @Override
    @Transactional
    @WebMethod(exclude = true)
    public RegistrierterNutzer registrieren(@WebParam(name = "nutzer") RegistrierterNutzer antrag) {

        if (checkPassword(antrag) == false) {
            logger.info("registrieren: Passwort hat sich beim registrieren disqualifiziert für " + antrag);
            return null;
        }

        if (isNutzerBekannt(antrag)) {
            logger.info("registrieren: " + antrag + " ist bereits registiert");
            return null;
        }

        em.persist(antrag);
        antrag = findeNutzerBeiNutzername(antrag);

        logger.info("registrieren: " + antrag + " wurde registriert");
        return antrag;
    }

    /**
     * Methode zum Einloggen in den Service.
     *
     * @param login RegistrierterNutzer Objekt, mit Parametern Nutzername und
     * Passwort.
     * @return Korrektes Entity Objekt aus der Datenbank, soweit der Nutzer
     * bekannt und das Passwort korrekt ist.
     */
    @Override
    @WebMethod(exclude = true)
    public RegistrierterNutzer einloggen(@WebParam(name = "nutzer") RegistrierterNutzer login) {

        RegistrierterNutzer gefundenerNutzer = findeNutzerBeiNutzername(login);

        if (isNutzerBekannt(gefundenerNutzer) == false) {
            return null;
        }

        if (!gefundenerNutzer.getPasswort().equals(login.getPasswort())) {
            logger.info("einloggen: Nutzer Passwort ist falsch: " + login);
            return null;
        }

        if (gefundenerNutzer.isGesperrt()) {
            logger.info("einloggen: Nutzer ist gesperrt: " + login);
            return null;
        }

        return gefundenerNutzer;
    }

    /**
     * Methode kann auch von nicht-Admins aufgerufen werden. Löscht das eigene
     * Nutzerkonto bei übergabe einer korrekten und der Datenbank bekannten
     * Entity vom Typ RegistrierterNutzer.
     *
     * @param loeschMich Zu löschender Nutzer
     * @return Wahrheitswert über den Erfolg des Löschens
     */
    @Override
    @Transactional
    @WebMethod(exclude = true)
    public boolean eigenesNutzerkontoLoeschen(@WebParam(name = "nutzer") RegistrierterNutzer loeschMich) {

        if (loeschMich == null || isNutzerBekannt(loeschMich) == false) {
            return false;
        }

        logger.info("eigenesNutzerkontoLoeschen: Nutzerkonto soll gelöscht werden = " + loeschMich);

        if (this.isNutzerUndPasswortKorrekt(loeschMich) == false) {
            return false;
        }

        loeschMich = em.merge(loeschMich);
        em.remove(loeschMich);

        logger.info("eigenesNutzerkontoLoeschen: Nutzer gelöscht " + loeschMich);
        return true;
    }

    /**
     * Prüft abhängig vom Admin-Token, ob ein Nutzer zum Admin aufsteigen kann.
     * AdminToken werden von Administratoren an noch-nicht-administratoren
     * vergeben, damit diese in Ihrer Nutzerrolle aufsteigen können, um weitere
     * Aufgaben übernehmen zu können.
     *
     * @param nutzer Nutzerkonto, dass ggf. geupgraded werde nsoll.
     * @param token AdminToken als einfacher String.
     * @return Entity-Objekt aus der Datenbank des ursprünglich angefragten
     * Nutzers. Jetzt evtl. mit istAdmin = true.F
     */
    @Override
    @Transactional
    @WebMethod(exclude = true)
    public RegistrierterNutzer upgradeNutzerkonto(@WebParam(name = "nutzer") RegistrierterNutzer nutzer, @WebParam(name = "token") String token) {

        if (isNutzerBekannt(nutzer) == false) {
            return null;
        }

        if (pruefeAdminToken(token) == false) {
            logger.info("upgradeNutzerkonto: Falscher AdminToken: " + token);
            return null;
        }

        RegistrierterNutzer upgrade = findeNutzerBeiNutzername(nutzer);
        upgrade.setAdminToken(token);
        upgrade.setIstAdmin(true);

        em.persist(upgrade);
        logger.info("upgradeNutzerkonto: Nutzerkonto erfolgreich geupgraded " + upgrade);

        return upgrade;
    }

    /**
     * Ändert das Passwort des übergebenen (eigenen) Nutzerskontos
     *
     * @param alterNutzer Nutzerkonto, das geändert werden soll.
     * @param neuesPasswort Neues Passwort für das Nutzerkonto.
     * @return Neues Entity-Objekt aus der Datenbank mit neuem Passwort.
     */
    @Override
    @Transactional
    @WebMethod(exclude = true)
    public RegistrierterNutzer aendereEigenesPasswort(@WebParam(name = "nutzer") RegistrierterNutzer alterNutzer, String neuesPasswort) {

        logger.info("aendereEigenesPasswort: AlterNutzer = " + alterNutzer + " neues Passwort = " + neuesPasswort);

        if (isNutzerUndPasswortKorrekt(alterNutzer) == false
                || utilCalc.getPwList().contains(neuesPasswort) || neuesPasswort.length() < 3) {
            return null;
        }

        RegistrierterNutzer neuerNutzer = alterNutzer;
        neuerNutzer.setPasswort(neuesPasswort);
        neuerNutzer = em.merge(alterNutzer);

        logger.info("aendereEigenesPasswort: Passwort erfolgreich geändert: " + neuerNutzer);
        return neuerNutzer;

    }

    /**
     * Liefert eine Liste aller Nutzer
     *
     * @return Liste aller Nutzer
     */
    @Transactional
    @WebMethod(exclude = true)
    public List<RegistrierterNutzer> holeAlleNutzer() {

        TypedQuery<RegistrierterNutzer> query = em.createQuery("SELECT s "
                + "FROM RegistrierterNutzer as s", RegistrierterNutzer.class);

        List<RegistrierterNutzer> nutzerListe = query.getResultList();

        if (nutzerListe != null) {
            logger.info("holeAlleNutzer: Liste wurde geholt mit x Einträgen = " + nutzerListe.size());
        } else {
            logger.info("holeAlleNutzer: Die Nutzerliste ist leer");
        }

        return nutzerListe;
    }

    /**
     * Prüft die Existenz eines Nutzers in der Datenbank abhängig vom
     * Nutzeranmen
     *
     * @param nutzer Entity, dessen Existenz geprüft werden soll
     * @return Wahrheitswert über die Existenz des Nutzers
     */
    private boolean isNutzerBekannt(RegistrierterNutzer nutzer) {

        if (nutzer == null) {
            return false;
        }

        RegistrierterNutzer dbNutzer = findeNutzerBeiNutzername(nutzer);

        if (dbNutzer == null) {
            logger.info("isNutzerBekannt: Nutzer ist nicht bekannt " + nutzer);
            return false;
        } else {
            logger.info("isNutzerBekannt: Nutzer ist bekannt " + nutzer);
            return true;
        }

    }

    /**
     * Prüft, ob die Logindaten eines Nutzers korrekt sind (Abgleich mit
     * Datenbank)
     *
     * @param nutzer Login-Objekt des Nutzers
     * @return Wahrheitswert über den Vergleich der Logindaten
     */
    private boolean isNutzerUndPasswortKorrekt(RegistrierterNutzer nutzer) {

        if (isNutzerBekannt(nutzer) == false) {
            return false;
        }

        RegistrierterNutzer dbNutzer = findeNutzerBeiNutzername(nutzer);

        // INFO: Equals basiert nur auf id - Überschreiben würde weitere Fehler hervorrufen
        if (dbNutzer.equalsCredentials(nutzer)) {
            logger.info("isNutzerUndPasswortKorrekt: Nutzer " + nutzer + " ist korrekt");
            return true;
        }

        logger.info("isNutzerUndPasswortKorrekt: Nutzer " + nutzer + " und dbNutzer " + dbNutzer + " sind nicht gleich");
        return false;
    }

    /**
     * Prüft die Korrektheit eines vom Nutzer eingegebenen AdminTokens um zum
     * Admin aufzusteigen.
     *
     * @param token Eingabe Token (String)
     * @return Wahrheitswert über die Prüfung des Tokens
     */
    private boolean pruefeAdminToken(String token) {

        if (utilCalc.getAdminTokenList().contains(token)) {
            logger.info("pruefeAdminToken: Token korrekt " + token);
            return true;
        } else {
            logger.info("pruefeAdminToken: Token nicht korrekt " + token);
            return false;
        }

    }

}
