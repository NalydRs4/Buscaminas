package ups.poo.buscaminas.controlador;

import ups.poo.buscaminas.modelos.PadreBuscaminas;
import ups.poo.buscaminas.modelos.Celdas;
import ups.poo.buscaminas.modelos.Minas;
import ups.poo.buscaminas.vista.MenuVista;
import ups.poo.buscaminas.datos.GestorArchivos;
import ups.poo.buscaminas.excepciones.CasillaYaDescubiertaException;
import ups.poo.buscaminas.excepciones.ErrorAlCargarPartida;
import ups.poo.buscaminas.excepciones.ErrorAlGuardarPartida;
import ups.poo.buscaminas.excepciones.OpcionInvalida;

public class CatalogoControlador {

    GestorArchivos gestor = new GestorArchivos();
    public static boolean modoBandera = false;
    private MenuVista vista;
    static boolean Victoria, Derrota;
    private int[] movimiento = new int[2];

    // Constructor de la clase CatalogoControlador
    public CatalogoControlador(MenuVista vista) {
        this.vista = vista;
    }

    // Método para iniciar el juego
    public void controladorInicio() throws ErrorAlCargarPartida, ErrorAlGuardarPartida {
        boolean salir = false;
        while (!salir) {
            try {
                int opcion = vista.menuPrincipal();
                switch (opcion) {
                    case 1:
                        Victoria = false;
                        Derrota = false;
                        GestorArchivos.usuarioQuisoSalir = false;
                        iniciarPartida();
                        vista.imprimirTablero();
                        bucleJuego();
                        break;
                    case 2:
                        Victoria = false;
                        Derrota = false;
                        GestorArchivos.usuarioQuisoSalir = false;
                        GestorArchivos.partidaCargadaDesdeArchivo = gestor.cargarPartida();
                        if (GestorArchivos.partidaCargadaDesdeArchivo) {
                            vista.imprimirTablero();
                            bucleJuego();
                        }
                        break;
                    case 3:
                        salir = true;
                        break;
                    default:
                        throw new OpcionInvalida("[ERROR]: Opción incorrecta, intente de nuevo.");
                }
            } catch (OpcionInvalida excepcion) {
                vista.mostrarMensaje("-----------------------------------------");
                System.out.println(excepcion.getMessage());
                vista.mostrarMensaje("-----------------------------------------");
            }
        }
    }

    // Método para ejecutar el Bucle principal del juego
    private void bucleJuego() throws ErrorAlGuardarPartida, OpcionInvalida {

        while (!Victoria && !Derrota) {
            pedirMovimiento();
            if (GestorArchivos.usuarioQuisoSalir) {
                vista.mostrarMensaje("Progreso gurdado");
                break;
            }
            int fila = movimiento[0];
            int columna = movimiento[1];
            try {
                if (!modoBandera && PadreBuscaminas.getEncontradas()[fila][columna]) {
                    throw new CasillaYaDescubiertaException("[Aviso] Esa casilla ya esta descubierta. Elige otra.");
                }
                if (modoBandera) {
                    if (!PadreBuscaminas.getEncontradas()[fila][columna]) {
                        PadreBuscaminas.getBanderas()[fila][columna] = !PadreBuscaminas.getBanderas()[fila][columna];
                    } else {
                        vista.mostrarMensaje("[Aviso] No puedes colocar una bandera sobre casillas ya visibles");
                    }
                    modoBandera = false;
                } else if (PadreBuscaminas.getBanderas()[fila][columna]) {
                    vista.mostrarMensaje("[Aviso] Celda protegida por bandera");
                } else if (PadreBuscaminas.getTablero()[fila][columna] == 'X') {
                    Derrota = true;
                    Celdas.revelarCeldas1();
                    vista.imprimirTablero();
                    vista.mostrarMensaje("¡PERDISTE! Has pisado una MINA");
                    break;
                } else {
                    Celdas.revelarCeldas0(fila, columna);
                }

                if (comprobarVictoria()) {
                    Victoria = true;
                    vista.imprimirTablero();
                    vista.mostrarMensaje("FELICIDADES! Has limpiado todas las minas de la zona");
                    break;
                }

                if (!Victoria && !Derrota) {
                    vista.imprimirTablero();
                }

            } catch (CasillaYaDescubiertaException excepcion1) {
                System.out.println(excepcion1.getMessage());
            }
        }
    }

    // Método para iniciar una nueva partida
    private void iniciarPartida() {
        for (int i = 0; i < PadreBuscaminas.getFilas(); i++) {
            for (int j = 0; j < PadreBuscaminas.getColumnas(); j++) {
                PadreBuscaminas.getTablero()[i][j] = '-';
            }
        }
        PadreBuscaminas.setEncontradas(new boolean[PadreBuscaminas.getFilas()][PadreBuscaminas.getColumnas()]);
        PadreBuscaminas.setBanderas(new boolean[PadreBuscaminas.getFilas()][PadreBuscaminas.getColumnas()]);

        Minas.colocarMinas();
        Minas.minasAlrededor();

        Celdas.setRestantes(
                PadreBuscaminas.getFilas() * PadreBuscaminas.getColumnas() - PadreBuscaminas.getNumeroMinas());
    }

    // Método para pedir el movimiento del jugador
    private void pedirMovimiento() throws ErrorAlGuardarPartida, OpcionInvalida {
        while (true) {
            try {
                int opcion = vista.accionJuego();
                if (opcion == 3) {
                    try {
                        GestorArchivos.guardarPartida();
                        GestorArchivos.usuarioQuisoSalir = true;
                        vista.mostrarMensaje("Partida Guardada Exitosamente. Adiós...");
                        System.exit(0);
                    } catch (ErrorAlGuardarPartida excepcion) {
                        System.out.println(excepcion.getMessage());
                    }
                }

                if (opcion == 1 || opcion == 2) {
                    modoBandera = (opcion == 2);
                    String coordenada = vista.pedirCoordenada();
                    char letraFila = coordenada.charAt(0);
                    movimiento[0] = letraFila - 'A';
                    String numColumna = coordenada.substring(1);
                    movimiento[1] = Integer.parseInt(numColumna) - 1;

                    if (Celdas.celdaValida(movimiento[0], movimiento[1])) {
                        return;
                    } else {
                        vista.mostrarMensaje("[Error] Coordenada fuera del tablero. Intente de nuevo");
                    }
                } else {
                    throw new OpcionInvalida("[ERROR]: Opción incorrecta, intente de nuevo.");
                }
            } catch (OpcionInvalida excepcion) {
                vista.mostrarMensaje("-----------------------------------------");
                System.out.println(excepcion.getMessage());
                vista.mostrarMensaje("-----------------------------------------");
            }
        }
    }

    // Método para comprobar si el jugador ha ganado
    private static boolean comprobarVictoria() {
        return Celdas.getRestantes() == 0;
    }
}