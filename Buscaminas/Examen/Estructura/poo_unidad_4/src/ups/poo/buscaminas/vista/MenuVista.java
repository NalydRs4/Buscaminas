package ups.poo.buscaminas.vista;

import ups.poo.buscaminas.modelos.PadreBuscaminas;
import ups.poo.buscaminas.excepciones.OpcionInvalida;

import java.util.Scanner;

public class MenuVista {

    private Scanner leer;

    // Creamos el constructor de la clase para ingresar datos por teclado
    public MenuVista() {
        this.leer = new Scanner(System.in);
    }

    // Método para imprimir el tablero
    public void imprimirTablero() {
        System.out.print("  ");
        for (int j = 1; j <= PadreBuscaminas.getColumnas(); j++) {
            System.out.printf(" %2d ", j);
        }
        System.out.println();

        System.out.print("  +");
        for (int j = 0; j < PadreBuscaminas.getColumnas(); j++) {
            System.out.print("---+");
        }
        System.out.println();

        for (int i = 0; i < PadreBuscaminas.getFilas(); i++) {
            char letraFila = (char) ('A' + i);
            System.out.print(letraFila + " |");

            for (int j = 0; j < PadreBuscaminas.getColumnas(); j++) {
                String contenido = " ";
                if (PadreBuscaminas.getBanderas()[i][j]) {
                    contenido = "F";
                } else if (PadreBuscaminas.getEncontradas()[i][j]) {
                    if (PadreBuscaminas.getTablero()[i][j] == '-') {
                        contenido = "V";
                    } else {
                        contenido = String.valueOf(PadreBuscaminas.getTablero()[i][j]);
                    }
                } else {
                    contenido = "-";
                }
                System.out.printf(" %s |", contenido);
            }
            System.out.println();

            System.out.print("  +");
            for (int j = 0; j < PadreBuscaminas.getColumnas(); j++) {
                System.out.print("---+");
            }
            System.out.println();
        }
    }

    // Método para dar la bienvenida al usuario
    public int menuPrincipal() {
        System.out.println("=========================================");
        System.out.println("        ¡BIENVENIDO AL BUSCAMINAS!       ");
        System.out.println("          Seleccione una opción:         ");
        System.out.println("           Totoy D. - Chamba I.         ");
        System.out.println("=========================================");
        System.out.println(" 1. Para Iniciar Una Partida Nueva (10x10)");
        System.out.println(" 2. Para Cargar Una Partida Guardada");
        System.out.println(" 3. Salir del programa");
        int opcion = 0;
        try {
            opcion = leer.nextInt();
            leer.nextLine();
        } catch (Exception excepcion) {
            System.out.println("-----------------------------------------");
            System.out.println("[ERROR]: INGRESAR SOLO NÚMEROS.");
            System.out.println("-----------------------------------------");
            leer.nextLine();
            opcion = -1;
        }
        return opcion;
    }

    // Método para pedir el siguiente movimiento
    public int accionJuego() {
        System.out.println("--------- SIGUIENTE MOVIMIENTO ---------");
        System.out.println("         Seleccione una opción:         ");
        System.out.println("----------------------------------------");
        System.out.println(" 1. Revelar una casilla (Fila y Columna)");
        System.out.println(" 2. Poner una Bandera (F)");
        System.out.println(" 3. Guardar partida y salir del juego");
        System.out.println("----------------------------------------");
        int opcion = 0;
        try {
            opcion = leer.nextInt();
            leer.nextLine();
        } catch (Exception excepcion) {
            System.out.println("-----------------------------------------");
            System.out.println("[ERROR]: INGRESAR SOLO NÚMEROS.");
            System.out.println("-----------------------------------------");
            leer.nextLine();
            opcion = -1;
        }
        return opcion;
    }

    // Método para mostrar un mensaje personalizado
    public void mostrarMensaje(String mensaje) {
        System.out.println("> " + mensaje + " <");
    }

    // Método para ingresar una coordenada al juego
    public String pedirCoordenada() {
        System.out.println("----------------------------------------");
        System.out.println("  Ingrese la coordenada en MAYUSCULAS:");
        System.out.println("----------------------------------------");
        System.out.println("   [Fila y Columna = Ej: A5 o J10]:");
        System.out.println("----------------------------------------");
        String coordenada = leer.next().toUpperCase();
        leer.nextLine();
        return coordenada;
    }

    // Método para limpiar el buffer para ingreso de datos
    public void limpiarBuffer() {
        leer.nextLine();
        return;
    }
}
