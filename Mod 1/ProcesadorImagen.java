import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.awt.Point;
import java.io.File;
import java.util.Queue;
import java.util.LinkedList;

/**
 * Clase principal, lee la imgaen, separa el fondo y la misma figura, saca las
 * coordenadas
 * para poder pasar las listas de coordenadas.
 */
public class ProcesadorImagen {
    private BufferedImage imagen;
    private boolean[][] visitados;
    private int colorFondo;

    /**
     * Constructor vacio xd.
     */
    public ProcesadorImagen() {
    }

    /**
     * Como se penso:
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

            for (x = 0; x < imagen.getWidth(); x++){
                for(x = 0; x < imagen.getHeight(); y++){
                    if(!visitados[x][y]){
                        resultado.add(extraerTodasLasFiguras(x, y));

                    }
                }
            }
        }  catch(Exception e){
            System.out.println("Esta mal, en algo :v");
        }

        return resultado;
    }

    /**
     * Yo estoy haciendo este
     */
    private void cargarImagen(String rutaArchivo) throws Exception {
        File archivo = new File(rutaArchivo);
        imagen = ImageIO.read(archivo);

        int anchoImagen = imagen.getWidth();
        int alturaImagen = imagen.getHeight();

        visitados = new boolean[anchoImagen][alturaImagem];
        colorFondo = detectaColorFondo();
    }

    /**
     * Como se penso:
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
     * Como se penso: 
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

        visitados = new boolean[largo][ancho];

        // Fors para recorrer los pixeles de la orilla de la imagen,
        // esto soluciona el problema de que haya una figura que parta en 2 o mas a la imagen.
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
                cola.add(new Point(y,0));
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
        // asi se sabe cual es fono y cual no.
        // El primer elemento de la cola ya sabemos que es fondo y ya esta marcado como true.
        // (casi me suicido pq no sabia como hacerlo true dentro del while xd).
        while (cola.isEmpty() != true){
            Point mainPixel = cola.poll();
            // Crea los cuatro puntos al rededor del punto que estaba en la cola.
            Point izq = new Point(mainPixel.x - 1, mainPixel.y);
            Point der = new Point(mainPixel.x + 1, mainPixel.y);
            Point arriba = new Point(mainPixel.x, mainPixel.y + 1);
            Point abajo = new Point(mainPixel.x, mainPixel.y - 1);

            // Revisa a todos los vecinos para ver cual es el que si es fondo o no.
            if (izq.x >= 0 && izq.y >= 0 && izq.x < ancho && izq.y < largo imagen.getRGB(izq.x, izq.y) == colorF && visitados[izq.x][izq.y] == false){
                cola.add(izq);
                visitados[izq.x][izq.y] = true;
            }
            if (der.x >= 0 && der.y >= 0 && der.x < ancho && der.y < largo imagen.getRGB(der.x, der.y) == colorF && visitados[der.x][der.y] == false){
                cola.add(der);
                visitados[der.x][der.y] = true;
            }
            if (arriba.x >= 0 && arriba.y >= 0 && arriba.x < ancho && arriba.y < largo imagen.getRGB(arriba.x, arriba.y) == colorF && visitados[arriba.x][arriba.y] == false){
                cola.add(arriba);
                visitados[arriba.x][arriba.y] = true;
            }
            if (abajo.x >= 0 && abajo.y >= 0 && abajo.x < ancho && abajo.y < largo imagen.getRGB(abajo.x, abajo.y) == colorF && visitados[abajo.x][abajo.y] == false){
                cola.add(abajo);
                visitados[abajo.x][abajo.y] = true;
            } 
        } 
    }

    /**
     * Yo estoy haciendo este
     */
    private List<DatosFigura> extraerTodasLasFiguras() {
        ArrayList<DatosFigura> listafiguras = new ArrayList<>();
        for (int y = 0; y < imagen.getHeight(); y++) {
            for (int x = 0; x < imagen.getWidth(); x++) {
                int color = imagen.getRGB(x, y);
                if ((!visitados[x][y])) {
                    DatosFigura figuraNueva = explorarFiguraBFS(x, y);
                    listafiguras.add(figuraNueva);
                }
            }
        }

        return listafiguras;

    }

    /**
     *  DOCUMENTALO NADA MAS.
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
                    // Si son del mismo color el punto actual y que se esta revisando , se agrega a
                    // ala cola el punto(vecino)
                    else if (colorvecino == colorFigura && visitados[corX][corY] == false) {
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

}