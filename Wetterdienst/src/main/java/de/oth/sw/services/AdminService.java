package de.oth.sw.services;

import de.oth.sw.entities.Ort;
import de.oth.sw.entities.RegistrierterNutzer;
import de.oth.sw.entities.enums.LandEnum;
import de.oth.sw.services.interfaces.AdminServiceInterface;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jws.WebParam;
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
@ApplicationScoped
public class AdminService implements AdminServiceInterface {

    @PersistenceContext(unitName = "swPU")
    private EntityManager em;

    @Inject
    NutzerService nutzerService;

    @Inject
    private transient Logger logger;

    /**
     * Löscht beliebiges Nutzerkonto. Die Methode ist nur von Nutzern der Gruppe
     * Admin aufrufbar.
     *
     * @param loeschMich Nutzer, der gelöscht werden soll.
     */
    @Override
    @Transactional
    public void loescheNutzerkonto(@WebParam(name = "nutzer") RegistrierterNutzer loeschMich) {

        RegistrierterNutzer zuLoeschen = em.find(RegistrierterNutzer.class, loeschMich.getId());
        em.remove(zuLoeschen);
        logger.info("loescheNutzerkonto: Nutzerkonto wurde gelöscht = " + loeschMich);
    }

    /**
     * Fügt einen neuen Ort hinzu. Die Methode ist nur von Nutzern der Gruppe
     * Admin aufrufbar.
     *
     * @param neuerOrt
     * @return Neu erstellte Ort-Entity (ggf. null)
     */
    @Override
    @Transactional
    public Ort ortHinzufuegen(@WebParam(name = "neuerOrt") Ort neuerOrt) {

        if (isOrtBekannt(neuerOrt)) {
            logger.info("addOrt: Ort ist bereits vorhanden " + neuerOrt);
            return null;
        }

        if (neuerOrt.getLand() != LandEnum.Deutschland) {
            neuerOrt.setBundesland(null);
            logger.info("ortHinzufuegen: Land/Bundesland wurde automatisch korrigiert");
        }

        em.persist(neuerOrt);
        Ort dbOrt = em.find(Ort.class, neuerOrt.getId());
        logger.info("ortHinzufuegen: Ort wurde hinzugefügt " + dbOrt);

        return dbOrt;

    }

    /**
     * Liefert die Ort-Entity abhängig vom Ort Namen aus der Datenbank, wenn
     * diese vorhanden ist.
     *
     * @param ortSuche
     * @return Gefundene Ort-Entity (ggf. null)
     */
    public Ort findeOrtBeiName(Ort ortSuche) {

        TypedQuery<Ort> query = em.createQuery("SELECT s "
                + "FROM Ort as s "
                + "WHERE s.ortName = :ortName", Ort.class);
        query.setParameter("ortName", ortSuche.getOrtName());

        Ort fund = null;

        try {
            fund = query.getSingleResult();
            logger.info("findeOrtBeiName: " + ortSuche.getOrtName() + " wurde gesucht und gefunden");
        } catch (NoResultException e) {
            logger.info("findeOrtBeiName: " + ortSuche.getOrtName() + " wurde gesucht, aber nicht gefunden :(");
        }

        return fund;
    }

    /**
     * Liefert die Ort-Entity abhängig von der Ort ID aus der Datenbank, wenn
     * diese vorhanden ist.
     *
     * @param ortId
     * @return Gefundene Ort-Entity (ggf. null)
     */
    @Transactional
    public Ort findeOrtBeiId(Long ortId) {

        TypedQuery<Ort> query = em.createQuery("SELECT s "
                + "FROM Ort as s "
                + "WHERE s.id = :id", Ort.class);
        query.setParameter("id", ortId);

        Ort ortFund = null;

        try {
            ortFund = query.getSingleResult();
            logger.info("findeOrtBeiId: " + ortFund + " wurde gesucht und gefunden");
        } catch (NoResultException e) {
            logger.info("findeOrtBeiId: " + ortId + " wurde gesucht, aber nicht gefunden :(");
        }

        return ortFund;

    }

    /**
     * Ent-/Sperrt den übergebenen Nutzer. Der Nutzername reicht als Parameter
     * für den zu sperrenden Nutzer. Die Methode ist nur von Nutzern der Gruppe
     * Admin aufrufbar.
     *
     * @param zuSperren Nutzer, der ent- bzw. gesperrt werden soll (Nutzername
     * reicht für die interne Suche)
     * @param sperre true = Nutzer wird gesperrt | false = nutzer wird entsperrt
     * @return
     */
    @Override
    @Transactional
    public RegistrierterNutzer sperreNutzerkonto(RegistrierterNutzer zuSperren, boolean sperre) {

        RegistrierterNutzer nutzer = nutzerService.findeNutzerBeiNutzername(zuSperren);

        if (nutzer == null) {
            return null;
        }

        nutzer.setGesperrt(sperre);
        em.merge(nutzer);
        logger.info("sperreNutzerkonto: Nutzerkonto wurde gesperrt: " + sperre + " für " + zuSperren);

        return nutzer;
    }

    /**
     * Prüft, ob ein Übergebener Ort in der Datenbank vorhanden/ bekannt ist.
     * Die Suche erfolgt über den Namen des Ortes, nicht über die ID. Diese
     * Methode ist nur von Nutzern der Gruppe Admin aufrufbar.
     *
     * @param suchMich Ort Objekt, welches gesucht werden soll
     * @return Wahrheitswert, ob der gesuchte Ort bekannt/vorhanden ist, oder
     * nicht (neuer/unbekannter Ort).
     */
    public boolean isOrtBekannt(Ort suchMich) {

        Ort dbOrt = findeOrtBeiName(suchMich);

        if (dbOrt != null) {
            logger.info("isOrtBekannt: Ort ist bekannt " + dbOrt);
            return true;
        } else {
            logger.info("isOrtBekannt: Ort ist nicht bekannt " + suchMich);
            return false;
        }
    }

}
