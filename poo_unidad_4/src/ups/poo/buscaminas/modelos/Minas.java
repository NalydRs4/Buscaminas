package ups.poo.buscaminas.modelos;

public class Minas extends PadreBuscaminas {

    static int minasAleatorias = 0;

    public Minas(int filas, int columnas, int numeroMinas, boolean[][] banderas, boolean[][] encontradas,
            char[][] tablero) {
        super(filas, columnas, banderas, encontradas, tablero, numeroMinas);
    }

    public static void colocarMinas() {
        while (minasAleatorias < getNumeroMinas()) {
            int fila = (int) (Math.random() * getFilas());
            int columna = (int) (Math.random() * getColumnas());
            if (getTablero()[fila][columna] != 'X') {
                getTablero()[fila][columna] = 'X';
                minasAleatorias++;
            }
        }
    }

    public static void minasAlrededor() {
        for (int i = 0; i < getFilas(); i++) {
            for (int j = 0; j < getColumnas(); j++) {
                if (getTablero()[i][j] != 'X') {
                    int conteo = 0;
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            if ((x != 0 || y != 0) && Celdas.celdaValida(i + x, j + y)
                                    && getTablero()[i + x][j + y] == 'X') {
                                conteo++;
                            }
                        }
                    }
                    if (conteo > 0) {
                        getTablero()[i][j] = (char) (conteo + '0');
                    }
                }
            }
        }
    }
}
