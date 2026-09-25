import java.awt.Point;
import java.util.List;

public class AnalizadorFiguras{
    private static final double EPSILON = 2.0;

    private FIltroRDP filtro;

    public AnalizadorFiguras(){
        this.filtro=new FIltroRDP();
    }


    public MetricasFigura analizarFigura(List<Point> figura){
        List<Point> figuraSimplificada = filtro.simplificarContorno(figura, EPSILON);

        //para probar
        //double areaP=100.0;
        //double perimetroP=40.0;
        //int verticesP=4;
        //double circularidadP= 0.777;


    
        //retorna datos inventados
        return new MetricasFigura(areaP, perimetroP, verticesP, circularidadP);

    }
}