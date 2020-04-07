package de.oth.sw.services.interfaces;

import de.oth.sw.entities.RegistrierterNutzer;

/**
 *
 * @author Maximilian Fries <maximilian.fries@st.oth-regensburg.de>
 */
public interface NutzerServiceInterface {

    public RegistrierterNutzer registrieren(RegistrierterNutzer antrag);

    public RegistrierterNutzer einloggen(RegistrierterNutzer loginDaten);

    public boolean eigenesNutzerkontoLoeschen(RegistrierterNutzer loginDaten);

    public RegistrierterNutzer upgradeNutzerkonto(RegistrierterNutzer loginDaten, String token);

    public RegistrierterNutzer aendereEigenesPasswort(RegistrierterNutzer neusPasswort, String neuesPasswort);

}
