package ups.poo.buscaminas.modelos;

public abstract class PadreBuscaminas {

    private static int filas = 10;
    private static int columnas = 10;
    private static int numeroMinas = 10;
    private static boolean[][] banderas;
    private static boolean[][] encontradas;
    private static char[][] Tablero = new char[filas][columnas];

    public PadreBuscaminas(int filas, int columnas, boolean[][] banderas, boolean[][] encontradas, char[][] tablero, int numeroMinas) {
        PadreBuscaminas.filas = filas;
        PadreBuscaminas.columnas = columnas;
        PadreBuscaminas.numeroMinas = numeroMinas;
        PadreBuscaminas.banderas = banderas;
        PadreBuscaminas.encontradas = encontradas;
        PadreBuscaminas.Tablero = tablero;
    }

    public static void setBanderas(boolean[][] banderas) {
        PadreBuscaminas.banderas = banderas;
    }

    public static void setEncontradas(boolean[][] encontradas) {
        PadreBuscaminas.encontradas = encontradas;
    }

    public static int getFilas() {
        return filas;
    }

    public static int getColumnas() {
        return columnas;
    }

    public static int getNumeroMinas() {
        return numeroMinas;
    }

    public static boolean[][] getBanderas() {
        return banderas;
    }

    public static boolean[][] getEncontradas() {
        return encontradas;
    }

    public static char[][] getTablero() {
        return Tablero;
    }

}
