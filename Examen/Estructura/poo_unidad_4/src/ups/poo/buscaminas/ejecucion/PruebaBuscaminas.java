package ups.poo.buscaminas.ejecucion;

import ups.poo.buscaminas.vista.MenuVista;
import ups.poo.buscaminas.controlador.CatalogoControlador;
import ups.poo.buscaminas.excepciones.ErrorAlCargarPartida;
import ups.poo.buscaminas.excepciones.ErrorAlGuardarPartida;
import ups.poo.buscaminas.excepciones.OpcionInvalida;

public class PruebaBuscaminas {
    public static void main(String[] args) throws OpcionInvalida, ErrorAlCargarPartida, ErrorAlGuardarPartida {
        MenuVista vista = new MenuVista();
        CatalogoControlador controlador = new CatalogoControlador(vista);
        controlador.controladorInicio();
    }
}
