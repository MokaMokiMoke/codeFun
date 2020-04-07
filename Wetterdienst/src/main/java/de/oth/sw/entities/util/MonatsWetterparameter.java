package de.oth.sw.entities.util;

import de.oth.sw.entities.enums.Monat;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Maximilian Fries <maximilian.fries@st.oth-regensburg.de>
 */
public class MonatsWetterparameter {

    @Getter
    @Setter
    private Monat monat;

    @Getter
    @Setter
    private Float minTemp;

    @Getter
    @Setter
    private Float maxTemp;

    @Getter
    @Setter
    private Float maxWindgeschwindigkeit;

    @Getter
    @Setter
    private Float maxRegenfall;

    @Getter
    @Setter
    private Float maxRegenwahrscheinlichkeit;

    public MonatsWetterparameter(Monat monat, Float minTemp, Float maxTemp,
            Float maxWindgeschwindigkeit, Float maxRegenfall,
            Float maxRegenwahrscheinlichkeit) {
        this.monat = monat;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.maxWindgeschwindigkeit = maxWindgeschwindigkeit;
        this.maxRegenfall = maxRegenfall;

        this.maxRegenwahrscheinlichkeit = maxRegenwahrscheinlichkeit;
    }

}
