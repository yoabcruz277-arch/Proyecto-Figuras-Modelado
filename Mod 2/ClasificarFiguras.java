/*
Esta clase contiene a las funciones auxiliares:
-clasificador: clasifica logicamente los datos en categorias como T(Triángulo),C(Cuadrado),O(Círculo)...
-esCirculo: compara la constante de circularidad y si es mayor devuelve true (Pues es un círculo)
*/

public class ClasificarFiguras{

    //Constantes para las categorias de las figuras geométricas
    public static final char CIRCULO = 'O';
    public static final char TRIANGULO = 'T';   
    public static final char CUADRADO = 'C';
    public static final char OTROS = 'X';

    //Umbral para calcular si la figura se aproxima a un círculo
    private static final double UMBRAL_CIRCULARIDAD = 0.88; 





    /**
     * Clasifica una figura en O, T, C u X según sus métricas precalculadas
     * @param datos, métricas precalculadas en clases anteriores
     * @return caracter correspondiente a la categoria de la figura
     * @throws IllegalArgumentException si las metricas son nulas o imposibles, como vértices negativos
     */
    public char clasificador(MetricasFigura datos){
        if(datos == null){
            throw new IllegalArgumentException("Los datos de la figura no pueden ser nulos");
        }
        if(datos.getNumVertices() < 0){
            throw new IllegalArgumentException("El número de vértices no puede ser negativo");
        }

        if(esCirculo(datos)){
            return CIRCULO;
        } else if(datos.getNumVertices() == 3) {
            return TRIANGULO;
        } else if(datos.getNumVertices() == 4) {
            return CUADRADO;
        } else { 
            return OTROS;
        }
        }


        /**
         * Evalua si una figura es un círculo basado en su factor de circularidad previamente calculado
         * @param datos
         * @return
         */
    private boolean esCirculo(MetricasFigura datos){
        return datos.getFactCircularidad() >= UMBRAL_CIRCULARIDAD;
    }
}