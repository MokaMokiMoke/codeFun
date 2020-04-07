public enum Dimension {

	KB(Math.pow(2, 10)),
    MB(Math.pow(2, 20)),
    GB(Math.pow(2, 30));
    
    private double size;
    
    private Dimension(double size) {
        this.size = size;
    }
    
    public double getSize() {
        return this.size;
    }
    
    public int getSizeAsInt() {
        return (int) this.size;
    }

}
