package de.oth.sw.entities;

import java.util.Calendar;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Maximilian Fries <maximilian.fries@st.oth-regensburg.de>
 */
@MappedSuperclass
public class Wettervorhersage extends AbstractEntity {

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private Ort ort;

    @Getter
    @Setter
    @Temporal(TemporalType.DATE)
    private Calendar datum;

    @Getter
    @Setter
    private Float avgTemperatur;

    @Getter
    @Setter
    private Float regenfall;

    @Getter
    @Setter
    private boolean wetterwarnung;

    // Default Constructor for persistence logic
    public Wettervorhersage() {
        super();
    }

    public Wettervorhersage(Long id, Calendar created_on) {
        super(id);
    }

    public Wettervorhersage(Ort ort, Calendar datum, Float avgTemperature, Float regenfall, boolean wetterwarnung, Long id, Calendar erstelltAm) {
        super(id);
        this.ort = ort;
        this.datum = datum;
        this.avgTemperatur = avgTemperature;
        this.regenfall = regenfall;
        this.wetterwarnung = wetterwarnung;
    }

    @Override
    public String toString() {
        return super.toString() + "\n" + "Wettervorhersage{" + "ort=" + ort
                + ", datum=" + datum + ", avgTemperature=" + avgTemperatur
                + ", regenfall=" + regenfall + ", wetterwarnung=" + wetterwarnung + '}';
    }

}
