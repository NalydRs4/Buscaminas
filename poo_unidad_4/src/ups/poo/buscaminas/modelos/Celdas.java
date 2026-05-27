package ups.poo.buscaminas.modelos;


public class Celdas extends PadreBuscaminas {

    private static int restantes;

    public Celdas(int filas, int columnas, int numeroMinas, boolean[][] banderas, boolean[][] encontradas,
            char[][] tablero) {
        super(filas, columnas, banderas, encontradas, tablero, numeroMinas);
    }

    public static boolean celdaValida(int fila, int columna) {
        return fila >= 0 && fila < getFilas() && columna >= 0 && columna < getColumnas();
    }

    public static void revelarCeldas0(int fila, int columna) {
        if (!celdaValida(fila, columna) || getEncontradas()[fila][columna] || getBanderas()[fila][columna]) {
            return;
        }
        getEncontradas()[fila][columna] = true;
        restantes--;
        if (getTablero()[fila][columna] == '-') {
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    revelarCeldas0(fila + x, columna + y);
                }
            }
        }
    }

    public static void revelarCeldas1() {
        for (int i = 0; i < getFilas(); i++) {
            for (int j = 0; j < getColumnas(); j++) {
                getEncontradas()[i][j] = true;
            }
        }
    }

    public static int getRestantes() {
        return restantes;
    }

    public static void setRestantes(int restantes) {
        Celdas.restantes = restantes;
    }

}
