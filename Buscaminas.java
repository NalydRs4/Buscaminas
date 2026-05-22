import java.util.Scanner;

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
        System.out.print("Ingrese la fila: (A-J) ");
        char letraFila = Leer.next().toUpperCase().charAt(0);
        Movimiento[0] = letraFila - 'A' + 1;
        System.out.print("Ingrese la columna: (1-10) ");
        Movimiento[1] = Leer.nextInt();
    }

    public static void main(String[] args) {
        System.out.println("¡Bienvenido al Buscaminas!");
        Iniciar(); 
        imprimirCuadricula();
        Movimiento();
        int fila = Movimiento[0];
        int columna = Movimiento[1];
        Minas();
        minasAlrededor();
        while (!Victoria && !Derrota) {
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
                Movimiento();
                fila = Movimiento[0];
                columna = Movimiento[1];
            }
        }
    }
}
