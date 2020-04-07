package de.oth.sw.entities.enums;

/**
 *
 * @author Maximilian Fries <maximilian.fries@st.oth-regensburg.de>
 */
public enum WolkenTypEnum {

    Schaefchenwolke("Schaefchenwolke"),
    Gewitterwolke("Gewitterwolke"),
    Regenwolke("Regenwolke"),
    Wolkenfrei("Wolkenfrei"),
    Schneewolke("Schneewolke");

    private final String bild;

    private WolkenTypEnum(final String bild) {
        this.bild = bild;
    }

    @Override
    public String toString() {
        return this.bild;
    }

    public String getBild() {
        return bild;
    }

}
