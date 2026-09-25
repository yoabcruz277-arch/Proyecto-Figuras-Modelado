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
     * El principal que va a hacer toda la chamba.
     */
    public List<DatosFigura> procesador(String imagenBmp) {

    }

    /**
     * Ve haceindo los que quieras, yo hare el de detectar color y separar fondo.
     */

    /**
     * Yo estoy haciendo este
     */
    private void cargarImagen(String rutaArchivo) throws Exception {
        File archivo = new File(rutaArchivo);
        imagen = ImageIO.read(archivo);

        int anchoImagen = imagen.getWidth();
        int alturaImagem = imagen.getHeight();

        visitados = new boolean[anchoImagen][alturaImagem];
        colorFondo = this.imagen.getRGB(0, 0);
    }

    private int detectaColorFondo() {

    }

    private void separarFondoBFS() {

    }

    /**
     * Yo estoy haciendo este
     */
    private List<DatosFigura> extraerTodasLasFiguras() {
        ArrayList<DatosFigura> listafiguras = new ArrayList<>();
        for (int y = 0; y < imagen.getHeight(); y++) {
            for (int x = 0; x < imagen.getWidth(); x++) {
                int color = imagen.getRGB(x, y);
                if ((!visitados[x][y]) && (color != colorFondo)) {
                    DatosFigura figuraNueva = explorarFiguraBFS(x, y);
                    listafiguras.add(figuraNueva);
                }
            }
        }

        return listafiguras;

    }

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
                // Si al menos uno de los 4 vecinos fue fondo o fuera de la imagen es borde
                if (esContorno) {
                    contorno.add(actual);
                }

            }
        }
        String colorEncontrado = String.format("#%06X", (colorFigura & 0xFFFFFF));
        return new DatosFigura(colorEncontrado, area, contorno);

    }

}