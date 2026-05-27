package ups.poo.buscaminas.vista;

import ups.poo.buscaminas.modelos.PadreBuscaminas;

import java.util.Scanner;

public class MenuVista {

    private Scanner leer;

    public MenuVista() {
        this.leer = new Scanner(System.in);
    }

    public void imprimirTablero() {
        System.out.print("   ");
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
                        contenido = "0";
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

    public int menuPrincipal() {
        System.out.println("=========================================");
        System.out.println("        ¡BIENVENIDO AL BUSCAMINAS!       ");
        System.out.println("=========================================");
        System.out.println(" 1. Iniciar una Partida Nueva (10x10)");
        System.out.println(" 2. Cargar Partida Guardada Anterior");
        System.out.println(" 3. Salir:");
        int opcion = leer.nextInt();
        leer.nextLine(); // Limpiar el buffer del scanner
        return opcion;
    }

    public int accionJuego() {
        System.out.println("----------- SIGUIENTE MOVIMIENTO -----------");
        System.out.println(" 1. Revelar una casilla (Fila y Columna)");
        System.out.println(" 2. Poner una Bandera (F)");
        System.out.println(" 3. Guardar partida y salir del juego");
        System.out.println("---------------------------------------------");
        int opcion = leer.nextInt();
        leer.nextLine(); // Limpiar el buffer del scanner
        return opcion;
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println("> " + mensaje + "<");
    }

    public String pedirCoordenada(){
        System.out.println("Ingrese la coordenada de la casilla (Ej:A5 o J10):");
        String coordenada = leer.next().toUpperCase();
        leer.nextLine();
        return coordenada;
    }

    public void limpiarBuffer(){
        leer.nextLine();
    }
}
