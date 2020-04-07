package de.oth.sw.services.interfaces;

import de.oth.sw.entities.Ort;
import de.oth.sw.entities.RegistrierterNutzer;

/**
 *
 * @author Maximilian Fries <maximilian.fries@st.oth-regensburg.de>
 */
public interface AdminServiceInterface {

    public void loescheNutzerkonto(RegistrierterNutzer nutzer);

    public RegistrierterNutzer sperreNutzerkonto(RegistrierterNutzer nutzer, boolean sperren);

    public Ort ortHinzufuegen(Ort neuerOrt);

}
