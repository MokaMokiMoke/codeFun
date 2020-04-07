package de.oth.sw.entities;

import de.oth.sw.entities.enums.WolkenTypEnum;
import java.util.Calendar;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Maximilian Fries <maximilian.fries@st.oth-regensburg.de>
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DetailWettervorhersage extends Wettervorhersage {

    @Getter
    @Setter
    private Float minTemperatur;

    @Getter
    @Setter
    private Float maxTemperatur;

    @Getter
    @Setter
    private Float windgeschwindigkeit;

    @Getter
    @Setter
    private WolkenTypEnum wolkenTyp;

    @Getter
    @Setter
    private Float regenwahrscheinlichkeit;

    // Default Constructor for persistence logic
    public DetailWettervorhersage() {
    }

    public DetailWettervorhersage(Float minTemperatur, Float maxTemperatur, Float windgeschwindigkeit, WolkenTypEnum wolkenTyp,
            Float regenwahrscheinlichkeit, Ort ort, Calendar datum, Float avgTemperature, Float regenfall,
            boolean wetterwarnung, Long id, Calendar erstelltAm) {
        super(ort, datum, avgTemperature, regenfall, wetterwarnung, id, erstelltAm);
        this.minTemperatur = minTemperatur;
        this.maxTemperatur = maxTemperatur;
        this.windgeschwindigkeit = windgeschwindigkeit;
        this.wolkenTyp = wolkenTyp;
        this.regenwahrscheinlichkeit = regenwahrscheinlichkeit;
    }

    @Override
    public String toString() {
        return super.toString() + "\n" + "DetailWettervorhersage{"
                + "minTemperatur=" + minTemperatur + ", maxTemperatur="
                + maxTemperatur + ", windgeschwindigkeit=" + windgeschwindigkeit
                + ", wolkenTyp=" + wolkenTyp + ", regenwahrscheinlichkeit="
                + regenwahrscheinlichkeit + '}';
    }

}
