import java.awt.Point;
import java.util.List;

public class AnalizadorFiguras{

    private FiltroRDP filtro;
    public AnalizadorGeometria(){
        this.filtro=new FiltroRDP();
    }


    public MetricasFigura analizador(Object figura){
        //para probar
        double areaP=100.0;
        double perimetroP=40.0;
        int verticesP=4;
        double circularidadP= 0.777;


    
        //retorna datos inventados
        return new MetricasFigura(areaP, perimetroP, verticesP, circularidadP);

    }
}