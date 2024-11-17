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
        System.out.println( "comienza juego");
        for (Participacion participacion : mesa.getParticipaciones()) {
            // Instancia de la vista con la participación
            DialogPanelCartas dialog = new DialogPanelCartas(null, participacion); 

            // Configuración de botones y acciones
            configurarVistaJugador(dialog, participacion);

            // Agrega la vista configurada a la lista de vistas
            vistas.add(dialog); 
        }

        juegoActivo = true;
        actualizarEstadoMano(EstadoMano.ESPERANDO_APUESTA);

        // Mostrar todas las vistas al mismo tiempo
        mostrarCartasParaTodos();
    }


        public void manejarPasar(Participacion participacion) {
            participacion.pasar();
            verificarEstadoMano(); // Lógica para avanzar el turno
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
            verificarEstadoMesa(); // Verifica si la mesa queda vacía
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

    private void configurarVistaJugador(DialogPanelCartas dialog, Participacion participacion) {
        Jugador jugador = participacion.getUnJugador();
        System.out.println("confvistajugagador");
        // Configura el título con el nombre y saldo del jugador
        dialog.setTitle("Jugador: " + jugador.getNombreCompleto() + " | Saldo: $" + jugador.getSaldo());

        // Configura los botones
        dialog.getBtnPasar().addActionListener(e -> {
            try {
                System.out.println("Botón Pasar clickeado para: " + participacion.getUnJugador().getNombreCompleto());
                manejarPasar(participacion);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        System.out.println("Listener para botón Pasar asociado");
        
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

}
