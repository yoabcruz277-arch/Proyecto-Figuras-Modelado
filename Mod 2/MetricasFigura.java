public class MetricasFigura{
    private int numVertices;
    private double perimetro;
    private double area;
    private double factCircularidad;

    public MetricasFigura(int numVertices, double perimetro, double area, double factCircularidad ){
        this.numVertices=numVertices;
        this.perimetro=perimetro;
        this.area=area;
        this.factCircularidad=factCircularidad;
    }
    public int getNumVertices() {
    return numVertices;
    }

    public double getPerimetro() {
        return perimetro;
    }

    public double getArea() {
        return area;
    }

    public double getFactCircularidad() {
        return factCircularidad;
    }
}
