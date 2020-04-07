package de.oth.sw.entities;

import java.util.Calendar;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Maximilian Fries <maximilian.fries@st.oth-regensburg.de>
 */
@Entity
public class Veranstaltung extends AbstractEntity {

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String beschreibung;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private Ort ort;

    @Getter
    @Setter
    @Temporal(TemporalType.DATE)
    private Calendar von;

    @Getter
    @Setter
    @Temporal(TemporalType.DATE)
    private Calendar bis;

    // Default Constructor for persistence logic
    public Veranstaltung() {
    }

    public Veranstaltung(String name, String beschreibung, Ort ort, Calendar von, Calendar bis, Long id, Calendar erstelltAm) {
        super(id);
        this.name = name;
        this.beschreibung = beschreibung;
        this.ort = ort;
        this.von = von;
        this.bis = bis;
    }

    @Override
    public String toString() {
        return super.toString() + "\n" + "Veranstaltung{" + "name=" + name
                + ", beschreibung=" + beschreibung + ", ort=" + ort
                + ", von=" + von + ", bis=" + bis + '}';
    }

}
