/*
Esta clase es solo de prueba para hacer modificaciones a ClasificadorFiguras
(Borrar después)
 */
public class DatosFigura {

    private int numVertices;
    private double perimetro;
    private double area;
    private double factCircularidad;


    //Constructor temporal
    public DatosFigura(int numVertices, double perimetro, double area, double factCircularidad ){
        this.numVertices = numVertices;
        this.perimetro = perimetro;
        this.area = area;
        this.factCircularidad = factCircularidad;
    }


    //Getters para poder leer los datos
    public int getNumVertices(){
        return numVertices;
    }

    public double getPerimetro(){
        return perimetro;
    }

    public double getArea(){
        return area;
    }

    public double getFactCircularidad(){
        return factCircularidad;
    }
    
}
