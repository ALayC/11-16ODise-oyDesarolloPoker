package Controladores;

import Dominio.EstadoAbierta;
import Dominio.EstadoIniciada;
import Dominio.Jugador;
import Dominio.Mesa;
import Dominio.Participacion;
import IU.DialogPanelCartas;
import Servicios.Fachada;
import java.util.List;
import Observador.Observador;
import Observador.Observable;
import Interfaces.VistaControlMesa;
import java.util.ArrayList;
import panelCartasPoker.CartaPoker;

public class ControlMesa implements Observador {

    private Fachada fachada;
    private VistaControlMesa vistaControlMesa;

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
        for (Participacion participacion : mesa.getParticipaciones()) {
            Jugador jugador = participacion.getUnJugador();


                List<CartaPoker> cartasDeJugador = new ArrayList<>(participacion.getUnaMano().getCartas());
                DialogPanelCartas dialogJuego = new DialogPanelCartas(null);
                dialogJuego.setTitle("Jugador: " + jugador.getNombreCompleto());
                dialogJuego.cargarCartas(new ArrayList<>(cartasDeJugador));
                dialogJuego.setLocationRelativeTo(null);
                dialogJuego.setVisible(true);
            

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

}
