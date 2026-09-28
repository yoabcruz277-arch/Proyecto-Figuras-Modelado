package Mod2;
/**
 * Clase que almacena las métricas geométricas y características extraídas
 * de una figura geométrica para clasificarla después.
 */
public class MetricasFigura{
    private int numVertices;
    private double perimetro;
    private double area;
    private double factCircularidad;

    /**
     * Constructor que inicializa los atributos geométricos de la figura
     * @param numVertices --> Número de vértices detectados con el algoritmo RDP
     * @param perimetro --> Perímetro total del contorno de la figura en píxeles
     * @param area --> Área total de la fígura en píxeles
     * @param factCircularidad --> Factor de circularidad de la figura
     */
    public MetricasFigura(int numVertices, double perimetro, double area, double factCircularidad ){
        this.numVertices = numVertices;
        this.perimetro = perimetro;
        this.area = area;
        this.factCircularidad = factCircularidad;
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
