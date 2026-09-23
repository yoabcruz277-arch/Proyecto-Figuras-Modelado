/*
Este programa es una función que clasifica figuras de una imagen .bmp en 4 categorías según algunos datos previamente obtenidos:
Cuadrilateros
Triángulos
Círculos
Otros
*/
//mañana la acabo no esten mamando
public class ClasificadorFiguras{

    private char Clasificador(DatosFigura datos){
        if(esCirculo(datos)){
            return 'O';
        } else if(datos.getNumVertices()== 3) {
            return 'T';
        } else if(datos.getNumVertices() == 4) {
            return 'C';
        } else { 
            return 'X';
        }
        }//Aquí cierra la mierda de función



    private boolean esCirculo(DatosFigura datos){
        return true;
    }
}