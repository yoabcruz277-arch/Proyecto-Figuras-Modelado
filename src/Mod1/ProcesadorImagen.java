package Mod1;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import javax.imageio.ImageIO;

/**
 * ProcesadorImagen, se encarga de leer la imagen, "separar" el fondo de la figura para distinguirlas
 * y convertirla en coordenadas que se devolvéran después.
 */
public class ProcesadorImagen {
    private BufferedImage imagen;
    private boolean[][] visitados;
    private int colorFondo;

    // Distancia máxima (suma de |dR|+|dG|+|dB|) entre dos píxeles vecinos para
    // considerar que pertenecen a la misma figura. 
    // Si es mayor, se trata de otra figura que está pegada y ese píxel pasa a ser contorno.
    private static final int TOLERANCIA_COLOR = 160;
 
    // Componentes con menos píxeles que esto se descartan.
    private static final int AREA_MINIMA = 400;

    /**
     * Constructor vacío
     */
    public ProcesadorImagen() {
    }

    /**
     * Como se pensó:
     * La idea inicial de la clase era que los metodos provados hicieran
     * la chamba por separado, uno se encargaria de cargar la imagen, otro de separar el fondo de las figuras, etc.
     * Asi en este metodo publico (al que Sergio y Johan pueden acceder) solo le quedaria utilizar los metodos
     * y meterlos a una lisa para tener todas las coordenadas que necesitan.
     * 
     * En resumen explora todo el arreglo booleano de coordenadas, lo ordena con separarFondoBFS,
     * y a las coordenadas que sigan en false (las figuras) por medio de extraerTodasLasFiguras 
     * "empaqueta" las coordenas de area y contorno que necesitan mis compañeros y se meten en la lista de obj DatosFifura :).
     */
    public List<DatosFigura> procesador(String imagenBmp) {
        List<DatosFigura> resultado = new ArrayList<>();
       
        try{
            cargarImagen(imagenBmp);
            separarFondoBFS();

            resultado = extraerTodasLasFiguras();

        }  catch(Exception e){
            System.out.println("Esta mal, en algo :v" + e.getMessage());
        }

        return resultado;
    }

    /**
     * Carga el archivo de imagen desde el sistema de archivos e inicializa estructuras de trabajo
     * @param rutaArchivo, ruta local del archivo de la imagen a procesar
     * @throws Exception Si ocurre un error de lectura de archivo o si el formato no es compatible
     */
    private void cargarImagen(String rutaArchivo) throws Exception {
        File archivo = new File(rutaArchivo);
        imagen = ImageIO.read(archivo);

        int anchoImagen = imagen.getWidth();
        int alturaImagen = imagen.getHeight();

        visitados = new boolean[anchoImagen][alturaImagen];
        colorFondo = detectaColorFondo();
    }

    /**
     * Como se pensó:
     * El problema inicial es que como las figuras se pueden dispiner sobre la imagen de forma arbitraria,
     * como sabriamos que no van a tocar una esquina de la imagen (como (0,0)), entonces primero se penso 
     * en comparar las 4 esquinas, pero que pasaria si una figura toca 2 esquinas ? Pues la ultima solucion
     * fue recorrer todo el perimetro y con ayuda de una tabla hash identificar el color que sale mas veces.
     */
    private int detectaColorFondo(){
        Map<Integer, Integer> colores = new HashMap<>();
        int ancho = imagen.getWidth();
        int largo = imagen.getHeight();

        // Se recorre el perimetro completo de la imagen con los 4 fors.
        // Pasa rgb como key y como valor el numero de veces que aparece key mas 1.
        for (int x = 0; x < ancho; x++){
            int rgb = imagen.getRGB(x,0);
            colores.put(rgb, colores.getOrDefault(rgb, 0) + 1);
        }
        
        for (int y = 1; y < largo; y++){
            int rgb = imagen.getRGB(0, y);
            colores.put(rgb, colores.getOrDefault(rgb, 0) + 1);
        }

        for (int x = 1; x < ancho; x++){
            int rgb = imagen.getRGB(x, largo - 1);
            colores.put(rgb, colores.getOrDefault(rgb, 0) + 1);
        }

        for (int y = 1; y < largo; y++){
            int rgb = imagen.getRGB(ancho - 1, y);
            colores.put(rgb, colores.getOrDefault(rgb, 0) + 1);
        }

        // hace un stream y compara al mas grande por su value, los guarda en maximo.
        int colorMasFrecuente = 0;
        Optional<Map.Entry<Integer, Integer>> maximo = colores.entrySet().stream().max(Map.Entry.comparingByValue());
        if (maximo.isPresent()){
            colorMasFrecuente = maximo.get().getKey();
        }
        return colorMasFrecuente;
    }

    /**
     * Como se pensó: 
     * Se tenia que separar el fondo de las figuras usando "Flood fill" que basicamente
     * toma una coordenada y a partir de ahi empieza a recorrer a sus vecinos de forma que
     * esquiva las figuraa, esto se ve reflejado en el arreglo "visitados" que se marca con
     * true cada que encuentre fondo. Para la busqueda se ocupo una cola que ira almacenando las 
     * coordenadas de los pixeles que sean fondo, termina el ciclo hasya que la cola este vacia.
     * Al inicio se recorre todo el perimetro de la imagen para evitar el caso donde una o mas 
     * figuras cortan la imagen en 2.
     */
    private void separarFondoBFS() {
        Queue<Point> cola = new LinkedList<>();
        int colorF = detectaColorFondo();
        int ancho = imagen.getWidth();
        int largo = imagen.getHeight();

        // For's para recorrer los pixeles de la orilla de la imagen,
        // esto soluciona el problema de que haya una figura que parta en 2 o más a la imagen.
        for (int x = 0; x < ancho; x++){
            int rgb = imagen.getRGB(x,0); 
            if (colorF == rgb){
                cola.add(new Point(x,0));
                visitados[x][0] = true;
            }
        }

        for (int y = 1; y < largo; y++){
            int rgb = imagen.getRGB(0, y); 
            if (colorF == rgb){
                cola.add(new Point(0, y));
                visitados[0][y] = true;
            }
        }

        for (int x = 1; x < ancho; x++){
            int rgb = imagen.getRGB(x, largo - 1); 
            if (colorF == rgb){
                cola.add(new Point(x,largo - 1));
                visitados[x][largo-1] = true;
            }
        }

        for (int y = 1; y < largo; y++){
            int rgb = imagen.getRGB(ancho - 1, y); 
            if (colorF == rgb){
                cola.add(new Point(ancho - 1, y));
                visitados[ancho-1][y] = true;
            }
        }

        // Ciclo principal que recorre a los vecinos de el pixel que sacamos de la cola y los revisa,
        // así se sabe cual es fondo y cual no.
        // El primer elemento de la cola ya sabemos que es fondo y ya esta marcado como true.
        while (cola.isEmpty() != true){
            Point mainPixel = cola.poll();
            // Crea los cuatro puntos alrededor del punto que estaba en la cola.
            Point izq = new Point(mainPixel.x - 1, mainPixel.y);
            Point der = new Point(mainPixel.x + 1, mainPixel.y);
            Point arriba = new Point(mainPixel.x, mainPixel.y + 1);
            Point abajo = new Point(mainPixel.x, mainPixel.y - 1);

            // Revisa a todos los vecinos para ver cuales son fondo o no.
            if (izq.x >= 0 && izq.y >= 0 && izq.x < ancho && izq.y < largo && imagen.getRGB(izq.x, izq.y) == colorF && visitados[izq.x][izq.y] == false){
                cola.add(izq);
                visitados[izq.x][izq.y] = true;
            }
            if (der.x >= 0 && der.y >= 0 && der.x < ancho && der.y < largo && imagen.getRGB(der.x, der.y) == colorF && visitados[der.x][der.y] == false){
                cola.add(der);
                visitados[der.x][der.y] = true;
            }
            if (arriba.x >= 0 && arriba.y >= 0 && arriba.x < ancho && arriba.y < largo && imagen.getRGB(arriba.x, arriba.y) == colorF && visitados[arriba.x][arriba.y] == false){
                cola.add(arriba);
                visitados[arriba.x][arriba.y] = true;
            }
            if (abajo.x >= 0 && abajo.y >= 0 && abajo.x < ancho && abajo.y < largo && imagen.getRGB(abajo.x, abajo.y) == colorF && visitados[abajo.x][abajo.y] == false){
                cola.add(abajo);
                visitados[abajo.x][abajo.y] = true;
            } 
        } 
    }

    /**
     * Escanea la imagen píxel por píxel para identificar y extraer todas las figuras geométricas.
     * 
     * Recorre la matríz de la imagen para encontrar píxeles no visitados,
     * cada vez que encuentra un píxel de la figura, ejecuta una exploración
     * en anchura (BFS) para aislar la figura completa y añadirla a la lista de resultados.
     * @return una lista con las figuras encontradas en la imagen.  
     */
    private List<DatosFigura> extraerTodasLasFiguras() {
        ArrayList<DatosFigura> listafiguras = new ArrayList<>();
        for (int y = 0; y < imagen.getHeight(); y++) {
            for (int x = 0; x < imagen.getWidth(); x++) {
                int color = imagen.getRGB(x, y);
                if ((!visitados[x][y])) {
                    DatosFigura figuraNueva = explorarFiguraBFS(x, y);
                    if(figuraNueva.getArea().size()>=AREA_MINIMA){ //nuevo uwu
                        listafiguras.add(figuraNueva);
                    }
                }
            }
        }

        return listafiguras;

    }

    /**
     * Reconstruya una figura geométrica a partir de una coordenada inicial mediante Búsqueda en Anchura (BFS).
     * 
     * @param startX, Coordenada x inicial donde se detectó el primer píxel
     * @param startY, Coordenada y inicial donde se detectó el segundo píxel
     * @return, Un objeto {@link DatosFigura} que encapsula el color HEX de la figura, 
     * la lista completa de puntos de su área y la lista de puntos de su contorno.
     */
    private DatosFigura explorarFiguraBFS(int startX, int startY) {
        ArrayList<Point> area = new ArrayList<>();
        ArrayList<Point> contorno = new ArrayList<>();
        Queue<Point> colita = new LinkedList<>();

        Point puntoInicial = new Point(startX, startY);
        colita.add(puntoInicial);
        visitados[startX][startY] = true;
        int colorFigura = imagen.getRGB(startX, startY);

        // arreglos para ver los vecinos del punto actual
        int coordenadasX[] = { 0, 0, -1, 1 };
        int coordenadasy[] = { -1, 1, 0, 0 };
        while (!colita.isEmpty()) {
            Point actual = colita.poll();
            int colorActual=imagen.getRGB(actual.x, actual.y); //nuevo uwu
            area.add(actual);
            boolean esContorno = false;
            // Ciclo for para revisar cada vecino del punto actual (arriba , abajo
            // ,izquierda, derecha)
            for (int vecino = 0; vecino < 4; vecino++) {
                // Valor de la coordenada x del vecino que se esta revisando
                int corX = actual.x + coordenadasX[vecino];
                // Valor de la coordenada x del vecino que se esta revisando
                int corY = actual.y + coordenadasy[vecino];
                // Booleano para ver que no salga de la imagen el nuevo punto (x,y) por revisar
                boolean estaEnLaImagen = (corX >= 0 && corX < imagen.getWidth())
                        && (corY >= 0 && corY < imagen.getHeight());
                // Si esta el punto (x,y) a revisar se revisan los casos
                if (estaEnLaImagen) {
                    int colorvecino = imagen.getRGB(corX, corY);
                    // Si el color del punto(vecino) es del color del fondo el punto actual era un
                    // contorno de la figura
                    if (colorvecino == colorFondo) {
                        esContorno = true;
                    }
                    else if(!mismaFigura(colorActual, colorvecino)) {   // nuevo uwu: otra figura pegada
                        esContorno = true;
                    }
                    // Si son del mismo color el punto actual y que se esta revisando , se agrega a
                    // ala cola el punto(vecino)
                    else if (visitados[corX][corY] == false) {
                        visitados[corX][corY] = true;
                        colita.add(new Point(corX, corY));
                    }

                }
                // Si el punto(vecino) no estaba dentor de la imagen , si era color del fondo
                else {
                    esContorno = true;
                }
                }
                // Si al menos uno de los 4 vecinos fue fondo o fuera de la imagen es borde
                // Daniel fix: Estaba bien solo que seguia dentro del for, solo lo puse fuera, asi cuanfo 
                // al menos 1 sea fondo directamente se meta encontorno.
                if (esContorno) {
                    contorno.add(actual);
                }
        }
        String colorEncontrado = String.format("#%06X", (colorFigura & 0xFFFFFF));
        return new DatosFigura(colorEncontrado, area, contorno);

    }
    /**
     * Nuevo uwu:
     * Decide si dos pixeles vecinos son parte de la misma figura comparando sus colores
     * con una tolerancia (para no romper figuras con bordes y/o tonos casi iguales).
     */
    private boolean mismaFigura(int rgbA, int rgbB) {
        int dr=Math.abs(((rgbA >> 16) & 0xFF)-((rgbB >> 16) & 0xFF));
        int dg=Math.abs(((rgbA >> 8) & 0xFF)-((rgbB >> 8) & 0xFF));
        int db=Math.abs((rgbA & 0xFF)-(rgbB & 0xFF));
        return(dr+dg+db)<=TOLERANCIA_COLOR;
    }

}