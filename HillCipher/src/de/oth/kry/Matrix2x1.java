package de.oth.kry;

public class Matrix2x1 extends AbstractMatrix {

    private int p1;
    private int p2;

    public Matrix2x1(int p1, int p2, int modulo) {
        super(modulo);
        this.p1 = p1;
        this.p2 = p2;
    }

    public Matrix2x1(int mod) {
        super(mod);
    }

    public int getP1() {
        return p1;
    }

    public int getP2() {
        return p2;
    }

    public void setP1(int p1) {
        int mod = p1 % this.modulo;
        if (mod < 0) {
            mod += this.modulo;
        }
        this.p1 = mod;
    }

    public void setP2(int p2) {
        int mod = p2 % this.modulo;
        if (mod < 0) {
            mod += this.modulo;
        }
        this.p2 = mod;
    }

    @Override
    public String toString() {
        return String.format("{p1 = %2d, p2 = %2d}", p1, p2);
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
        final Matrix2x1 other = (Matrix2x1) obj;
        if (this.p1 != other.p1) {
            return false;
        }
        if (this.p2 != other.p2) {
            return false;
        }
        return true;
    }

}
