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


    /**
     * Yo estoy haciendo este
     */
    private void cargarImagen(String rutaArchivo) throws Exception{
        File archivo= new File (rutaArchivo);
        imagen = ImageIO.read(archivo);
       int anchoImagen= imagen.getWidth();
        int alturaImagem=imagen.getHeight();
        visitados= new boolean[anchoImagen][alturaImagem];
        colorFondo=this.imagen.getRGB(0, 0);

    }

    private void detectaColorBFS(){

    }

    private void separarFondoBFS(){

    }
    /**
     * Yo estoy haciendo este
     */
    private List<DatosFigura> extraerTodasLasFiguras(){

    }

    private DatosFigura explorarFiguraBFS(int startX, int startY){

    }


}