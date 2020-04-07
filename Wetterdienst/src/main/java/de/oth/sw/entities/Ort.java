package de.oth.sw.entities;

import de.oth.sw.entities.enums.BundeslandEnum;
import de.oth.sw.entities.enums.LandEnum;
import java.util.Calendar;
import java.util.Objects;
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
public class Ort extends AbstractEntity {

    @Getter
    @Setter
    private String ortName;

    @Getter
    @Setter
    private LandEnum land;

    @Getter
    @Setter
    private BundeslandEnum bundesland;

// Default Constructor for persistence logic
    public Ort() {
    }

    public Ort(String ortName, LandEnum land, BundeslandEnum bundesland, Long id, Calendar erstelltAm) {
        super(id);
        this.ortName = ortName;
        this.land = land;
        this.bundesland = bundesland;
    }

    @Override
    public String toString() {
        if (land == LandEnum.Deutschland) {
            return ortName + " [" + land + ", " + bundesland + "]";
        } else {
            return ortName + " [" + land + "]";
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.ortName);
        hash = 13 * hash + Objects.hashCode(this.land);
        hash = 13 * hash + Objects.hashCode(this.bundesland);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Ort other = (Ort) obj;
        if (!Objects.equals(this.ortName, other.ortName)) {
            return false;
        }
        if (this.land != other.land) {
            return false;
        }
        if (this.bundesland != other.bundesland) {
            return false;
        }
        return true;
    }

}
