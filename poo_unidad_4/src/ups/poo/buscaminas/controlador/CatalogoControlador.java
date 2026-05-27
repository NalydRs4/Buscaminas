package ups.poo.buscaminas.controlador;

import ups.poo.buscaminas.modelos.PadreBuscaminas;
import ups.poo.buscaminas.modelos.Celdas;
import ups.poo.buscaminas.modelos.Minas;
import ups.poo.buscaminas.vista.MenuVista;
import ups.poo.buscaminas.datos.GestorArchivos;
import ups.poo.buscaminas.excepciones.CasillaYaDescubiertaException;

import java.util.InputMismatchException;

public class CatalogoControlador {

    GestorArchivos gestor = new GestorArchivos();
    public static boolean modoBandera = false;
    private MenuVista vista;
    static boolean Victoria, Derrota;
    private int[] movimiento = new int[2];

    public CatalogoControlador(MenuVista vista) {
        this.vista = vista;
    }

    public void controladorInicio() {
        boolean salir = false;
        while (!salir) {
            int opcion = vista.menuPrincipal();
            switch (opcion) {
                case 1:
                    iniciarPartida();
                    vista.imprimirTablero();
                    while (!Victoria && !Derrota) {
                        pedirMovimiento();
                        if (GestorArchivos.usuarioQuisoSalir) {
                            System.out.println("Progreso guardado");
                            break;
                        }
                        int fila = movimiento[0];
                        int columna = movimiento[1];
                        try {
                            if (!modoBandera && Celdas.getEncontradas()[fila][columna]) {
                                throw new CasillaYaDescubiertaException(
                                        "[Aviso] Esa casilla ya está descubierta. Elige otra.");
                            }
                            if (modoBandera) {
                                if (!Celdas.getEncontradas()[fila][columna]) {
                                    Celdas.getBanderas()[fila][columna] = !Celdas.getBanderas()[fila][columna];
                                } else {
                                    System.out.println(
                                            "[Aviso] No puedes colocar una bandera sobre casillas ya visibles.");
                                }
                                modoBandera = false;
                            } else if (Celdas.getBanderas()[fila][columna]) {
                                System.out.println(
                                        "[Aviso] Celda protegida por bandera. Quítala en el menú si deseas pisarla.");
                            } else if (PadreBuscaminas.getTablero()[fila][columna] == 'X') {
                                Derrota = true;
                                Celdas.revelarCeldas1();
                                vista.imprimirTablero();
                                ;
                                System.out.println("¡PERDISTE! Has pisado una mina terrestre.");
                                break;
                            } else {
                                Celdas.revelarCeldas0(fila, columna);
                            }

                            if (comprobarVictoria()) {
                                Victoria = true;
                                vista.imprimirTablero();
                                ;
                                System.out.println("¡FELICIDADES! Has limpiado todas las minas de la zona.");
                                break;
                            }

                            if (!Victoria && !Derrota) {
                                vista.imprimirTablero();
                            }

                        } catch (CasillaYaDescubiertaException e) {
                            System.out.println(e.getMessage() + "\n");
                        }
                    }
                    break;
                case 2:
                    GestorArchivos.partidaCargadaDesdeArchivo = gestor.cargarPartida();
                    break;
                case 3:
                    salir = true;
                    vista.mostrarMensaje("Saliendo del programa...");
                    break;
                default:
                    vista.mostrarMensaje("Opción no válida. Intente de nuevo.");
            }
        }
    }

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

        Celdas.setRestantes(PadreBuscaminas.getFilas() * PadreBuscaminas.getColumnas() - PadreBuscaminas.getNumeroMinas());
    }

    private void pedirMovimiento() {
        while (true) {
            try {
                int opcion = vista.accionJuego();
                if (opcion == 3) {
                    gestor.guardarPartida();
                    GestorArchivos.usuarioQuisoSalir = true;
                    return;
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
                        System.out.println("[Error] Coordenada fuera del tablero. Intente de nuevo.\n");
                    }
                } else {
                    System.out.println("[Error] Opción inválida. Elige 1, 2 o 3.\n");
                }
            } catch (InputMismatchException | NumberFormatException | StringIndexOutOfBoundsException e) {
                System.out.println("[Error] Formato incorrecto de datos. Intente de nuevo.\n");
                vista.limpiarBuffer();
            }
        }
    }

    private static boolean comprobarVictoria() {
        return Celdas.getRestantes() == 0;
    }

}
