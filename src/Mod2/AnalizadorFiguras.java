/**
 * Esta clase contiene las funciones auxiliares: 
 * analizar: calcula y devuelve las métricas necesarias para ClasificarFiguras
 * calcularPerimetro: calcula el perímetro recorriendo
 */

package Mod2;
import Mod1.DatosFigura;
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
        
        //utiliza el nuevo metodo (ultimo) y ordena la lista del contorno.
        List<Point> contornoSecuencial = ordenaContorno(contornoOG);

        //Ya esta ordenada la lista 
        List<Point> contornoCerrado = new ArrayList<>(contornoSecuencial);
        if(!contornoCerrado.isEmpty() && !contornoCerrado.get(0).equals(contornoCerrado.get(contornoCerrado.size()-1))){
            contornoCerrado.add(contornoCerrado.get(0));    //Unimos el inicio de la figura con el final
        }


        //Aplicamos el algoritmo de simplificación para obtener el número de vértices
        List<Point> figuraSimplificada = filtro.simplificarContorno(contornoCerrado, EPSILON);

        boolean limpiando = true;
        // CODIGO NUEVO: Limpia los vertices de los triangulos rectangulos. (solo aplica si tienen mas de 3)
        while(limpiando && figuraSimplificada.size() > 3){
            limpiando = false;
            for (int i = 0; i < figuraSimplificada.size(); i++) {
                Point p1 = figuraSimplificada.get(i);
                Point p2 = figuraSimplificada.get((i + 1) % figuraSimplificada.size());
                Point p3 = figuraSimplificada.get((i + 2) % figuraSimplificada.size());

                double areaFalsa = Math.abs(p1.x * (p2.y - p3.y) + p2.x * (p3.y - p1.y) + p3.x * (p1.y - p2.y)) / 2.0;

                // Si el area es menor a 30 px, significa que p2 no es una esquina real,
                // sino un vertice falso en medio de una línea recta.
                if (areaFalsa < 30.0) { 
                    figuraSimplificada.remove((i + 1) % figuraSimplificada.size());
                    limpiando = true;
                    break;
                }
            }
        }
        
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

    // Los 8 vecinos de un pixel, en sentido horario (en pantalla, y crece hacia abajo):
    // E, SE, S, SO, O, NO, N, NE
    private static final int[] DX8={ 1, 1, 0, -1, -1, -1, 0, 1 };   // nuevo uwu
    private static final int[] DY8={ 0, 1, 1, 1, 0, -1, -1, -1 };   // nuevo uwu

    /**
     * Ordena los puntos del contorno RECORRIENDO la orilla de la figura (trazado de contorno
     * por vecindad de Moore), de modo que puntos consecutivos de la lista sean pixeles vecinos.
     *
     * Antes se usaba "el punto no visitado mas cercano", pero ese metodo se queda atascado en las
     * esquinas (sobre todo en bases planas y en figuras pegadas al borde de la imagen): deja pixeles
     * atras, luego "salta" cientos de pixeles hasta ellos y esos saltos el RDP los cuenta como
     * vertices falsos (un triangulo salia con ¡¡¡¡¡¡¡¡¡¡¡6-7 vertices!!!!! y se clasificaba como "Otros").
     *
     * @param contornoDesordenado lista con todos los puntos del contorno (sin orden).
     * @return los puntos del contorno ordenados a lo largo de la orilla.
     */
    private List<Point> ordenaContorno(List<Point> contornoDesordenado){
        if (contornoDesordenado == null || contornoDesordenado.isEmpty()){
            return new ArrayList<>();
        }
 
        java.util.Set<Point> conjunto = new java.util.HashSet<>(contornoDesordenado);
 
        // Punto de partida: el de arriba y, si hay empate, el de mas a la izquierda.
        // Asi se garantiza que su vecino del oeste NO es parte de la figura.
        Point inicio = contornoDesordenado.get(0);
        for (Point p : contornoDesordenado){
            if (p.y < inicio.y || (p.y == inicio.y && p.x < inicio.x)){
                inicio = p;
            }
        }
 
        List<Point> ordenado = new ArrayList<>();
        ordenado.add(inicio);
 
        Point actual = inicio;
        int dirInicioBusqueda = 5;                       // empezamos a buscar desde el NO (venimos "del oeste")
        int limite = conjunto.size() * 8 + 16;           // seguro contra ciclos infinitos
 
        for (int paso = 0; paso < limite; paso++){
            Point siguiente = null;
            int dirEncontrada = -1;
 
            // Se giran los 8 vecinos en sentido horario hasta toparse con otro punto del contorno.
            for (int k = 0; k < 8; k++){
                int d = (dirInicioBusqueda + k) % 8;
                Point candidato = new Point(actual.x + DX8[d], actual.y + DY8[d]);
                if (conjunto.contains(candidato)){
                    siguiente = candidato;
                    dirEncontrada = d;
                    break;
                }
            }
 
            // Pixel aislado (figura de 1 solo punto): no hay nada que recorrer.
            if (siguiente == null){
                break;
            }
            // Regresamos al inicio: la vuelta esta completa.
            if (siguiente.equals(inicio)){
                break;
            }
 
            ordenado.add(siguiente);
            actual = siguiente;
            // La siguiente busqueda arranca "dos lugares atras" de la direccion en que nos movimos.
            dirInicioBusqueda = (dirEncontrada + 6) % 8;
        }
        return ordenado;
    }
}