package ups.poo.buscaminas.datos;

import ups.poo.buscaminas.modelos.PadreBuscaminas;
import ups.poo.buscaminas.modelos.Celdas;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GestorArchivos {

    public static boolean partidaCargadaDesdeArchivo = false;
    public static boolean usuarioQuisoSalir = false;
    private final String RUTA_ARCHIVO_BUSCAMINAS = "poo_unidad_4/partida.txt";

    public void guardarPartida() {
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
            System.out.println("[Error] No se pudo guardar la partida.");
        }
    }

    public boolean cargarPartida() {
        try (BufferedReader lector = new BufferedReader(new FileReader(RUTA_ARCHIVO_BUSCAMINAS))) {
            Celdas.setRestantes(Integer.parseInt(lector.readLine()));
            for (int i = 0; i < PadreBuscaminas.getFilas(); i++) {
                String linea = lector.readLine();
                for (int j = 0; j < PadreBuscaminas.getColumnas(); j++) {
                    PadreBuscaminas.getTablero()[i][j] = linea.charAt(j);
                }
            }
            for (int i = 0; i < PadreBuscaminas.getFilas(); i++) {
                String linea = lector.readLine();
                for (int j = 0; j < PadreBuscaminas.getColumnas(); j++) {
                    PadreBuscaminas.getEncontradas()[i][j] = linea.charAt(j) == '1';
                }
            }
            for (int i = 0; i < PadreBuscaminas.getFilas(); i++) {
                String linea = lector.readLine();
                for (int j = 0; j < PadreBuscaminas.getColumnas(); j++) {
                    PadreBuscaminas.getBanderas()[i][j] = linea.charAt(j) == '1';
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

}
