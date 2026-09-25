/*
Esta clase contiene a las funciones auxiliares:
-clasificador: clasifica logicamente los datos en categorias como T(Triángulo),C(Cuadrado),O(Círculo)...
-esCirculo: compara la constante de circularidad y si es mayor devuelve true (Pues es un círculo)
*/
//mañana la acabo no esten mamando
public class ClasificadorFiguras{
    private static final double UMBRAL_CIRCULARIDAD = 0.88; //umbral de circularidad aproximado para círculo

    public char clasificador(DatosFigura datos){
        if(esCirculo(datos)){
            return 'O';
        } else if(datos.getNumVertices() == 3) {
            return 'T';
        } else if(datos.getNumVertices() == 4) {
            return 'C';
        } else { 
            return 'X';
        }
        }//Aquí cierra la mierda de función



    private boolean esCirculo(DatosFigura datos){
        return datos.getFactCircularidad() >= UMBRAL_CIRCULARIDAD;
    }
}