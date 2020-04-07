package de.oth.sw.services;

import de.oth.sw.entities.util.NumberCruncher;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

/**
 *
 * @author Maximilian Fries <maximilian.fries@st.oth-regensburg.de>
 */
@Singleton
public class CronService {

    @Inject
    WetterService wetterservice;

    @Inject
    private transient NumberCruncher utilCalc;

    /**
     * Cron-Job, der täglich um Mitternacht die Wettervorhersage für alle Orte
     * generiert und in die Datenbank einträgt.
     */
    @Schedule(hour = "0",
            persistent = false)
    public void tageswetterEintragen() {

        Calendar inTwoWeeks = Calendar.getInstance();
        Date heute = new Date();
        inTwoWeeks.setTime(heute);
        inTwoWeeks.add(Calendar.DATE, 14); // 14 Tage später

        wetterservice.vorhersageFürTagEintragen(utilCalc.calendar2Date(inTwoWeeks));
    }

}
