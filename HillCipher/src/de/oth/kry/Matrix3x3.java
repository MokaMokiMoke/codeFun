package de.oth.kry;

public class Matrix3x3 extends AbstractMatrix {

    private int a11;
    private int a12;
    private int a13;
    private int a21;
    private int a22;
    private int a23;
    private int a31;
    private int a32;
    private int a33;

    public Matrix3x3(int a11, int a12, int a13, int a21, int a22, int a23,
            int a31, int a32, int a33, int modulo) {
        super(modulo);
        this.a11 = a11;
        this.a12 = a12;
        this.a13 = a13;
        this.a21 = a21;
        this.a22 = a22;
        this.a23 = a23;
        this.a31 = a31;
        this.a32 = a32;
        this.a33 = a33;
    }

    public int getA11() {
        return a11;
    }

    public int getA12() {
        return a12;
    }

    public int getA13() {
        return a13;
    }

    public int getA21() {
        return a21;
    }

    public int getA22() {
        return a22;
    }

    public int getA23() {
        return a23;
    }

    public int getA31() {
        return a31;
    }

    public int getA32() {
        return a32;
    }

    public int getA33() {
        return a33;
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

    public void setA13(int a13) {
        int mod = a13 % this.modulo;
        if (mod < 0) {
            mod += this.modulo;
        }
        this.a13 = mod;
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

    public void setA23(int a23) {
        int mod = a23 % this.modulo;
        if (mod < 0) {
            mod += this.modulo;
        }
        this.a23 = mod;
    }

    public void setA31(int a31) {
        int mod = a31 % this.modulo;
        if (mod < 0) {
            mod += this.modulo;
        }
        this.a31 = mod;
    }

    public void setA32(int a32) {
        int mod = a22 % this.modulo;
        if (mod < 0) {
            mod += this.modulo;
        }
        this.a32 = mod;
    }

    public void setA33(int a33) {
        int mod = a33 % this.modulo;
        if (mod < 0) {
            mod += this.modulo;
        }
        this.a33 = mod;
    }

    @Override
    public String toString() {
        return String.format("{a11 = %2d, a12 = %2d, a13 = %2d, "
                + "a21 = %2d, a22 = %2d, a23 = %2d,"
                + "a31 = %2d, a32 = %2d, a33 = %2d}", a11, a12, a13, a21, a22,
                a23, a31, a32, a33);
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
        final Matrix3x3 other = (Matrix3x3) obj;
        if (this.a11 != other.a11) {
            return false;
        }
        if (this.a12 != other.a12) {
            return false;
        }
        if (this.a13 != other.a13) {
            return false;
        }
        if (this.a21 != other.a21) {
            return false;
        }
        if (this.a22 != other.a22) {
            return false;
        }
        if (this.a23 != other.a23) {
            return false;
        }
        if (this.a31 != other.a31) {
            return false;
        }
        if (this.a32 != other.a32) {
            return false;
        }
        if (this.a33 != other.a33) {
            return false;
        }
        return true;
    }

    public int computeDeterminante() {
        return 0;
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
