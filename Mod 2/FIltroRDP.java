import java.util.ArrayList;
import java.util.List;
import java.awt.Point;

public class FIltroRDP{
    
    public List<Point> simplificarContorno(List<Point> puntos, double epsilon){
        if(epsilon<0){
            throw new IllegalArgumentException("Epsilon no puede ser menor que cero");
        }
        if(puntos==null||puntos.size()<3){
            return new ArrayList<>(puntos);
        }
        return reducir(puntos,epsilon);
    }
    private List<Point>
}