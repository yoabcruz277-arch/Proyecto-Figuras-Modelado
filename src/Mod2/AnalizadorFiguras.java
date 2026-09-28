/**
 * Esta clase contiene las funciones auxiliares: 
 * analizar: calcula y devuelve las métricas necesarias para ClasificarFiguras
 * calcularPerimetro: calcula el perímetro recorriendo
 */

package Mod2;
import Mod1.*;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;



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
        
        //CODIGO NUEVO: utilixa el nuevo metodo (ultimo) y ordena la lista del contorno.
        List<Point> contornoSecuencial = ordenaContorno(contornoOG);

        //Ya esta ordenada la lista 
        List<Point> contornoCerrado = new ArrayList<>(contornoSecuencial);
        if(!contornoCerrado.isEmpty() && !contornoCerrado.get(0).equals(contornoCerrado.get(contornoCerrado.size()-1))){
            contornoCerrado.add(contornoCerrado.get(0));    //Unimos el inicio de la figura con el final
        }


        //Aplicamos el algoritmo de simplificación para obtener el número de vértices
        List<Point> figuraSimplificada = filtro.simplificarContorno(contornoCerrado, EPSILON);

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

    /**
     * Ordena la lista que tiene a los puntos que consstituyen al contorno.
     * Clona la lista desordenada para que no mueran las coordenadas.
     * @param contornoDesordenado, es la lista que tiene a todos los puntos del contorno (desordenada).
     * @return una lista con todos los puntos del contorno ya ordenados. 
     */
    private List<Point> ordenaContorno(List<Point> contornoDesordenado){
        if (contornoDesordenado == null || contornoDesordenado.isEmpty()){
            return new ArrayList<>();
        }

        List<Point> ordenado = new ArrayList<>();
        List<Point> aux = new ArrayList<>(contornoDesordenado);

        // Saca al primer punto desordenado y es el punto de partida para ordenar todo lo demas
        // remove "elimina" el elemento de la lista por lo que ahora el punto 0 es el que era 1.
        Point actual = aux.remove(0);
        ordenado.add(actual);

        // El ciclo principal que no termina hasta que aux este vacia.
        // Se pone distanciaChica como el valor maximo para garantizar que la 
        // primera iteracion del for sea verdadera (ve el if) y se itere toda la lista.
        while (!aux.isEmpty()){
            int pixelCercano = 0;
            double distanciaChica = Double.MAX_VALUE; 

            // El for que iterara sobre toda la lista aux.
            // Saca al primer Point de aux y calcula la distancia (.distance) a el primer Point. 
            for (int i = 0; i < aux.size(); i++){
                Point pendiente = aux.get(i);
                double distancia = actual.distance(pendiente);

                // Hace la verificacion en caso de encontrar una distancia mas pequeña.
                // Se actualiza el pixel mas cercano.
                if (distancia < distanciaChica){
                distanciaChica = distancia;
                pixelCercano = i;
                }
            }

            // Una vez que se termina el for tenemos el indice del punto mas cercano a actual.
            // Ese putno pasa a ser el actual y se agrega a la lista de ordenados.
            actual = aux.remove(pixelCercano);
            ordenado.add(actual);
        }
        return ordenado;
    }
}