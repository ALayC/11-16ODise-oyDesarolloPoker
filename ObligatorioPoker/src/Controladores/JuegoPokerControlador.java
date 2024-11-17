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
        System.out.println("comienza juego");

        for (Participacion participacion : mesa.getParticipaciones()) {
            DialogPanelCartas dialog = new DialogPanelCartas(null, participacion);

            // Configuración de botones y acciones
            configurarVistaJugador(dialog, participacion);

            // Agrega la vista configurada a la lista de vistas
            vistas.add(dialog);

            // Actualizar la vista con los datos iniciales
            actualizarVistaJugador(participacion);
        }

        juegoActivo = true;
        actualizarEstadoMano(EstadoMano.ESPERANDO_APUESTA);

        // Mostrar todas las vistas al mismo tiempo
        mostrarCartasParaTodos();
    }

    public void manejarApostar(Participacion participacion, double monto) {
        if (!mesa.getParticipaciones().contains(participacion)) {
            mostrarError("No puedes apostar, ya no estás en la mesa.");
            return;
        }

        if (participacion.apostar(monto)) {
            mesa.incrementarPozo(monto); // Actualiza el pozo
            System.out.println("Jugador " + participacion.getUnJugador().getNombreCompleto() + " apostó: " + monto);
            System.out.println("Nuevo pozo tras apuesta: " + mesa.getPozo()); // Debug del pozo tras la apuesta
            actualizarEstadoMano(EstadoMano.ESPERANDO_APUESTA);
            verificarEstadoMano();
        } else {
            mostrarError("Saldo insuficiente para apostar.");
        }
    }

    public void manejarPagar(Participacion participacion, double monto) {
        if (!mesa.getParticipaciones().contains(participacion)) {
            mostrarError("No puedes pagar, ya no estás en la mesa.");
            return; // Ignora la acción si el jugador no está en la mesa
        }

        if (participacion.pagarApuesta(monto)) {
            actualizarEstadoMano(EstadoMano.ESPERANDO_CARTAS);
            verificarEstadoMano();
        } else {
            mostrarError("Saldo insuficiente para pagar.");
        }
    }

    public void manejarPasar(Participacion participacion) {
        if (!mesa.getParticipaciones().contains(participacion)) {
            mostrarError("No puedes pasar, ya no estás en la mesa.");
            return; // Ignora la acción si el jugador no está en la mesa
        }

        participacion.pasar();
        verificarEstadoMano();
    }

    public void manejarSalir(Participacion participacion) {
        if (!mesa.getParticipaciones().contains(participacion)) {
            mostrarError("Ya no estás en la mesa.");
            return; // Ignora la acción si el jugador no está en la mesa
        }

        // Elimina la participación de la mesa
        mesa.eliminarParticipacion(participacion);

        // Cierra la vista correspondiente al jugador
        cerrarVistaJugador(participacion);

        // Verifica si la mesa queda vacía
        verificarEstadoMesa();

        // Mostrar mensaje al jugador que salió
        JOptionPane.showMessageDialog(null,
                "Has salido de la mesa. Gracias por participar.",
                "Jugador Retirado",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void finalizarMano() {
        mesa.getParticipaciones().forEach(Participacion::calcularFigura); // Calcula las figuras
        determinarGanador(); // Determina el ganador
        iniciarNuevaMano(); // Prepara una nueva mano
    }

    public void iniciarRondaCartasNuevas() {
        for (Participacion participacion : mesa.getParticipaciones()) {
            if (participacion.getEstado() == Participacion.Estado.PAGA) {
                mostrarCartasParaJugador(participacion);
            }
        }
    }

    public void verificarFinDeTurno(int turnoActual) {
        Participacion participacionActual = mesa.getParticipaciones().get(turnoActual);
        actualizarTurno(participacionActual); // Notifica a las vistas quién está jugando

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
            participacion.setCartas(mazo.repartirMano(5)); // Reparte nuevas cartas
            actualizarVistaJugador(participacion); // Actualizar la vista del jugador
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
            vistaControlMesa.mostrarMensaje("Ganador: " + ganador.getUnJugador().getNombreCompleto()
                    + " | Figura: " + ganador.getFigura());
        }
    }

    public void mostrarCartasParaJugador(Participacion participacion) {
        int index = mesa.getParticipaciones().indexOf(participacion);

        if (index >= 0) {
            DialogPanelCartas vista;

            if (index < vistas.size() && vistas.get(index) != null) {
                vista = vistas.get(index);
            } else {
                vista = new DialogPanelCartas(null, participacion); // Pasar también la Participacion
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
        // Busca la vista correspondiente a la participación
        DialogPanelCartas vista = vistas.stream()
                .filter(v -> v != null && v.getParticipacion() == participacion)
                .findFirst()
                .orElse(null);

        if (vista != null) {
            vista.dispose(); // Cierra la ventana
            vistas.remove(vista); // Elimina la vista de la lista
        }
    }

    private void actualizarEstadoMano(EstadoMano estado) {
        mesa.getManoActual().setEstado(estado);

        for (Participacion participacion : mesa.getParticipaciones()) {
            actualizarVistaJugador(participacion);
        }
    }

    private void mostrarError(String mensaje) {
        vistaControlMesa.mostrarError(mensaje);
    }

    private void verificarEstadoMesa() {
        if (mesa.getParticipaciones().isEmpty()) {
            juegoActivo = false; // Finaliza el juego si no hay jugadores
            JOptionPane.showMessageDialog(null, "La mesa se ha quedado sin jugadores.");
        } else {
            // Actualiza a los jugadores restantes
            mesa.getParticipaciones().forEach(participacion -> {
                DialogPanelCartas vista = vistas.get(mesa.getParticipaciones().indexOf(participacion));
                if (vista != null) {
                    vista.setTitle("Jugador: " + participacion.getUnJugador().getNombreCompleto()
                            + " | Saldo: $" + participacion.getUnJugador().getSaldo());
                }
            });
        }
    }

    private void configurarVistaJugador(DialogPanelCartas dialog, Participacion participacion) {
        Jugador jugador = participacion.getUnJugador();
        System.out.println("confvistajugagador");
        // Configura el título con el nombre y saldo del jugador
        dialog.setTitle("Jugador: " + jugador.getNombreCompleto() + " | Saldo: $" + jugador.getSaldo());

        // Configura los botones
        dialog.getBtnPasar().addActionListener(e -> manejarJugadorPasa(participacion));


        System.out.println("Botón Pasar clickeado para: " + participacion.getUnJugador().getNombreCompleto());
        dialog.getBtnApostar().addActionListener(e -> {
            System.out.println("Botón apostar clickeado para: " + participacion.getUnJugador().getNombreCompleto());
            double monto = obtenerMontoApuesta(); // Solicita el monto de la apuesta
            manejarApostar(participacion, monto);
        });
        dialog.getBtnPagar().addActionListener(e -> {
            System.out.println("Botón Pasar pagar para: " + participacion.getUnJugador().getNombreCompleto());
            double monto = obtenerMontoApuesta(); // Solicita el monto a pagar
            manejarPagar(participacion, monto);
        });
        dialog.getBtnRetirarse().addActionListener(e -> manejarSalir(participacion));
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
            Participacion siguiente = mesa.getParticipaciones().stream()
                    .filter(p -> p.getEstado() == Participacion.Estado.ESPERANDO)
                    .findFirst()
                    .orElse(null);

            if (siguiente != null) {
                actualizarTurno(siguiente);
            }
        }
    }

    public void mostrarCartasParaTodos() {
        vistas.forEach(vista -> {
            vista.setLocationRelativeTo(null); // Asegúrate de posicionar la ventana
            vista.setVisible(true); // Muestra la vista
        });
    }

    private void actualizarTurno(Participacion participacionActual) {
        mesa.getParticipaciones().forEach(participacion -> {
            if (participacion == participacionActual) {
                participacion.setEstado(Participacion.Estado.APUESTA); // Marca como jugando
            } else {
                participacion.setEstado(Participacion.Estado.ESPERANDO); // Marca como esperando
            }
        });
    }

    private void actualizarVistaJugador(Participacion participacion) {
        int numeroMesa = mesa.getNumeroMesa();
        int numeroMano = mesa.getNumeroManoActual();
        String figuraAlta = participacion.getFigura() != null ? participacion.getFigura().getTipoFigura().toString() : "Sin figura";
        double pozo = mesa.getPozo(); // Obtiene el valor actualizado del pozo

        List<String> jugadores = new ArrayList<>();
        for (Participacion p : mesa.getParticipaciones()) {
            String estado = p.getEstado() != null ? p.getEstado().name() : "Desconocido";
            jugadores.add(p.getUnJugador().getNombreCompleto() + " (" + estado + ")");
        }

        List<String> figurasDisponibles = List.of("Póker", "Full House", "Color", "Escalera", "Trío", "Doble Par", "Par", "Sin Figura");

        DialogPanelCartas vista = vistas.stream()
                .filter(v -> v != null && v.getParticipacion() == participacion)
                .findFirst()
                .orElse(null);

        if (vista != null) {
            vista.actualizarDatosVista(numeroMesa, numeroMano, figuraAlta, pozo, jugadores, figurasDisponibles);
        }
    }

    public void manejarJugadorPasa(Participacion participacion) {
        if (!mesa.getParticipaciones().contains(participacion)) {
            mostrarError("No puedes pasar, ya no estás en la mesa.");
            return;
        }

        mesa.registrarJugadorPasa(participacion); // Actualiza el estado en la mesa
        actualizarVistaJugador(participacion); // Actualiza la vista
    }

    public void manejarFinDeMano() {
        mesa.finalizarMano(); // Llama al método en Mesa
        actualizarVistas(); // Actualiza todas las vistas
    }

    private void actualizarVistas() {
        mesa.getParticipaciones().forEach(this::actualizarVistaJugador);
    }
}
