/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import Dominio.EstadoIniciada;
import Dominio.EventosMesa;
import Dominio.Jugador;
import Dominio.Mesa;
import Dominio.Participacion;
import IU.DialogPanelCartas;
import Interfaces.AdministrarMesasVista;
import Interfaces.VistaControlMesa;
import Observador.Observable;
import Observador.Observador;
import Servicios.Fachada;
import java.util.ArrayList;
import java.util.List;
import panelCartasPoker.CartaPoker;

/**
 *
 * @author Santiago
 */
public class AdministrarMesasControlador implements Observador{
    private Fachada fachada;
    private AdministrarMesasVista mesaVista;
    private Mesa mesa;
    
        public AdministrarMesasControlador(AdministrarMesasVista vista) {
            this.fachada = Fachada.getInstancia();
            this.mesaVista = vista; 
            inicializar();
            obtenerMesas();
        }

    public ArrayList<Mesa> obtenerMesas() {
        return fachada.getMesas();
    }
    
    public void ingresarMesa(Jugador jugador, int numeroMesa) {
        Mesa mesa = fachada.getMesaPorNumero(numeroMesa);

        if (mesa == null) {
            mesaVista.mostrarError("La mesa seleccionada no existe.");
            return;
        }

        double saldoMinimo = mesa.getApuestaBase() * 10;

        if (jugador.getSaldo() >= saldoMinimo) {
            // Evitar duplicación de jugador en la misma mesa
            if (mesa.getParticipaciones().stream().anyMatch(p -> p.getUnJugador().equals(jugador))) {
                mesaVista.mostrarError("El jugador ya está en la mesa.");
                return;
            }

            if (mesa.agregarJugador(jugador)) {
                mesaVista.mostrarMensaje("Jugador ingresado a la mesa.");

                if (mesa.getCantidadActualJugadores() == mesa.getCantidadJugadores()) {
                    mesa.setEstado(new EstadoIniciada());
                    mesaVista.mostrarMensaje("La mesa ha sido iniciada.");
                     mostrarCartasParaJugadores(mesa);
                }

            } else {
                mesaVista.mostrarError("No se pudo ingresar al jugador. La mesa puede estar llena.");
            }
        } else {
            mesaVista.mostrarError("Saldo insuficiente para ingresar a la mesa.");
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
        if (EventosMesa.MesaCreada.equals(evento)) {
        // Obtiene las mesas actualizadas desde la fachada
        ArrayList<Mesa> mesasActualizadas = obtenerMesas();
        
        // Actualiza la vista con las mesas
        mesaVista.cargarMesas(mesasActualizadas);
    }
    }

    private void inicializar() {
         for (Mesa mesa : fachada.getMesas()) {
            mesa.agregarObservador(this);
        }
    }
}
