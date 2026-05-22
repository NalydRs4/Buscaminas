import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

class CasillaYaDescubiertaException extends Exception {
    public CasillaYaDescubiertaException(String mensaje) {
        super(mensaje);
    }
}

public class Buscaminas {

    static int Filas = 11;
    static int Columnas = 11;
    static int numeroMinas = 10;
    static char[][] Tablero = new char[Filas][Columnas];
    static int Restantes;
    static int[] Movimiento = new int[2];
    static boolean[][] Encontradas;
    static boolean Victoria, Derrota;
    static Scanner Leer = new Scanner(System.in);
    
    static boolean opcionArchivo = false;

    private static void Iniciar() {
        for (int i = 0; i < Filas; i++) {
            for (int j = 0; j < Columnas; j++) {
                Tablero[i][j] = '-';
            }
        }
        Encontradas = new boolean[Filas][Columnas];
        Restantes = Filas * Columnas - numeroMinas;
    }

    private static void Minas() {
        int minasAleatorias = 0;
        while (minasAleatorias < numeroMinas) {
            int fila = (int) (Math.random() * Filas);
            int columna = (int) (Math.random() * Columnas);
            if (Tablero[fila][columna] != 'X') {
                Tablero[fila][columna] = 'X';
                minasAleatorias++;
            }
        }
    }

    private static void minasAlrededor() {
        for (int i = 0; i < Filas; i++) {
            for (int j = 0; j < Columnas; j++) {
                if (Tablero[i][j] != 'X') {
                    int conteo = 0;
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            if ((x != 0 || y != 0) && celdaValida(i + x, j + y) && Tablero[i + x][j + y] == 'X') {
                                conteo++;
                            }
                        }
                    }
                    if (conteo > 0) {
                        Tablero[i][j] = (char) (conteo + '0');
                    }
                }
            }
        }
    }

    private static boolean celdaValida(int fila, int columna) {
        return fila >= 0 && fila < Filas && columna >= 0 && columna < Columnas;
    }

    private static void imprimirCuadricula() {
        System.out.print("   ");
        for (int j = 1; j < Columnas; j++) {
            System.out.printf(" %2d ", j);
        }
        System.out.println();

        System.out.print("  +");
        for (int j = 1; j < Columnas; j++) {
            System.out.print("---+");
        }
        System.out.println();

        for (int i = 1; i < Filas; i++) {
            char letraFila = (char) ('A' + i - 1);
            System.out.print(letraFila + " |");

            for (int j = 1; j < Columnas; j++) {
                String contenido = " ";
                if (Tablero[i][j] == 'F') {
                    contenido = "F";
                } else if (Encontradas[i][j]) {
                    if (Tablero[i][j] == '-') {
                        contenido = "0";
                    } else {
                        contenido = String.valueOf(Tablero[i][j]);
                    }
                } else {
                    contenido = "-";
                }
                System.out.printf(" %s |", contenido);
            }
            System.out.println();

            System.out.print("  +");
            for (int j = 1; j < Columnas; j++) {
                System.out.print("---+");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void revelarCeldas(int fila, int columna) {
        if (!celdaValida(fila, columna) || Encontradas[fila][columna]) {
            return;
        }
        Encontradas[fila][columna] = true;
        Restantes--;
        if (Tablero[fila][columna] == '-') {
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    revelarCeldas(fila + x, columna + y);
                }
            }
        }
    }

    private static void revelarCeldas() {
        for (int i = 0; i < Filas; i++) {
            for (int j = 0; j < Columnas; j++) {
                Encontradas[i][j] = true;
            }
        }
    }

    private static boolean comprobarVictoria() {
        for (int i = 0; i < Filas; i++) {
            for (int j = 0; j < Columnas; j++) {
                if (Tablero[i][j] != 'X' && !Encontradas[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void Movimiento() {
        opcionArchivo = false;
        System.out.print("Ingrese la fila (A-J) o escriba 'Guardar'/'Cargar': ");
        String entrada = Leer.next().toUpperCase();
        
        if (entrada.equals("Guardar")) {
            guardarJuego("partida.txt");
            opcionArchivo = true;
            return;
        }

        // PERSISTENCIA DE DATOS: Cargar estado
        if (entrada.equals("Cargar")) {
            cargarJuego("partida.txt");
            opcionArchivo = true; 
            return;
        }

        char letraFila = entrada.charAt(0);
        Movimiento[0] = letraFila - 'A' + 1;
        System.out.print("Ingrese la columna: (1-10) ");
        Movimiento[1] = Leer.nextInt();
    }
    
    private static void guardarJuego(String nombreArchivo) {
        try (PrintWriter escritor = new PrintWriter(new FileWriter(nombreArchivo))) {
            escritor.println(Restantes);
            for (int i = 0; i < Filas; i++) {
                for (int j = 0; j < Columnas; j++) {
                    escritor.print(Tablero[i][j]);
                }
                escritor.println();
            }
            for (int i = 0; i < Filas; i++) {
                for (int j = 0; j < Columnas; j++) {
                    escritor.print(Encontradas[i][j] ? "1" : "0");
                }
                escritor.println();
            }
            System.out.println("¡Juego guardado exitosamente en " + nombreArchivo + "!");
        } catch (IOException e) {
            System.out.println("Error al intentar guardar el archivo de la partida.");
        }
    }
    private static void cargarJuego(String nombreArchivo) {
        try (BufferedReader lector = new BufferedReader(new FileReader(nombreArchivo))) {
            Restantes = Integer.parseInt(lector.readLine());
            for (int i = 0; i < Filas; i++) {
                String linea = lector.readLine();
                for (int j = 0; j < Columnas; j++) {
                    Tablero[i][j] = linea.charAt(j);
                }
            }
            for (int i = 0; i < Filas; i++) {
                String linea = lector.readLine();
                for (int j = 0; j < Columnas; j++) {
                    Encontradas[i][j] = linea.charAt(j) == '1';
                }
            }
            System.out.println("¡Partida cargada exitosamente desde " + nombreArchivo + "!");
        } catch (IOException | NullPointerException | NumberFormatException e) {
            System.out.println("No se encontró una partida guardada válida o el archivo está corrupto.");
        }
    }

    public static void main(String[] args) {
        System.out.println("¡Bienvenido al Buscaminas!");
        Iniciar();
        imprimirCuadricula();
        boolean primerTiroValido = false;
        while (!primerTiroValido) {
            try {
                Movimiento();
                if (opcionArchivo) { // CORREGIDO
                    imprimirCuadricula();
                    continue;
                }
                if (!celdaValida(Movimiento[0], Movimiento[1]) || Movimiento[0] == 0 || Movimiento[1] == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                primerTiroValido = true;
            } catch (InputMismatchException e) {
                System.out.println("[ERROR] La columna debe ser un número entero. Intenta de nuevo.");
                Leer.nextLine(); // Limpiar el búfer
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("[ERROR] Posición fuera del rango permitido del tablero. Intenta de nuevo.");
                Leer.nextLine();
            }
        }

        int fila = Movimiento[0];
        int columna = Movimiento[1];
        
        if (!opcionArchivo) { 
            Minas();
            minasAlrededor();
        }

        while (!Victoria && !Derrota) {
            try {
                if (Encontradas[fila][columna]) {
                    throw new CasillaYaDescubiertaException("[Esa casilla ya está descubierta. Elige otra.");
                }

                if (Tablero[fila][columna] == 'X') {
                    Derrota = true;
                    revelarCeldas();
                    imprimirCuadricula();
                    System.out.println("¡PERDISTE! Ha pisado una mina.");
                    break;
                } else {
                    revelarCeldas(fila, columna);
                    Restantes--;
                    imprimirCuadricula();
                    if (comprobarVictoria()) {
                        Victoria = true;
                        System.out.println("¡Felicidades! Has limpiado todas las minas.");
                        break;
                    }

                    
                    boolean siguienteMovimientoValido = false;
                    while (!siguienteMovimientoValido) {
                        try {
                            Movimiento();
                            if (opcionArchivo) { 
                                imprimirCuadricula();
                                fila = Movimiento[0];
                                columna = Movimiento[1];
                                continue;
                            }
                            if (!celdaValida(Movimiento[0], Movimiento[1]) || Movimiento[0] == 0 || Movimiento[1] == 0) {
                                throw new ArrayIndexOutOfBoundsException();
                            }
                            siguienteMovimientoValido = true;
                        } catch (InputMismatchException e) {
                            System.out.println("[Error] La columna debe ser un número entero.");
                            Leer.nextLine();
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("[Error] Posición inválida en el tablero.");
                            Leer.nextLine();
                        }
                    }

                    fila = Movimiento[0];
                    columna = Movimiento[1];
                }
            } catch (CasillaYaDescubiertaException e) {
                System.out.println(e.getMessage());
                // Forzamos pedir el movimiento otra vez sin penalizar ni avanzar el ciclo
                Movimiento();
                fila = Movimiento[0];
                columna = Movimiento[1];
            }
        }
    }
}
