package Controladores;

import Dominio.Carta;
import Dominio.EstadoAbierta;
import Dominio.EstadoIniciada;
import Dominio.Jugador;
import Dominio.Mano;
import Dominio.Mesa;
import Dominio.Participacion;
import IU.DialogPanelCartas;
import Servicios.Fachada;
import java.util.List;
import Observador.Observador;
import Observador.Observable;
import Interfaces.VistaControlMesa;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import panelCartasPoker.CartaPoker;

public class ControlMesa implements Observador {

    private Fachada fachada;
    private VistaControlMesa vistaControlMesa;
    private JuegoPokerControlador juegoPokerControlador; // Controlador del caso de uso 'Jugar al póker'
    public ControlMesa(VistaControlMesa vistaControlMesa) {
        this.fachada = Fachada.getInstancia();
        this.vistaControlMesa = vistaControlMesa;
        fachada.getServicioMesas().agregarObservador(this); // Registra `ControlMesa` como observador de `ServicioMesas`
    }

    public void crearMesa(int cantidadJugadores, double apuestaBase, double porcentajeComision) {
        Mesa nuevaMesa = new Mesa(cantidadJugadores, apuestaBase, porcentajeComision);
        fachada.agregarMesa(nuevaMesa);  // Esto debería notificar a los observadores registrados en ServicioMesas

    }

    public List<Mesa> obtenerMesas() {
        return fachada.getMesas();
    }

    public void ingresarMesa(Jugador jugador, int numeroMesa) {
        Mesa mesa = fachada.getMesaPorNumero(numeroMesa);

        if (mesa == null) {
            vistaControlMesa.mostrarError("La mesa seleccionada no existe.");
            return;
        }

        double saldoMinimo = mesa.getApuestaBase() * 10;

        if (jugador.getSaldo() >= saldoMinimo) {
            if (fachada.getServicioMesas().agregarJugadorAMesa(numeroMesa, jugador)) {
                vistaControlMesa.mostrarMensaje("Jugador ingresado a la mesa.");
                if (mesa.getEstado().toString().equals("Abierta")) {
                    mostrarCartasParaJugadoresEsperando(mesa);
                }
                // Verificar si la mesa está llena
                if (mesa.getCantidadActualJugadores() == mesa.getCantidadJugadores()) {
                    // Cambiar el estado de la mesa
                    mesa.setEstado(new EstadoIniciada());
                    vistaControlMesa.mostrarMensaje("La mesa ha sido iniciada.");

                    // Repartir cartas y mostrar
                    mostrarCartasParaJugadores(mesa);
                }
            } else {
                vistaControlMesa.mostrarError("No se pudo ingresar al jugador. La mesa puede estar llena.");
            }
        } else {
            vistaControlMesa.mostrarError("Saldo insuficiente para ingresar a la mesa.");
        }
    }

    public Object[][] obtenerDatosMesas() {
        List<Mesa> mesas = fachada.getMesas();
        Object[][] datos = new Object[mesas.size()][5];

        for (int i = 0; i < mesas.size(); i++) {
            Mesa mesa = mesas.get(i);
            datos[i] = new Object[]{
                mesa.getNumeroMesa(),
                mesa.getCantidadJugadores(),
                mesa.getApuestaBase(),
                mesa.getCantidadActualJugadores(),
                mesa.getPorcentajeComision()
            };
        }
        return datos;
    }

    public Mesa getMesaPorNumero(int numeroMesa) {
        return fachada.getMesaPorNumero(numeroMesa);
    }

    public void mostrarCartasParaJugadores(Mesa mesa) {
        List<Participacion> participaciones = mesa.getParticipaciones();

        for (int i = 0; i < participaciones.size(); i++) {
            Participacion participacion = participaciones.get(i);
            Jugador jugador = participacion.getUnJugador();

            // Crear el diálogo para el jugador actual
            DialogPanelCartas dialogJuego = new DialogPanelCartas(null);
            dialogJuego.setTitle("Jugador: " + jugador.getNombreCompleto());

            // Cargar las cartas del jugador en el panel
            dialogJuego.cargarCartas(new ArrayList<>(participacion.obtenerCartasDeParticipacion()));

            // Configurar el comportamiento de los botones según el turno
            configurarAccionesDialog(dialogJuego, mesa, participacion, i);

            // Mostrar el diálogo
            dialogJuego.setLocationRelativeTo(null);
            dialogJuego.setVisible(true);

            // Esperar la acción del jugador antes de pasar al siguiente
        }
    }


    public void mostrarCartasParaJugadoresEsperando(Mesa mesa) {   
            DialogPanelCartas dialogEspera = new DialogPanelCartas(null);
            
            if (mesa.getEstado().toString().equals("Abierta")) {
                dialogEspera.setTitle("Esperando inicio del juego, hay " + mesa.getCantidadActualJugadores() +" jugadores de " + mesa.getCantidadJugadores() + " en la mesa");
                // Mostrar el diálogo
                dialogEspera.setLocationRelativeTo(null);
                dialogEspera.setVisible(true);
            }       
    }

    @Override
    public void actualizar(Observable origen, Object evento) {
        if (evento instanceof Mesa) {
            // Llama a la vista para recargar la lista de mesas
            vistaControlMesa.cargarMesas(fachada.getMesas());
        }
    }
    
    public void iniciarJuego(Mesa mesa) {
        if (mesa.getEstado() instanceof EstadoIniciada) {
            // Inicializa el controlador para manejar el juego en esta mesa
            juegoPokerControlador = new JuegoPokerControlador(mesa);
            juegoPokerControlador.iniciarJuego(); // Maneja el inicio del juego y las vistas
        } else {
            vistaControlMesa.mostrarError("La mesa no está lista para iniciar el juego.");
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
            double monto = obtenerMontoApuesta(); // Solicita al jugador el monto de la apuesta
            if (participacion.pagarApuesta(monto)) { // Registrar el pago de la apuesta
                dialog.dispose(); // Cierra la ventana del jugador actual
                verificarFinDeTurno(mesa, turnoActual); // Verificar si el turno terminó
            } else {
                JOptionPane.showMessageDialog(null, "Saldo insuficiente para pagar la apuesta.", "Error", JOptionPane.ERROR_MESSAGE);
            }
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
                iniciarRondaCartasNuevas(mesa);
            }
        } else {
            // Advance to the next player
            int siguienteTurno = (turnoActual + 1) % mesa.getParticipaciones().size();
            if (juegoPokerControlador != null) {
                juegoPokerControlador.mostrarCartasParaJugador(mesa.getParticipaciones().get(siguienteTurno));
            } else {
                vistaControlMesa.mostrarError("No hay un controlador de juego inicializado para esta mesa.");
            }
        }
    }



    private void finalizarMano(Mesa mesa) {
        // Determinar ganador y distribuir el pozo
        Participacion ganador = mesa.getManos().get(mesa.getNumeroManoActual() - 1).determinarGanador();
        if (ganador != null) {
            ganador.esGanador();
        }

        // Reiniciar el estado de la mesa para la próxima mano
        mesa.agregarMano(new Mano(mesa.getNumeroManoActual() + 1));
        vistaControlMesa.mostrarMensaje("La mano ha terminado. Preparando la próxima mano.");
    }


    private double obtenerMontoApuesta() {
        while (true) {
            try {
                String input = JOptionPane.showInputDialog(null, "Ingrese el monto de su apuesta:");
                if (input == null) { // Si el usuario cancela, devuelve 0
                    return 0;
                }
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese un monto válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
        
    private void iniciarRondaCartasNuevas(Mesa mesa) {
        vistaControlMesa.mostrarMensaje("Pidiendo cartas nuevas a los jugadores.");
        if (juegoPokerControlador != null) {
            juegoPokerControlador.iniciarRondaCartasNuevas();
        } else {
            vistaControlMesa.mostrarError("No hay un controlador de juego inicializado para esta mesa.");
        }
    }




}
