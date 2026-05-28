package ups.poo.buscaminas.datos;

import ups.poo.buscaminas.modelos.PadreBuscaminas;
import ups.poo.buscaminas.modelos.Celdas;
import ups.poo.buscaminas.excepciones.ErrorAlGuardarPartida;
import ups.poo.buscaminas.excepciones.ErrorAlCargarPartida;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GestorArchivos {

    public static boolean partidaCargadaDesdeArchivo = false;
    public static boolean usuarioQuisoSalir = false;
    // Ruta del archivo guardado de la partida
    private static final String RUTA_ARCHIVO_BUSCAMINAS = "partida_guardada.txt";

    // Método para guardar la partida en un archivo con excepción personalizada
    public static void guardarPartida() throws ErrorAlGuardarPartida {
        try (PrintWriter escritor = new PrintWriter(new FileWriter(RUTA_ARCHIVO_BUSCAMINAS))) {
            escritor.println(Celdas.getRestantes());
            for (int i = 0; i < PadreBuscaminas.getFilas(); i++) {
                for (int j = 0; j < PadreBuscaminas.getColumnas(); j++) {
                    escritor.print(PadreBuscaminas.getTablero()[i][j]);
                }
                escritor.println();
            }
            for (int i = 0; i < PadreBuscaminas.getFilas(); i++) {
                for (int j = 0; j < PadreBuscaminas.getColumnas(); j++) {
                    escritor.print(PadreBuscaminas.getEncontradas()[i][j] ? "1" : "0");
                }
                escritor.println();
            }
            for (int i = 0; i < PadreBuscaminas.getFilas(); i++) {
                for (int j = 0; j < PadreBuscaminas.getColumnas(); j++) {
                    escritor.print(PadreBuscaminas.getBanderas()[i][j] ? "1" : "0");
                }
                escritor.println();
            }
        } catch (IOException error) {
            throw new ErrorAlGuardarPartida("Error al guardar la partida: " + error.getMessage());
        }
    }

    // Método para cargar la partida desde un archivo con excepción personalizada
    public boolean cargarPartida() throws ErrorAlCargarPartida {
        try (BufferedReader lector = new BufferedReader(new FileReader(RUTA_ARCHIVO_BUSCAMINAS))) {
            Celdas.setRestantes(Integer.parseInt(lector.readLine()));

            int filas = PadreBuscaminas.getFilas();
            int columnas = PadreBuscaminas.getColumnas();

            char[][] nuevoTablero = new char[filas][columnas];
            boolean[][] nuevasEncontradas = new boolean[filas][columnas];
            boolean[][] nuevasBanderas = new boolean[filas][columnas];

            for (int i = 0; i < filas; i++) {
                String linea = lector.readLine();
                for (int j = 0; j < columnas; j++) {
                    nuevoTablero[i][j] = linea.charAt(j);
                }
            }

            PadreBuscaminas.setTablero(nuevoTablero);

            for (int i = 0; i < filas; i++) {
                String linea = lector.readLine();
                for (int j = 0; j < columnas; j++) {
                    nuevasEncontradas[i][j] = linea.charAt(j) == '1';
                }
            }

            PadreBuscaminas.setEncontradas(nuevasEncontradas);

            for (int i = 0; i < filas; i++) {
                String linea = lector.readLine();
                for (int j = 0; j < columnas; j++) {
                    nuevasBanderas[i][j] = linea.charAt(j) == '1';
                }
            }

            PadreBuscaminas.setBanderas(nuevasBanderas);

            partidaCargadaDesdeArchivo = true;
            return true;
        } catch (IOException error) {
            throw new ErrorAlCargarPartida("Error al cargar la partida: " + error.getMessage());
        }
    }
}
