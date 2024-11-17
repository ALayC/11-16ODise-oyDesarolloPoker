package Controladores;

import Dominio.Carta;
import Dominio.EstadoMano;
import Dominio.Mano;
import Dominio.Mazo;
import Dominio.Mesa;
import Dominio.Participacion;
import IU.DialogPanelCartas;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class JuegoPokerControlador {

    private Mesa mesa;
    private Mazo mazo;
    private List<DialogPanelCartas> vistas;
    private boolean juegoActivo;
    
    public JuegoPokerControlador(Mesa mesa) {
        this.mesa = mesa;
        this.mazo = new Mazo();
        this.vistas = new ArrayList<>();
        this.juegoActivo = false;
    }

    // Método para iniciar el juego
public void iniciarJuego() {
    mesa.iniciarMesa();
    mazo.barajar();
    vistas = new ArrayList<>(mesa.getParticipaciones().size());

    for (Participacion participacion : mesa.getParticipaciones()) {
        DialogPanelCartas vista = new DialogPanelCartas(null);
        configurarVistaJugador(vista, participacion);
        vistas.add(vista); // Sincroniza las vistas con las participaciones
    }

    juegoActivo = true;
    actualizarEstadoMano(EstadoMano.ESPERANDO_APUESTA);
}

    // Métodos para manejar las acciones de los jugadores
    public void manejarPasar(Participacion participacion) {
        participacion.pasar();
        verificarEstadoMano();
    }

    public void manejarApostar(Participacion participacion, double monto) {
        if (participacion.apostar(monto)) {
            actualizarEstadoMano(EstadoMano.ESPERANDO_APUESTA);
        } else {
            mostrarError("Saldo insuficiente para apostar.");
        }
    }

    public void manejarPagar(Participacion participacion, double monto) {
        if (participacion.pagarApuesta(monto)) {
            actualizarEstadoMano(EstadoMano.ESPERANDO_CARTAS);
        } else {
            mostrarError("Saldo insuficiente para pagar.");
        }
    }

    public void manejarSalir(Participacion participacion) {
        mesa.eliminarParticipacion(participacion);
        cerrarVistaJugador(participacion);
        verificarEstadoMesa();
    }

    // Métodos auxiliares para el flujo del juego
    private void verificarEstadoMano() {
        if (mesa.getManoActual().todosHanPasado()) {
            mesa.getManoActual().acumularPozo();
            iniciarNuevaMano();
        }
    }

    private void determinarGanador() {
        Participacion ganador = mesa.getManoActual().determinarGanador();
        mostrarGanador(ganador);
    }

    private void iniciarNuevaMano() {
        mazo.reiniciarMazo();
        mazo.barajar();

        for (Participacion participacion : mesa.getParticipaciones()) {
            participacion.agregarCartas(mazo.repartirMano(5));
        }

        actualizarEstadoMano(EstadoMano.ESPERANDO_APUESTA);
    }

    // Métodos para manejar las vistas
    private void mostrarVistaJugador(Participacion participacion) {
        int index = mesa.getParticipaciones().indexOf(participacion);

        if (index >= 0) {
            DialogPanelCartas vista = vistas.get(index);

            if (vista == null) {
                vista = new DialogPanelCartas(null);
                configurarVistaJugador(vista, participacion);
                vistas.set(index, vista); // Actualiza la lista de vistas
            }

            // Actualizar las cartas y mostrar la vista
            vista.cargarCartas(new ArrayList<>(participacion.getCartas()));
            vista.setVisible(true);
        }
    }


    private void cerrarVistaJugador(Participacion participacion) {
        int index = mesa.getParticipaciones().indexOf(participacion);

        if (index >= 0) {
            DialogPanelCartas vista = vistas.get(index);
            if (vista != null) {
                vista.dispose(); // Cierra la ventana
                vistas.set(index, null); // Limpia la referencia
            }
        }
    }


    private void mostrarGanador(Participacion ganador) {
        JOptionPane.showMessageDialog(null,
            "Ganador: " + ganador.getUnJugador().getNombreCompleto() +
            "\nFigura: " + ganador.getFigura() +
            "\nPozo ganado: " + mesa.getManoActual().getPozo());
    }

public void actualizarEstadoMano(EstadoMano estado) {
    mesa.getManoActual().setEstado(estado);
    for (Participacion participacion : mesa.getParticipaciones()) {
        vistas.get(mesa.getParticipaciones().indexOf(participacion))
            .setTitle("Estado: " + estado.name());
    }
}

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void verificarEstadoMesa() {
        if (mesa.getParticipaciones().isEmpty()) {
            juegoActivo = false;
            JOptionPane.showMessageDialog(null, "La mesa se ha quedado sin jugadores.");
        }
    }
    
    private void configurarAccionesDialog(DialogPanelCartas dialog, Mesa mesa, Participacion participacion, int turnoActual) {
        dialog.getBtnPasar().addActionListener(e -> {
            participacion.pasar(); // Registrar que el jugador pasó
            dialog.dispose(); // Cierra la ventana del jugador actual
            verificarFinDeTurno(mesa, turnoActual); // Verificar si el turno terminó
        });

        dialog.getBtnApostar().addActionListener(e -> {
            double monto = obtenerMontoApuesta(); // Solicita al jugador el monto de la apuesta
            participacion.apostar(monto); // Registrar la apuesta
            dialog.dispose(); // Cierra la ventana del jugador actual
            verificarFinDeTurno(mesa, turnoActual); // Verificar si el turno terminó
        });

        dialog.getBtnPagar().addActionListener(e -> {
             double monto = obtenerMontoApuesta(); 
            participacion.pagarApuesta(monto); // Registrar el pago de la apuesta
            dialog.dispose(); // Cierra la ventana del jugador actual
            verificarFinDeTurno(mesa, turnoActual); // Verificar si el turno terminó
        });

        dialog.getBtnRetirarse().addActionListener(e -> {
            mesa.eliminarParticipacion(participacion); // Retira al jugador de la mesa
            dialog.dispose(); // Cierra la ventana del jugador actual
            verificarFinDeTurno(mesa, turnoActual); // Verificar si el turno terminó
        });
    }

    private void verificarFinDeTurno(Mesa mesa, int turnoActual) {
            boolean todosHanJugado = mesa.getParticipaciones().stream()
                .allMatch(p -> p.getEstado() != Participacion.Estado.ESPERANDO);

            if (todosHanJugado) {
                if (mesa.getParticipaciones().stream().allMatch(p -> p.getEstado() == Participacion.Estado.PASA)) {
                    finalizarMano(mesa);
                } else {
                    iniciarRondaCartasNuevas();
                }
            } else {
                int siguienteTurno = (turnoActual + 1) % mesa.getParticipaciones().size();
                mostrarVistaJugador(mesa.getParticipaciones().get(siguienteTurno));
            }
    }
    
    private double obtenerMontoApuesta() {
        String input = JOptionPane.showInputDialog("Ingrese el monto de la apuesta:");
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            mostrarError("Monto inválido. Intente nuevamente.");
            return obtenerMontoApuesta(); // Reintentar
        }
    }
    private void finalizarMano(Mesa mesa) {
        determinarGanador();
        iniciarNuevaMano();
    }

    public void iniciarRondaCartasNuevas() {
        System.out.println("Iniciando ronda de cartas nuevas...");
        for (Participacion participacion : mesa.getParticipaciones()) {
            System.out.println("Participacion: " + participacion.getUnJugador().getNombreCompleto());
            System.out.println("Estado: " + participacion.getEstado());
            if (participacion.getEstado() == Participacion.Estado.PAGA) {
                mostrarCartasParaJugador(participacion);
            }
        }
    }




    
    private void configurarVistaJugador(DialogPanelCartas vista, Participacion participacion) {
        vista.setTitle("Jugador: " + participacion.getUnJugador().getNombreCompleto());
        vista.getBtnPasar().addActionListener(e -> manejarPasar(participacion));
        vista.getBtnApostar().addActionListener(e -> manejarApostar(participacion, 100)); // Ejemplo con monto fijo
        vista.getBtnPagar().addActionListener(e -> manejarPagar(participacion, 100));    // Ejemplo con monto fijo
        vista.getBtnRetirarse().addActionListener(e -> manejarSalir(participacion));
    }


    
    public void mostrarCartasParaJugador(Participacion participacion) {
        int index = mesa.getParticipaciones().indexOf(participacion);

        if (index >= 0) {
            DialogPanelCartas vista;

            // Verifica si ya existe una vista para este jugador
            if (index < vistas.size() && vistas.get(index) != null) {
                vista = vistas.get(index);
            } else {
                // Crea una nueva vista si no existe
                vista = new DialogPanelCartas(null);
                configurarVistaJugador(vista, participacion);

                // Sincroniza la lista de vistas con la lista de participaciones
                while (vistas.size() <= index) {
                    vistas.add(null);
                }
                vistas.set(index, vista);
            }

            // Actualiza las cartas del jugador y muestra la vista
            vista.cargarCartas(new ArrayList<>(participacion.getCartas()));
            vista.setVisible(true);
        }
    }
    
}
