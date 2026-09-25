import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del algoritmo Ramer-Douglas-Peucker (RDP).
 * Su función es recibir el contorno de una figura geométrica 
 * (todos sus pixeles) y quita los puntos que sobran, quedándonos
 * únicamente con las esquinas reales (vértices estructurales).
 */

// CRÉDITOS: 
// La idea base de la recursividad la tomamos del proyecto "series-reducer" 
// de LukaszWiktor (Github: https://github.com/LukaszWiktor/series-reducer).
// El código se adaptó para operar con java.awt.Point y simplificamos 
// la matemática para que funcione mejor con nuestros pixeles.

// MINI EXPLICACIÓN:
// La línea base: Trazamos una recta imaginaria desde el primer punto hasta el último.
// Medir el error: Checamos qué tan lejos está cada punto de en medio respecto a esa línea.
// Vemos si es una recta: Si la distancia máxima no pasa de nuestro límite (epsilon), asumimos 
// que todo ese tramo es una línea recta y borramos los puntos interiores.
// Romper y repetir: Si un punto está muy lejos de la línea, entonces es una esquina y partimos 
// el contorno justo en esa esquina y volvemos a hacer lo mismo para cada mitad.


public class FIltroRDP{
    
    /**
     * Simplifica la orilla de la figura, quitando pixeles extra pero manteniendo la forma.
     * @param puntos  Lista con todos los pixeles que forman el borde de la figura (perímetro).
     * @param epsilon Nuestro límite. Si lo subes, borra más puntos; si lo bajas, es más estricto.
     * @return Una lista más corta que solo tiene los vértices significativos (esquinas).
     * @throws IllegalArgumentException Si el límite de tolerancia es negativo.
     */
    public List<Point> simplificarContorno(List<Point> puntos, double epsilon){
        // No tiene sentido tener una tolerancia negativa
        if(epsilon<0){
            throw new IllegalArgumentException("Epsilon no puede ser menor que cero :,v");
        }

        // Si tenemos menos de 3 puntos, no hay nada que simplificar porque ya es un segmento mínimo (si se puede evaluar)
        if(puntos==null||puntos.size()<3){
            return new ArrayList<>(puntos);
        }
        return reducir(puntos,epsilon);
    }

    /**
     * Método recursivo interno que hace parte la lista y la evalua.
     * @param puntos  Sublista de puntos a evaluar en la iteración actual.
     * @param epsilon Nuestro límite.
     * @return Sublista simplificada.
     */
    private List<Point> reducir(List<Point> puntos, double epsilon){
        Point inicio=puntos.get(0);
        Point fin=puntos.get(puntos.size()-1);

        int indMeutuLejos=0;
        double distMax=0.0;

        // Buscamos qué punto se aleja más de nuestra línea recta imaginaria
        for(int i=1; i<puntos.size()-1; i++ ){
            double distancia=calcularDistanciaOrtogonal(puntos.get(i),inicio,fin);
            if(distancia>distMax){
                distMax=distancia;
                indMeutuLejos=i;
            }
        }
        // CASO BASE: Si nadie se pasa de la tolerancia, decimos que esto ya es una recta. 
        // Descartamos el "relleno" y devolvemos solo el inicio y el fin.
        if(distMax<=epsilon){
            List<Point> resultado=new ArrayList<>();
            resultado.add(inicio);
            resultado.add(fin);
            return resultado;
        }

        // PASO RECURSIVO: Encontramos una esquina de verdad, partimos la lista en dos.
        List<Point> mitadDerecha=reducir(puntos.subList(indMeutuLejos, puntos.size()), epsilon);
        List<Point> mitadIzquierda=reducir(puntos.subList(0, indMeutuLejos+1), epsilon);

        // Juntamos las dos mitades. 
        // Nos saltamos el primer punto de la derecha (subList desde 1) 
        // para no repetir la esquina donde acabamos de cortar.
        List<Point> resultado=new ArrayList<>(mitadIzquierda);
        resultado.addAll(mitadDerecha.subList(1, mitadDerecha.size()));
        return resultado;
    }

    /**
     * Calcula la distancia perpendicular (en ángulo recto) de un punto suelto hacia la línea base.
     * @param puntoEvaluado El punto del que se quiere conocer la desviación.
     * @param inicio        El punto de inicio de la recta base.
     * @param fin           El punto final de la recta base.
     * @return La distancia perpendicular exacta.
     */

    // Para no usar senos ni cosenos, calculamos el área del triángulo que forman los 3 puntos 
    // y de ahí despejamos la altura (trucazo). Esa altura es la distancia que buscamos.
    // Fórmula aplicada: Altura = (Doble del Área) / Base
    private  double calcularDistanciaOrtogonal(Point p, Point inicio, Point fin){

        // Sacamos las medidas de nuestra línea base (la secante)
        double diferenciaYBase=inicio.getY()-fin.getY();
        double diferenciaXBase=inicio.getX()-fin.getX();
        double longitudBase=Math.sqrt((diferenciaXBase*diferenciaXBase)+(diferenciaYBase*diferenciaYBase));

        // Por si acaso nos pasan un inicio y un fin que son exactamente el mismo pixel (protección contra división por cero)
        if(longitudBase==0.0){
            double distanciaYalInicio=p.getY()-inicio.getY();
            double distanciaXalInicio=p.getX()-inicio.getX();
            return Math.sqrt((distanciaXalInicio * distanciaXalInicio)+(distanciaYalInicio*distanciaYalInicio));
        }

        // Calculamos el valor constante de la recta
        double cruceInicioX_FinY=inicio.getX()*fin.getY();
        double cruceFinX_InicioY=fin.getX()*inicio.getY();
        double terminoConstanteRecta=cruceInicioX_FinY-cruceFinX_InicioY;

        // Aplicamos la fórmula: Altura = (Doble del Área) / Base
        double dobleAreaTriangulo=Math.abs((diferenciaYBase*p.getX())-(diferenciaXBase*p.getY())+(terminoConstanteRecta));

        return dobleAreaTriangulo/longitudBase;
    }
}