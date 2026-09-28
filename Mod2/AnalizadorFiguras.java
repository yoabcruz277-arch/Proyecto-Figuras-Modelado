/**
 * Esta clase contiene las funciones auxiliares: 
 * analizar: calcula y devuelve las métricas necesarias para ClasificarFiguras
 * calcularPerimetro: calcula el perímetro recorriendo
 */

package Mod2;
import Mod1.DatosFigura;
import Mod1.ProcesadorImagen;
import java.awt.Point;
import java.util.List;
import java.util.ArrayList;

public class AnalizadorFiguras{

    //Variable estática para simplificación de imagenes
    private static final double EPSILON = 2.0;

    //Instancia del filtro RDP
    private FIltroRDP filtro;

    /**
     * Constructor de AnalizadorFiguras
     * Inicializa la instancia del filtro RDP para la simplificación de contornos
     */
    public AnalizadorFiguras(){
        this.filtro = new FIltroRDP();
    }


    /**
     * Función que calcula y devuelve las métricas necesarias para la clasificación de figuras
     * @param datos, Objeto con la información obtenida del Módulo 1
     * @return, Objeto con las métricas de la figura
     * @throws IllegalArgumentException si el objeto (los datos recibidos) es nulo.
     */
    public MetricasFigura analizar(DatosFigura datos){
        if(datos == null){
            throw new IllegalArgumentException("Los datos de la figura no pueden ser nulos");
        }

        //Obtenemosm el área directamente con los datos de Módulo 1
        double area = datos.getArea().size();

        //Guardamos el contorno original (De Módulo 1) en la lista contornoOG
        List<Point> contornoOG = datos.getContorno();

        //CÓDIGO NUEVO
        List<Point> contornoCerrado = new ArrayList<>(contornoOG);
        if(!contornoCerrado.isEmpty() && !contornoCerrado.get(0).equals(contornoCerrado.get(contornoCerrado.size()-1))){
            contornoCerrado.add(contornoCerrado.get(0));    //Unimos el inicio de la figura con el final
        }


        //Aplicamos el algoritmo de simplificación para obtener el número de vértices
        List<Point> figuraSimplificada = filtro.simplificarContorno(contornoOG, EPSILON);

        //Guardamos el número de vértices en numVertices obteniendo el tamaño de la lista anteriormente simplificada
        int numVertices = figuraSimplificada.size();
        //Elimina el vertice adicional en caso de que lo haya
        if(numVertices > 1 && figuraSimplificada.get(0).equals(figuraSimplificada.get(numVertices - 1))){
            numVertices--;
        }
        //Llamamos a la función auxiliar calcularPerimetro
        double perimetro = calcularPerimetro(figuraSimplificada);
        //Calculamos el facotr de circularidad
        double factCircularidad = 0.0;
        if(perimetro > 0 ){
            factCircularidad = ((4 * Math.PI * area)/(perimetro * perimetro));
        }

        //Devolvemos todos los datos listos para ser procesados en "ClasificarFiguras"
        return new MetricasFigura(numVertices, perimetro, area, factCircularidad);

    }

    /**
     * Calcula el perímetro total de una figura al sumar la longitud de todos los lados
     * @param puntos, lista de puntos simplificados = vértices de la figura
     * @return la longitud total del perímetro en pixeles
     */
    private double calcularPerimetro(List<Point> puntos){
        double perimetro = 0.0;
        int n = puntos.size();

        for(int i = 0; i < n; i++){
            Point p1 = puntos.get(i);
            Point p2 = puntos.get((i + 1) % n);
            perimetro += p1.distance(p2);
        }
        return perimetro;
    }
}