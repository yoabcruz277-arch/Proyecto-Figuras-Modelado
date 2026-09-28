
import Mod1.*;
import Mod2.*;
import java.io.File;
import java.util.List;
import java.util.Scanner;


public class Main{
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int opcion=0;
        System.out.println("==========================================================");
        System.out.println("   Proyecto 1 - Reconocimiento de Figuras Geométricas");
        System.out.println("==========================================================");
        System.out.println("");
        do{
            System.out.println("\n"+"Ingresa el número de opción con la que quieras continuar" +"\n");
            System.out.println("1. Analizar una imagen .bmp");
            System.out.println("2. Salir");
            System.out.println("");
            try {
                opcion=sc.nextInt();
                sc.nextLine();
            } catch(java.util.InputMismatchException e){
                System.out.println("\n"+"Ingresa una opción válida");
                sc.nextLine();
                continue;
            }
            switch(opcion){
                case 1:
                    System.out.print("\n"+"Ingrese la ruta de la imagen .bmp a analizar: "+"\n");
                    String rutota=sc.nextLine();
                    File archivo=new File(rutota);
                    if(!archivo.exists() || !archivo.isFile()) {
                        System.out.println("\n"+"Por favor prueba de nuevo con otro archivo" +"\n");
                        break;
                    }
                    try{
                        ProcesadorImagen procesador=new ProcesadorImagen();
                        AnalizadorFiguras analizador=new AnalizadorFiguras();
                        ClasificarFiguras clasificador=new ClasificarFiguras();

                        List<DatosFigura> figurasEncontradas = procesador.procesador(rutota);
                        if (figurasEncontradas == null || figurasEncontradas.isEmpty()) {
                            System.out.println("\n"+"Que no hay nada");
                            break;
                        }
                        System.out.println("Se encontraron: " + figurasEncontradas.size() + " figura(s):"+"\n");
                        for (int i = 0; i < figurasEncontradas.size(); i++){
                            DatosFigura datos=figurasEncontradas.get(i);
                            MetricasFigura metricas=analizador.analizar(datos);
                            char categoria=clasificador.clasificador(metricas);

                            System.out.println("");
                            System.out.println("===============================================");
                            System.out.println("Figura " + (i+1) + ":");
                            System.out.println(" -> Color: " +datos.getColor());
                            System.out.println(" -> Vértices (RDP)    : "+metricas.getNumVertices());
                            System.out.println(" -> Área              : "+metricas.getArea() + " px²");
                            System.out.println(" -> Perímetro         : "+String.format("%.2f", metricas.getPerimetro()) + " px");
                            System.out.println(" -> Fact. Circularidad: "+String.format("%.4f", metricas.getFactCircularidad()));
                            System.out.println(" -> Categoría         : "+traductorGoogle(categoria) + " (" + categoria + ")");
                            System.out.println("===============================================");
                        }
                    } catch(Exception e){
                        System.out.println("\n"+" ESTA MAL EN ALGO " + e.getMessage());
                    }
                    break;
                    case 2:
                    System.out.println("\n"+"Vuelve pronto");
                    break;

                default:
                    System.out.println("\n"+"Opción inválida, intenta de nuevo");
                }
    
        }while(opcion!=2);
        sc.close();
    }

    /**
     * Función auxiliar para que la salida en terminal se imprima su categoría.
     */
    private static String traductorGoogle(char c) {
        switch (c) {
            case 'C': return "Cuadrilátero";
            case 'T': return "Triángulo";
            case 'O': return "Círculo";
            case 'X': return "Otros";
            default: return "Figura no reconocida";
        }
    }
}