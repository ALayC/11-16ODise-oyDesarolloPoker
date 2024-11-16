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
            // Evitar duplicación de jugador en la misma mesa
            if (mesa.getParticipaciones().stream().anyMatch(p -> p.getUnJugador().equals(jugador))) {
                vistaControlMesa.mostrarError("El jugador ya está en la mesa.");
                return;
            }

            if (mesa.agregarJugador(jugador)) {
                vistaControlMesa.mostrarMensaje("Jugador ingresado a la mesa.");

                if (mesa.getCantidadActualJugadores() == mesa.getCantidadJugadores()) {
                    mesa.setEstado(new EstadoIniciada());
                    vistaControlMesa.mostrarMensaje("La mesa ha sido iniciada.");
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
            datos[i] = new Object[] {
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

            // Crear el diálogo para el jugador actual
            DialogPanelCartas dialog = new DialogPanelCartas(null);
            dialog.setTitle("Jugador: " + jugador.getNombreCompleto());

            // Cargar las cartas en el panel de cartas
            dialog.cargarCartas(new ArrayList<>(cartasDeJugador));

            // Mostrar el diálogo
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
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
