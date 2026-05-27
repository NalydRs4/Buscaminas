package ups.poo.buscaminas.ejecucion;

import ups.poo.buscaminas.vista.MenuVista;
import ups.poo.buscaminas.controlador.CatalogoControlador;

public class PruebaBuscaminas {
    public static void main(String[] args) {
        MenuVista vista = new MenuVista();
        CatalogoControlador controlador = new CatalogoControlador(vista);
        controlador.controladorInicio();
    }
}
