package ups.poo.buscaminas.modelos;

// Clase Celdas que hereda de PadreBuscaminas
public class Celdas extends PadreBuscaminas {

    private static int restantes;

    // Constructor de la clase Celdas con super de PadreBuscaminas
    public Celdas(int filas, int columnas, int numeroMinas, boolean[][] banderas, boolean[][] encontradas, char[][] tablero) {
        super(filas, columnas, banderas, encontradas, tablero, numeroMinas);
    }

    // Método para verificar si una celda es válida en el tablero
    public static boolean celdaValida(int fila, int columna) {
        return fila >= 0 && fila < getFilas() && columna >= 0 && columna < getColumnas();
    }

    // Método para revelar una celda seleccionada por el usuario
    public static void revelarCeldas0(int fila, int columna) {
        if (!celdaValida(fila, columna) || getEncontradas()[fila][columna] || getBanderas()[fila][columna]) {
            return;
        }
        getEncontradas()[fila][columna] = true;
        restantes--;
        if (getTablero()[fila][columna] == '-') {
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    if (x != 0 || y != 0) {
                        revelarCeldas0(fila + x, columna + y);
                    }
                }
            }
        }
    }

    // Método para revelar todas las celdas en el tablero
    public static void revelarCeldas1() {
        for (int i = 0; i < getFilas(); i++) {
            for (int j = 0; j < getColumnas(); j++) {
                getEncontradas()[i][j] = true;
            }
        }
    }

    // Getters y Setters para las celdas restantes
    public static int getRestantes() {
        return restantes;
    }

    public static void setRestantes(int restantes) {
        Celdas.restantes = restantes;
    }
}