import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.awt.Point;
import java.io.File;
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
        ArrayList<DatosFigura>  listafiguras= new ArrayList<>();
        for (int y=0 ; y<imagen.getHeight();y++){
            for (int x=0; x<imagen.getWidth();x++){
               int color= imagen.getRGB(x, y);
                if((!visitados[x][y])&&(color!=colorFondo)){
                    DatosFigura figuraNueva= explorarFiguraBFS(x, y);
                    listafiguras.add(figuraNueva);
                }
            }
        }
        
        return listafiguras;

    }

    private DatosFigura explorarFiguraBFS(int startX, int startY){

    }


}