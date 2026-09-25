import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class FIltroRDP{
    
    public List<Point> simplificarContorno(List<Point> puntos, double epsilon){
        if(epsilon<0){
            throw new IllegalArgumentException("Epsilon no puede ser menor que cero :,v");
        }
        if(puntos==null||puntos.size()<3){
            return new ArrayList<>(puntos);
        }
        return reducir(puntos,epsilon);
    }
    private List<Point> reducir(List<Point> puntos, double epsilon){
        Point inicio=puntos.get(0);
        Point fin=puntos.get(puntos.size()-1);

        int indMeutuLejos=0;
        double distMax=0.0;

        for(int i=1; i<puntos.size()-1; i++ ){
            double distancia=calcularDistanciaOrtogonal(puntos.get(i),inicio,fin);
            if(distancia>distMax){
                distMax=distancia;
                indMeutuLejos=i;
            }
        }
        if(distMax<=epsilon){
            List<Point> resultado=new ArrayList<>();
            resultado.add(inicio);
            resultado.add(fin);
            return resultado;
        }

        List<Point> mitadDerecha=reducir(puntos.subList(indMeutuLejos, puntos.size()), epsilon);
        List<Point> mitadIzquierda=reducir(puntos.subList(0, indMeutuLejos+1), epsilon);

        List<Point> resultado=new ArrayList<>(mitadIzquierda);
        resultado.addAll(mitadDerecha.subList(1, mitadDerecha.size()));
        return resultado;
    }

    private  double calcularDistanciaOrtogonal(Point p, Point inicio, Point fin){
        double diferenciaYBase=inicio.getY()-fin.getY();
        double diferenciaXBase=inicio.getX()-fin.getX();
        double longitudBase=Math.sqrt((diferenciaXBase*diferenciaXBase)+(diferenciaYBase*diferenciaYBase));

        if(longitudBase==0.0){
            double distanciaYalInicio=p.getY()-inicio.getY();
            double distanciaXalInicio=p.getX()-inicio.getX();
            return Math.sqrt((distanciaXalInicio * distanciaXalInicio)+(distanciaYalInicio*distanciaYalInicio));
        }

        double cruceInicioX_FinY=inicio.getX()*fin.getY();
            double cruceFinX_InicioY=fin.getX()*inicio.getY();
            double terminoConstanteRecta=cruceInicioX_FinY-cruceFinX_InicioY;

            double dobleAreaTriangulo=Math.abs((diferenciaYBase*p.getX())-(diferenciaXBase*p.getY())+(terminoConstanteRecta));

            return dobleAreaTriangulo/longitudBase;
    }
}