package Controladores;

import Dominio.*;
import IU.DialogPanelCartas;
import Interfaces.VistaControlMesa;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class JuegoPokerControlador {

    private Mesa mesa;
    private Mazo mazo;
    private List<DialogPanelCartas> vistas;
    private boolean juegoActivo;
    private VistaControlMesa vistaControlMesa;
    public JuegoPokerControlador(Mesa mesa, VistaControlMesa vistaControlMesa) {
        this.mesa = mesa;
        this.mazo = new Mazo();
        this.vistas = new ArrayList<>();
        this.juegoActivo = false;
        this.vistaControlMesa = vistaControlMesa;
    }

    public void iniciarJuego() {
        mesa.iniciarMesa();
        mazo.barajar();
        vistas = new ArrayList<>(mesa.getParticipaciones().size());

        for (Participacion participacion : mesa.getParticipaciones()) {
            DialogPanelCartas vista = new DialogPanelCartas(null);
            configurarVistaJugador(vista, participacion);
            vistas.add(vista);
        }

        juegoActivo = true;
        actualizarEstadoMano(EstadoMano.ESPERANDO_APUESTA);

        // Mostrar todas las vistas al mismo tiempo
        mostrarCartasParaTodos();
    }

    public void manejarPasar(Participacion participacion) {
        participacion.pasar();
        verificarEstadoMano();
    }

    public void manejarApostar(Participacion participacion, double monto) {
        if (participacion.apostar(monto)) {
            actualizarEstadoMano(EstadoMano.ESPERANDO_APUESTA);
            verificarEstadoMano();
        } else {
            mostrarError("Saldo insuficiente para apostar.");
        }
    }

    public void manejarPagar(Participacion participacion, double monto) {
        if (participacion.pagarApuesta(monto)) {
            actualizarEstadoMano(EstadoMano.ESPERANDO_CARTAS);
            verificarEstadoMano();
        } else {
            mostrarError("Saldo insuficiente para pagar.");
        }
    }

    public void manejarSalir(Participacion participacion) {
        mesa.eliminarParticipacion(participacion);
        cerrarVistaJugador(participacion);
        verificarEstadoMesa();
    }

    public void finalizarMano() {
        for (Participacion participacion : mesa.getParticipaciones()) {
            participacion.calcularFigura(); // Calcula la figura de cada jugador
        }
        determinarGanador(); // Determina el ganador basado en las figuras calculadas
        iniciarNuevaMano(); // Reinicia para la próxima mano
    }

    public void iniciarRondaCartasNuevas() {
        for (Participacion participacion : mesa.getParticipaciones()) {
            if (participacion.getEstado() == Participacion.Estado.PAGA) {
                mostrarCartasParaJugador(participacion);
            }
        }
    }

    public void verificarFinDeTurno(int turnoActual) {
        boolean todosHanJugado = mesa.getParticipaciones().stream()
            .allMatch(p -> p.getEstado() != Participacion.Estado.ESPERANDO);

        if (todosHanJugado) {
            if (mesa.getParticipaciones().stream().allMatch(p -> p.getEstado() == Participacion.Estado.PASA)) {
                finalizarMano();
            } else {
                iniciarRondaCartasNuevas();
            }
        } else {
            int siguienteTurno = (turnoActual + 1) % mesa.getParticipaciones().size();
            mostrarCartasParaJugador(mesa.getParticipaciones().get(siguienteTurno));
        }
    }

    private void iniciarNuevaMano() {
        mazo.reiniciarMazo();
        mazo.barajar();

        for (Participacion participacion : mesa.getParticipaciones()) {
            participacion.agregarCartas(mazo.repartirMano(5));
        }

        actualizarEstadoMano(EstadoMano.ESPERANDO_APUESTA);
    }

    private void determinarGanador() {
        Participacion ganador = null;
        for (Participacion participacion : mesa.getParticipaciones()) {
            if (ganador == null || participacion.getFigura().esMejorQue(ganador.getFigura())) {
                ganador = participacion;
            }
        }
        if (ganador != null) {
            ganador.esGanador(mesa.getPozo()); // Otorga el pozo al ganador
            vistaControlMesa.mostrarMensaje("Ganador: " + ganador.getUnJugador().getNombreCompleto() +
                    " | Figura: " + ganador.getFigura());
        }
    }


    public void mostrarCartasParaJugador(Participacion participacion) {
        int index = mesa.getParticipaciones().indexOf(participacion);

        if (index >= 0) {
            DialogPanelCartas vista;

            if (index < vistas.size() && vistas.get(index) != null) {
                vista = vistas.get(index);
            } else {
                vista = new DialogPanelCartas(null);
                configurarVistaJugador(vista, participacion);
                while (vistas.size() <= index) {
                    vistas.add(null);
                }
                vistas.set(index, vista);
            }

            vista.cargarCartas(new ArrayList<>(participacion.getCartas()));
            vista.setVisible(true);
        }
    }

    private void cerrarVistaJugador(Participacion participacion) {
        int index = mesa.getParticipaciones().indexOf(participacion);

        if (index >= 0) {
            DialogPanelCartas vista = vistas.get(index);
            if (vista != null) {
                vista.dispose();
                vistas.set(index, null);
            }
        }
    }

    private void actualizarEstadoMano(EstadoMano estado) {
        mesa.getManoActual().setEstado(estado);
        for (Participacion participacion : mesa.getParticipaciones()) {
            vistas.get(mesa.getParticipaciones().indexOf(participacion))
                .setTitle("Estado: " + estado.name());
        }
    }

    private void mostrarError(String mensaje) {
        vistaControlMesa.mostrarError(mensaje);
    }

    private void verificarEstadoMesa() {
        if (mesa.getParticipaciones().isEmpty()) {
            juegoActivo = false;
            JOptionPane.showMessageDialog(null, "La mesa se ha quedado sin jugadores.");
        }
    }

    private void configurarVistaJugador(DialogPanelCartas vista, Participacion participacion) {
        Jugador jugador = participacion.getUnJugador();
        String titulo = "Jugador: " + jugador.getNombreCompleto() + " | Saldo: $" + jugador.getSaldo();
        vista.setTitle(titulo); // Configura el título
        SwingUtilities.invokeLater(() -> vista.repaint()); // Forzar la actualización visual

        // Configura las acciones de los botones
        vista.getBtnPasar().addActionListener(e -> manejarPasar(participacion));
        vista.getBtnApostar().addActionListener(e -> manejarApostar(participacion, obtenerMontoApuesta()));
        vista.getBtnPagar().addActionListener(e -> manejarPagar(participacion, obtenerMontoApuesta()));

        vista.getBtnRetirarse().addActionListener(e -> manejarSalir(participacion));
    }

    private double obtenerMontoApuesta() {
        while (true) {
            try {
                String input = JOptionPane.showInputDialog(null, "Ingrese el monto de su apuesta:");
                if (input == null || input.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debe ingresar un monto válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Monto inválido. Por favor, intente nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    
    private void verificarEstadoMano() {
        boolean todosHanJugado = mesa.getParticipaciones().stream()
            .allMatch(p -> p.getEstado() != Participacion.Estado.ESPERANDO);

        if (todosHanJugado) {
            if (mesa.getParticipaciones().stream().allMatch(p -> p.getEstado() == Participacion.Estado.PASA)) {
                finalizarMano();
            } else {
                iniciarRondaCartasNuevas();
            }
        } else {
            Participacion siguienteParticipacion = mesa.getParticipaciones().stream()
                .filter(p -> p.getEstado() == Participacion.Estado.ESPERANDO)
                .findFirst()
                .orElse(null);

            if (siguienteParticipacion != null) {
                mostrarCartasParaJugador(siguienteParticipacion);
            }
        }
    }
    
    public void mostrarCartasParaTodos() {
        mesa.getParticipaciones().forEach(participacion -> {
            mostrarCartasParaJugador(participacion); // Actualiza la vista individual
        });

        // Asegurar que todas las vistas se hacen visibles simultáneamente
        SwingUtilities.invokeLater(() -> {
            vistas.forEach(vista -> {
                if (vista != null) {
                    vista.setVisible(true);
                }
            });
        });
    }

}
