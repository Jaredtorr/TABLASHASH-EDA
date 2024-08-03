import models.TablaHash;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        TablaHash tablaHash1 = new TablaHash(8000);
        TablaHash tablaHash2 = new TablaHash(8000);

        cargarDatosEnTablas(tablaHash1, tablaHash2);

        mostrarResultadosDeInsercion(tablaHash1, tablaHash2);

        gestionarMenu(tablaHash1, tablaHash2);
    }

    private static void cargarDatosEnTablas(TablaHash tablaHash1, TablaHash tablaHash2) {
        String delimitador = ",";
        String archivo = "bussines.csv";

        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                String[] negocio = linea.split(delimitador);
                if (negocio.length >= 5) {
                    String clave = negocio[1];
                    String valor = "ID=" + negocio[0] + ", Dirección=" + negocio[2] +
                            ", Ciudad=" + negocio[3] + ", Estado=" + negocio[4];

                    tablaHash1.medirTiempoInsercion(clave, valor, 1);
                    tablaHash2.medirTiempoInsercion(clave, valor, 2);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    private static void mostrarResultadosDeInsercion(TablaHash tablaHash1, TablaHash tablaHash2) {
        int tiempoTotalInsercion1 = tablaHash1.obtenerTiempoTotalInsercion1();
        int tiempoTotalInsercion2 = tablaHash2.obtenerTiempoTotalInsercion2();

        System.out.println("\nTiempo total de inserción usando HashFunction1: " + tiempoTotalInsercion1 + " ns");
        System.out.println("Tiempo total de inserción usando HashFunction2: " + tiempoTotalInsercion2 + " ns");

        if (tiempoTotalInsercion1 < tiempoTotalInsercion2) {
            System.out.println("La funcion hash 1 fue mas rapida en la insercion.");
        } else {
            System.out.println("La funcion hash 2 fue mas rapida en la insercion.");
        }
    }


    private static void gestionarMenu(TablaHash tablaHash1, TablaHash tablaHash2) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            mostrarMenu();
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    buscarPorNombre(tablaHash1, tablaHash2, scanner);
                    break;
                case 2:
                    mostrarDatosPorIndice(tablaHash1, tablaHash2, scanner);
                    break;
                case 3:
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida. Inténtalo de nuevo.");
                    break;
            }
        }

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\nMenú:");
        System.out.println("1. Buscar por nombre:");
        System.out.println("2. Mostrar por indice");
        System.out.println("3. Salir");
        System.out.print("Selecciona una opción: ");
    }

    private static void buscarPorNombre(TablaHash tablaHash1, TablaHash tablaHash2, Scanner scanner) {
        System.out.print("Ingresa el nombre para buscar: ");
        String claveBusqueda = scanner.nextLine().trim();

        try {
            long inicioTiempo1 = System.nanoTime();
            String valorEncontrado1 = tablaHash1.buscarValorPorClave(claveBusqueda, 1);
            long finTiempo1 = System.nanoTime();
            long tiempoBusqueda1 = finTiempo1 - inicioTiempo1;

            long inicioTiempo2 = System.nanoTime();
            String valorEncontrado2 = tablaHash2.buscarValorPorClave(claveBusqueda, 2);
            long finTiempo2 = System.nanoTime();
            long tiempoBusqueda2 = finTiempo2 - inicioTiempo2;

            System.out.println("Tiempo de busqueda usando la funcion hash 1: " + tiempoBusqueda1 + " ns");
            System.out.println("Tiempo de busqueda usando la funcion hash 2: " + tiempoBusqueda2 + " ns");

            if (valorEncontrado1 != null || valorEncontrado2 != null) {
                if (valorEncontrado1 != null) {
                    System.out.println("Clave '" + claveBusqueda + "' encontrada en Tabla Hash 1. " + valorEncontrado1);
                }
                if (valorEncontrado2 != null) {
                    System.out.println("Clave '" + claveBusqueda + "' encontrada en Tabla Hash 2. " + valorEncontrado2);
                }
            } else {
                System.out.println("Clave '" + claveBusqueda + "' no encontrada.");
            }

            if (tiempoBusqueda1 < tiempoBusqueda2) {
                System.out.println("La funcion hash fue mas rapida en la búsqueda.");
            } else {
                System.out.println("La funcion hash 2 fue mas rapida en la búsqueda.");
            }
        } catch (Exception e) {
            System.err.println("Error al buscar el nombre: " + e.getMessage());
        }
    }

    private static void mostrarDatosPorIndice(TablaHash tablaHash1, TablaHash tablaHash2, Scanner scanner) {
        System.out.print("Introduce el índice para mostrar: ");
        int indiceBusqueda = scanner.nextInt();
        scanner.nextLine();

        try {
            if (indiceBusqueda < 0 || indiceBusqueda >= tablaHash1.getCapacidad()) {
                System.out.println("Índice fuera de rango");
            } else {
                System.out.println("Datos en el índice " + indiceBusqueda + " de la Tabla Hash 1:");
                List<String> datos1 = tablaHash1.obtenerDatosEnIndice(indiceBusqueda);
                for (String dato : datos1) {
                    System.out.println(dato);
                }

                System.out.println("Datos en el índice " + indiceBusqueda + " de la Tabla Hash 2:");
                List<String> datos2 = tablaHash2.obtenerDatosEnIndice(indiceBusqueda);
                for (String dato : datos2) {
                    System.out.println(dato);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener los datos del índice: " + e.getMessage());
        }
    }
}
