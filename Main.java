
import Mod1.*;
import Mod2.*;
import java.io.File;
import java.util.List;
import java.util.Scanner;


public class Main{
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int opcion=0;
        System.out.println("============================================");
        System.out.println("   Proyecto 1 - Reconocimiento de Figuras");
        System.out.println("============================================");
        System.out.println("");
        do{
            System.out.println("\n"+"Escriba el número de la opción con la que quiere continuar" +"\n");
            System.out.println("1. Analizar una imagen .bmp");
            System.out.println("2. Salir");
            System.out.println("");
            try {
                opcion=sc.nextInt();
                sc.nextLine();
            } catch(java.util.InputMismatchException e){
                System.out.println("\n"+"Ingresa un número válido, no letras :v");
                sc.nextLine();
                continue;
            }
            switch(opcion){
                case 1:
                    System.out.print("\n"+"Pon la ruta de la imagen .bmp a analizar: "+"\n");
                    String rutota=sc.nextLine();
                    File archivo=new File(rutota);
                    if(!archivo.exists() || !archivo.isFile()) {
                        System.out.println("\n"+"Esta mal en algo, intenta denuevo :,v" +"\n");
                        break;
                    }
                    try{
                        ProcesadorImagen procesador=new ProcesadorImagen();
                        AnalizadorFiguras analizador=new AnalizadorFiguras();
                        ClasificarFiguras clasificador=new ClasificarFiguras();

                        List<DatosFigura> figurasEncontradas = procesador.procesador(rutota);
                        if (figurasEncontradas == null || figurasEncontradas.isEmpty()) {
                            System.out.println("\n"+"Que no hay nada xd");
                            break;
                        }
                        System.out.println("Lo que tu me esta diciendo que se encontro: " + figurasEncontradas.size() + " figura(s):"+"\n");
                        for (int i = 0; i < figurasEncontradas.size(); i++){
                            DatosFigura datos=figurasEncontradas.get(i);
                            MetricasFigura metricas=analizador.analizar(datos);
                            char categoria=clasificador.clasificador(metricas);

                            System.out.println("");
                            System.out.println("===============================================");
                            System.out.println("Figura " + (i+1) + ":");
                            System.out.println(" -> Color: " +datos.getColor());
                            System.out.println(" -> Categoría         : "+traductorGoogle(categoria) + " (" + categoria + ")");
                            System.out.println(" -> Vértices (RDP)    : "+metricas.getNumVertices());
                            System.out.println(" -> Área              : "+metricas.getArea() + " px²");
                            System.out.println(" -> Perímetro         : "+String.format("%.2f", metricas.getPerimetro()) + " px");
                            System.out.println(" -> Fact. Circularidad: "+String.format("%.4f", metricas.getFactCircularidad()));
                            System.out.println("===============================================");
                        }
                    } catch(Exception e){
                        System.out.println("\n"+" ESTA MAL EN ALGO " + e.getMessage());
                    }
                    break;
                    case 2:
                    System.out.println("\n"+"Chao");
                    break;

                default:
                    System.out.println("\n"+"Esa opción no jala, intenta de nuevo :V");
                }
    
        }while(opcion!=2);
        sc.close();
    }

    /**
     * Función auxiliar para que la salida en terminal sea legible.
     */
    private static String traductorGoogle(char c) {
        switch (c) {
            case 'C': return "Cuadrilátero";
            case 'T': return "Triángulo";
            case 'O': return "Círculo";
            case 'X': return "Otros";
            default: return "Yo que se brother";
        }
    }
}