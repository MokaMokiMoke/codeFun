package de.oth.sw.entities;

import java.util.Calendar;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Maximilian Fries <maximilian.fries@st.oth-regensburg.de>
 */
@Entity
public class RegistrierterNutzer extends AbstractEntity {

    @Getter
    @Setter
    @Column(name = "nutzername", unique = true)
    private String nutzername;

    @Getter
    @Setter
    private String passwort;

    @Getter
    @Setter
    private String adminToken;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    private Ort heimatort;

    @Getter
    @Setter
    private boolean gesperrt;

    @Getter
    @Setter
    private boolean istAdmin;

    // Default Constructor for persistence logic
    public RegistrierterNutzer() {
    }

    public RegistrierterNutzer(String nutzername, String passwort, String adminToken,
            Ort heimatort, boolean gesperrt, boolean istAdmin, Long id, Calendar erstelltAm) {
        super(id);
        this.nutzername = nutzername;
        this.passwort = passwort;
        this.adminToken = adminToken;
        this.heimatort = heimatort;
        this.gesperrt = gesperrt;
        this.istAdmin = istAdmin;
    }

    @Override
    public String toString() {
        return nutzername;
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
        final RegistrierterNutzer other = (RegistrierterNutzer) obj;
        if (this.gesperrt != other.gesperrt) {
            return false;
        }
        if (this.istAdmin != other.istAdmin) {
            return false;
        }
        if (!Objects.equals(this.nutzername, other.nutzername)) {
            return false;
        }
        if (!Objects.equals(this.passwort, other.passwort)) {
            return false;
        }
        if (!Objects.equals(this.adminToken, other.adminToken)) {
            return false;
        }
        if (!Objects.equals(this.heimatort, other.heimatort)) {
            return false;
        }
        return true;
    }

    public boolean equalsCredentials(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RegistrierterNutzer other = (RegistrierterNutzer) obj;
        if (this.gesperrt != other.gesperrt) {
            return false;
        }
        if (!Objects.equals(this.nutzername, other.nutzername)) {
            return false;
        }
        if (!Objects.equals(this.passwort, other.passwort)) {
            return false;
        }
        return true;
    }

}
