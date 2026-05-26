import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

class CasillaYaDescubiertaException extends Exception {
    public CasillaYaDescubiertaException(String mensaje) {
        super(mensaje);
    }
}

public class Buscaminas {

    static int Filas = 10;
    static int Columnas = 10;
    static int numeroMinas = 10;
    static char[][] Tablero = new char[Filas][Columnas];
    static int Restantes;
    static int[] Movimiento = new int[2];
    static boolean[][] Encontradas;
    static boolean[][] Banderas;
    static boolean Victoria, Derrota;
    static boolean modoBandera = false;
    static Scanner Leer = new Scanner(System.in);
    
    static boolean partidaCargadaDesdeArchivo = false;
    static boolean usuarioQuisoSalir = false;

    private static void Iniciar() {
        for (int i = 0; i < Filas; i++) {
            for (int j = 0; j < Columnas; j++) {
                Tablero[i][j] = '-';
            }
        }
        Encontradas = new boolean[Filas][Columnas];
        Banderas = new boolean[Filas][Columnas];
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
        for (int j = 1; j <= Columnas; j++) {
            System.out.printf(" %2d ", j);
        }
        System.out.println();

        System.out.print("  +");
        for (int j = 0; j < Columnas; j++) {
            System.out.print("---+");
        }
        System.out.println();

        for (int i = 0; i < Filas; i++) {
            char letraFila = (char) ('A' + i);
            System.out.print(letraFila + " |");

            for (int j = 0; j < Columnas; j++) {
                String contenido = " ";
                if (Banderas[i][j]) {
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
            for (int j = 0; j < Columnas; j++) {
                System.out.print("---+");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void revelarCeldas(int fila, int columna) {
        if (!celdaValida(fila, columna) || Encontradas[fila][columna] || Banderas[fila][columna]) {
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
        return Restantes == 0;
    }
    
    private static void elegirAccionTurno() {
        while (true) {
            try {
                System.out.println("---- ¿QUÉ DESEAS HACER EN ESTE TURNO? ----");
                System.out.println(" 1. Revelar/Pisar una casilla");
                System.out.println(" 2. Poner o Quitar una Bandera (F)");
                System.out.println(" 3. Guardar partida actual y salir del juego");
                System.out.print(" Seleccione una opción (1-3): ");
                int opcion = Leer.nextInt();

                if (opcion == 3) {
                    guardarJuego("partida.txt");
                    usuarioQuisoSalir = true;
                    return;
                }

                if (opcion == 1 || opcion == 2) {
                    modoBandera = (opcion == 2);

                    System.out.print(" Ingrese la coordenada de la casilla (Ej: A5 o J10): ");
                    String coordenada = Leer.next().toUpperCase();

                    char letraFila = coordenada.charAt(0);
                    Movimiento[0] = letraFila - 'A';

                    String numColumna = coordenada.substring(1);
                    Movimiento[1] = Integer.parseInt(numColumna) - 1;

                    if (celdaValida(Movimiento[0], Movimiento[1])) {
                        return; // Todo correcto, regresamos al flujo
                    } else {
                        System.out.println("[Error] Coordenada fuera del tablero. Intente de nuevo.\n");
                    }
                } else {
                    System.out.println("[Error] Opción inválida. Elige 1, 2 o 3.\n");
                }
            } catch (InputMismatchException | NumberFormatException | StringIndexOutOfBoundsException e) {
                System.out.println("[Error] Formato incorrecto de datos. Intente de nuevo.\n");
                Leer.nextLine(); // Limpiar búfer
            }
        }
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
            for (int i = 0; i < Filas; i++) {
                for (int j = 0; j < Columnas; j++) {
                    escritor.print(Banderas[i][j] ? "1" : "0");
                }
                escritor.println();
            }
            System.out.println("\n» ¡Juego guardado exitosamente en " + nombreArchivo + "!");
        } catch (IOException e) {
            System.out.println("[Error] No se pudo guardar el archivo de la partida.");
        }
    }

    private static boolean cargarJuego(String nombreArchivo) {
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
            for (int i = 0; i < Filas; i++) {
                String linea = lector.readLine();
                for (int j = 0; j < Columnas; j++) {
                    Banderas[i][j] = linea.charAt(j) == '1';
                }
            }
            System.out.println("\n» ¡Partida cargada de forma exitosa!");
            return true;
        } catch (IOException | NullPointerException | NumberFormatException e) {
            System.out.println("\n[Error] No se encontró ninguna partida guardada o el archivo está dañado.");
            System.out.println("Iniciando un juego nuevo por defecto...\n");
            return false;
        }
    }

    
    private static void menuInicioPrincipal() {
        System.out.println("=========================================");
        System.out.println("        ¡BIENVENIDO AL BUSCAMINAS!       ");
        System.out.println("=========================================");
        System.out.println(" 1. Iniciar una Partida Nueva (10x10)");
        System.out.println(" 2. Cargar Partida Guardada Anterior");
        System.out.print(" Seleccione una opción (1-2): ");

        int seleccion = 1;
        try {
            seleccion = Leer.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("[Atento] Entrada no válida. Iniciando juego nuevo...");
            Leer.nextLine();
        }

        Iniciar(); 

        if (seleccion == 2) {
            partidaCargadaDesdeArchivo = cargarJuego("partida.txt");
        } else {
            System.out.println("\n» Creando tablero nuevo...\n");
        }
    }

    public static void main(String[] args) {
        menuInicioPrincipal();
        
        if (!partidaCargadaDesdeArchivo) {
            Minas();
            minasAlrededor();
        }

        imprimirCuadricula();
        
        elegirAccionTurno();
        if (usuarioQuisoSalir) {
            System.out.println("¡Gracias por jugar! Hasta pronto.");
            return;
        }

        int fila = Movimiento[0];
        int columna = Movimiento[1];

        while (!Victoria && !Derrota) {
            try {
                if (!modoBandera && Encontradas[fila][columna]) {
                    throw new CasillaYaDescubiertaException("[Aviso] Esa casilla ya está descubierta. Elige otra.");
                }

                if (modoBandera) {
                    if (!Encontradas[fila][columna]) {
                        Banderas[fila][columna] = !Banderas[fila][columna];
                    } else {
                        System.out.println("[Aviso] No puedes colocar una bandera sobre casillas ya visibles.");
                    }
                    modoBandera = false;
                } else if (Banderas[fila][columna]) {
                    System.out.println("[Aviso] Celda protegida por bandera. Quítala en el menú si deseas pisarla.");
                } else if (Tablero[fila][columna] == 'X') {
                    Derrota = true;
                    revelarCeldas();
                    imprimirCuadricula();
                    System.out.println("¡PERDISTE! Has pisado una mina terrestre.");
                    break;
                } else {
                    revelarCeldas(fila, columna);
                }

                if (comprobarVictoria()) {
                    Victoria = true;
                    imprimirCuadricula();
                    System.out.println("¡FELICIDADES! Has limpiado todas las minas de la zona.");
                    break;
                }

                imprimirCuadricula();
                
                elegirAccionTurno();
                if (usuarioQuisoSalir) {
                    System.out.println("Progreso guardado. ¡Gracias por jugar!");
                    return;
                }

                fila = Movimiento[0];
                columna = Movimiento[1];

            } catch (CasillaYaDescubiertaException e) {
                System.out.println(e.getMessage() + "\n");
                elegirAccionTurno();
                if (usuarioQuisoSalir) return;
                fila = Movimiento[0];
                columna = Movimiento[1];
            }
        }
    }
