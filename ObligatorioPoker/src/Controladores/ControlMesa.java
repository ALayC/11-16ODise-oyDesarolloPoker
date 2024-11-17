package Controladores;

import Dominio.Jugador;
import Dominio.Mesa;
import Observador.Observable;
import Observador.Observador;
import Servicios.Fachada;
import Interfaces.VistaControlMesa;
import Dominio.Participacion;
import java.util.List;

public class ControlMesa implements Observador {

    private Fachada fachada;
    private VistaControlMesa vistaControlMesa;
    private JuegoPokerControlador juegoPokerControlador;

    public ControlMesa(VistaControlMesa vistaControlMesa) {
        this.fachada = Fachada.getInstancia();
        this.vistaControlMesa = vistaControlMesa;
        fachada.getServicioMesas().agregarObservador(this); // Observa cambios en las mesas
    }

    public void crearMesa(int cantidadJugadores, double apuestaBase, double porcentajeComision) {
        Mesa nuevaMesa = new Mesa(cantidadJugadores, apuestaBase, porcentajeComision);
        fachada.agregarMesa(nuevaMesa); // Notifica a los observadores registrados
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

                if (mesa.getCantidadActualJugadores() == mesa.getCantidadJugadores()) {
                    iniciarJuego(mesa); // Inicia el juego cuando la mesa está completa
                }
            } else {
                vistaControlMesa.mostrarError("No se pudo ingresar al jugador. La mesa puede estar llena.");
            }
        } else {
            vistaControlMesa.mostrarError("Saldo insuficiente para ingresar a la mesa.");
        }
    }

    public void iniciarJuego(Mesa mesa) {
        if (mesa.estaListaParaIniciar()) {
            juegoPokerControlador = new JuegoPokerControlador(mesa, vistaControlMesa);
            juegoPokerControlador.iniciarJuego();
        } else {
            vistaControlMesa.mostrarError("La mesa no está lista para iniciar el juego.");
        }
    }

    public void mostrarCartasParaJugadores(Mesa mesa) {
        if (juegoPokerControlador != null) {
            for (Participacion participacion : mesa.getParticipaciones()) {
                juegoPokerControlador.mostrarCartasParaJugador(participacion);
            }
        } else {
            vistaControlMesa.mostrarError("No hay un controlador de juego inicializado para esta mesa.");
        }
    }

    public void finalizarMano(Mesa mesa) {
        if (juegoPokerControlador != null) {
            juegoPokerControlador.finalizarMano();
        } else {
            vistaControlMesa.mostrarError("No hay un controlador de juego inicializado para esta mesa.");
        }
    }

    public void iniciarRondaCartasNuevas(Mesa mesa) {
        if (juegoPokerControlador != null) {
            juegoPokerControlador.iniciarRondaCartasNuevas();
        } else {
            vistaControlMesa.mostrarError("No hay un controlador de juego inicializado para esta mesa.");
        }
    }

    @Override
    public void actualizar(Observable origen, Object evento) {
        if (evento instanceof Mesa) {
            vistaControlMesa.cargarMesas(fachada.getMesas());
        }
    }
}
