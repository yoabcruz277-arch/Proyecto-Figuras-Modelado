import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.awt.Point;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase principal, lee la imgaen, separa el fondo y la misma figura, saca las coordenadas 
 * para poder pasar las listas de coordenadas.
 */
public class ProcesadorImagen {
    private BufferedImage imagen;
    private boolean[][] visitados;
    private int colorFondo;

    /**
     * Constructor vacio xd.
     */
    public ProcesadorImagen(){
    }

    /**
     * El principal que va a hacer toda la chamba.
     */
    public List<DatosFigura> procesador(String imagenBmp){

    }

    /**
     * Yo estoy haciendo este
     */
    private void cargarImagen(String rutaArchivo) throws Exception{
        File archivo = new File (rutaArchivo);
        imagen = ImageIO.read(archivo);

        int anchoImagen = imagen.getWidth();
        int alturaImagen = imagen.getHeight();

        visitados = new boolean[anchoImagen][alturaImagen];
        colorFondo = detectaColorFondo();   // cambie al color mas frecuente en los bordes para saber cual es el fondo.
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
    private void separarFondoBFS(){
        Queue<Point> cola = new LinkedList<>();
        int colorF = detectaColorFondo();
        int ancho = imagen.getWidth();
        int largo = imagen.getHeight();

        visitados = new boolean[largo][ancho];

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
        // Empieza el cilco while, pero ya a mimir xd 
    }

    /**
     * Yo estoy haciendo este
     */
    private List<DatosFigura> extraerTodasLasFiguras(){
        ArrayList<DatosFigura> listafiguras = new ArrayList<>();
        for (int y=0 ; y<imagen.getHeight(); y++){
            for (int x=0; x<imagen.getWidth(); x++){
                int color = imagen.getRGB(x, y);
                if((!visitados[x][y])&&(color!=colorFondo)){
                    DatosFigura figuraNueva = explorarFiguraBFS(x, y);
                    listafiguras.add(figuraNueva);
                }
            }
        }
        
        return listafiguras;

    }

    private DatosFigura explorarFiguraBFS(int startX, int startY){

    }


}