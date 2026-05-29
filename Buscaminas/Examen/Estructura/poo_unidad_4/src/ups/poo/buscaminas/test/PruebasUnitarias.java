package ups.poo.buscaminas.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ups.poo.buscaminas.datos.GestorArchivos;
import ups.poo.buscaminas.excepciones.ErrorAlCargarPartida;
import ups.poo.buscaminas.modelos.PadreBuscaminas;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GestorArchivosTest {

    private final String rutaTest = "partida.txt";

    @BeforeEach
    void setUp() throws IOException {
        // 1. Configuramos el tamaño esperado del tablero estático para el test
        PadreBuscaminas.getFilas();
        PadreBuscaminas.getColumnas();

        // 2. Creamos un archivo de guardado falso 
        File archivo = new File(rutaTest);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            writer.write("10\n");
            writer.write("V1\n");
            writer.write("1-\n");
            writer.write("10\n");
            writer.write("01\n");
        }
    }

    @AfterEach
    void tearDown() {
        // Limpiamos el archivo después de la prueba
        File archivo = new File(rutaTest);
        if (archivo.exists()) {
            archivo.delete();
        }
    }

    @Test
    void testCargarPartidaExitosamente() throws ErrorAlCargarPartida {
        GestorArchivos gestor = new GestorArchivos();

        boolean resultado = gestor.cargarPartida();

        assertTrue(resultado, "El método debería retornar true al cargar correctamente");

        char[][] tableroCargado = PadreBuscaminas.getTablero();
        assertEquals('V', tableroCargado[0][0]);
        assertEquals('1', tableroCargado[0][1]);
        assertEquals('1', tableroCargado[1][0]);
        assertEquals('-', tableroCargado[1][1]);
    }
}