import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.awt.Point;
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
     * Ve haceindo los que quieras, yo hare el de detectar color y separar fondo.
     */
    private void cargarImagen(){

    }

    private void detectaColorBFS(){

    }

    private void separarFondoBFS(){

    }

    private List<DatosFigura> extraerTodasLasFiguras(){

    }

    private DatosFigura explorarFiguraBFS(int startX, int startY){

    }


}