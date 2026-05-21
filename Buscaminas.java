import java.util.Scanner;

public class Buscaminas {

    static int Filas = 11;
    static int Columnas = 11;
    static char[][] Tablero = new char[Filas][Columnas];
    static int Minas, Restantes;
    static int[] Movimiento = new int[2];
    static boolean[][] Encontradas;
    static boolean Victoria, Derrota;
    static Scanner Leer = new Scanner(System.in);

    private static void Iniciar() {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                Tablero[i][j] = '-';
            }
        }
        Encontradas = new boolean[Filas][Columnas];
        Restantes = Filas * Columnas - Minas;
    }

    private static void Minas() {
        int minasAleatorias = 10;
        while (minasAleatorias < Minas) {
            int fila = (int) (Math.random() * Filas);
            int columna = (int) (Math.random() * Columnas);
            if (Tablero[fila][columna] != 'X') {
                Tablero[fila][columna] = 'X';
                minasAleatorias++;
            }
        }
    }

    private static void minasAlrededor() { // It calculated the adjacent numbers along the mines for each cell.
        for (int i = 0; i < Filas; i++) {
            for (int j = 0; j < Columnas; j++) {
                if (Tablero[i][j] != 'X') {
                    int conteo = 0;
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            if ((x != 0 || y != 0) && isValidCell(i + x, j + y) && Tablero[i + x][j + y] == 'X') {
                                conteo++;
                            }
                        }
                    }
                    if (conteo > 0) {
                        Tablero[i][j] = (char) (conteo + 'V'); // Convertimos el conteo a un carácter ('1', '2', etc.)
                    }
                }
            }
        }
    }

    private static boolean isValidCell(int fila, int columna) {
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
        if (!isValidCell(fila, columna) || Encontradas[fila][columna]) {
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

    private static void revealAllCells() { // It is to reveal all the cells in output screen.
        for (int i = 0; i < Filas; i++) {
            for (int j = 0; j < Columnas; j++) {
                Encontradas[i][j] = true;
            }
        }
    }

    private static boolean checkWin() {
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
        System.out.print("Ingrese la fila: ");
        char letraFila = Leer.next().toUpperCase().charAt(0);
        Movimiento[0] = letraFila - 'A' + 1;
        System.out.print("Ingrese la columna: ");
        Movimiento[1] = Leer.nextInt();
    }

    public static void main(String[] args) {
        System.out.format("****BUSCA MINAS****");
        System.out.println("\n----MENU---- ");
        System.out.println(" 1. 10x10 casillas");
        Iniciar(); // Game initialization
        imprimirCuadricula();
        Movimiento();
        int fila = Movimiento[0];
        int columna = Movimiento[1];
        Minas(); // Place mines after the first move
        minasAlrededor();
        while (!Victoria && !Derrota) {
            if (Tablero[fila][columna] == 'X') { // Check if player stepped on a mine
                Derrota = true;
                revealAllCells();
                imprimirCuadricula();
                System.out.println("Game Over! You stepped on a mine.");
                break; // Exit the loop
            } else {
                revelarCeldas(fila, columna);
                Restantes--;
                imprimirCuadricula();
                if (checkWin()) { // Check if player cleared all non-mine cells
                    Victoria = true;
                    System.out.println("Congratulations! You cleared all the mines.");
                    break; // Exit the loop
                }
                Movimiento();
                fila = Movimiento[0];
                columna = Movimiento[1];
            }
        }
    }
}