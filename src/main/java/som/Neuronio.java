package som;

public class Neuronio {

    private double x;
    private double y;
    private double[] peso;

    public Neuronio() {}
    
    public Neuronio( double x, double y, double[] peso ) {
        this.x = x;
        this.y = y;
        this.peso = peso;
    }
    
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double[] getPeso() {
        return peso;
    }

    public void setPeso(double[] peso) {
        this.peso = peso;
    }
    
    
}
