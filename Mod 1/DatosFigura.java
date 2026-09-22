import java.awt.Point; 
import java.util.List;
/** 
* Esta clase es la que se encarga de almacenar todos los datos de la imagen,
* para que se pueda acceder a ellos a la hora de ser clasificados.
* IMPORTANTE LEELO MONO
* Importe la biblioteca Point para representar los puntos de las coordenadas.
*/
public class DatosFigura {
    private String color;
    private List<Point> area;
    private List<Point> contorno;

    /**
     * Clase construcotra se encarga de settear los valores y varios getters para cuando se quiera sacar el valor gg.
     */
    public DatosFigura(String color, List<Point> area, List<Point> contorno){
        this.color = color;
        this.area = area;
        this.contorno = contorno;
    }

    public String getColor(){
        return color; 
    }

    public List<Point> getArea(){
        return area;
    }

    public List<Point> getContorno(){
        return contorno;
    }
}