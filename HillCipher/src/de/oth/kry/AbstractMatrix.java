package de.oth.kry;

public class AbstractMatrix {

    public AbstractMatrix(int modulo) {
        this.modulo = modulo;
    }

    int[][] size;
    int modulo;

    public int[][] getSize() {
        return size;
    }

    public int getModulo() {
        return modulo;
    }

    public void setSize(int[][] size) {
        this.size = size;
    }

    public void setModulo(int modulo) {
        this.modulo = modulo;
    }

}
