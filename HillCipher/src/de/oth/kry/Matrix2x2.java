package de.oth.kry;

public class Matrix2x2 extends AbstractMatrix {

    private int a11;
    private int a12;
    private int a21;
    private int a22;

    public Matrix2x2(int a11, int a12, int a21, int a22, int modulo) {
        super(modulo);
        this.a11 = a11;
        this.a12 = a12;
        this.a21 = a21;
        this.a22 = a22;
    }

    public int getA11() {
        return a11;
    }

    public int getA12() {
        return a12;
    }

    public int getA21() {
        return a21;
    }

    public int getA22() {
        return a22;
    }

    public void setA11(int a11) {
        int mod = a11 % this.modulo;
        if (mod < 0) {
            mod += this.modulo;
        }
        this.a11 = mod;
    }

    public void setA12(int a12) {
        int mod = a12 % this.modulo;
        if (mod < 0) {
            mod += this.modulo;
        }
        this.a12 = mod;
    }

    public void setA21(int a21) {
        int mod = a21 % this.modulo;
        if (mod < 0) {
            mod += this.modulo;
        }
        this.a21 = mod;
    }

    public void setA22(int a22) {
        int mod = a22 % this.modulo;
        if (mod < 0) {
            mod += this.modulo;
        }
        this.a22 = mod;
    }

    @Override
    public String toString() {
        return String.format("{a11 = %2d, a12 = %2d, a21 = %2d, a22 = %2d}", a11, a12, a21, a22);
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
        final Matrix2x2 other = (Matrix2x2) obj;
        if (this.a11 != other.a11) {
            return false;
        }
        if (this.a12 != other.a12) {
            return false;
        }
        if (this.a21 != other.a21) {
            return false;
        }
        if (this.a22 != other.a22) {
            return false;
        }
        return true;
    }

    public int computeDeterminante() {
        return a11 * a22 - a21 * a12;
    }

    public int computeDeterminanteModus() {
        int det = computeDeterminante() % this.modulo;
        if (det < 0) {
            det += this.modulo;
        }
        return det;
    }

    public boolean determinanteInvertierbar() {

        int det = this.computeDeterminanteModus();

        // Prüfe, ob Determinante gleich Null --> Nicht invertierbar
        if (det == 0) {
            return false;
        }

        // Prüfe, ob es inverses zur Determinante gibt
        for (int i = 1; i < this.modulo; i++) {

            int check = i * det % this.modulo;

            // Remainer in Modulus umwandeln
            if (check < 0) {
                check += this.modulo;
            }

            // Inverses existiert
            if (check == 1) {
                return true;
            }
        }

        return false;

    }

}
