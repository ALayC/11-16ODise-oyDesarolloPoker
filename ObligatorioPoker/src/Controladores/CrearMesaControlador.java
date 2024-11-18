/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import Dominio.EstadoAbierta;
import Dominio.EventosMesa;
import Dominio.Mesa;
import Dominio.Usuario;
import Dominio.UsuarioConectado;
import Observador.Observable;
import Observador.Observador;
import Servicios.Fachada;

/**
 *
 * @author Santiago
 */
public class CrearMesaControlador implements Observador {
    
    private Fachada fachada;
    private CrearMesaVista mesaVista;
    private Usuario usuario;

    public CrearMesaControlador(CrearMesaVista mesaVista) {
        this.mesaVista = mesaVista;
    }
    
    
    public void crearMesa(int cantidadJugadores, double apuestaBase, double porcentajeComision) {
        Mesa mesa = new Mesa();
        mesa.setCantidadJugadores(cantidadJugadores);
        mesa.setApuestaBase(apuestaBase);
        mesa.setPorcentajeComision(porcentajeComision);
        mesa.setEstado(new EstadoAbierta());
        fachada.getInstancia().agregarMesa(mesa);
    }
    
    public void logout() {
        Fachada.getInstancia().quitar(usuario);
        mesaVista.cerrar();
    }
    
    
    @Override
    public void actualizar(Observable origen, Object evento) {
        if (EventosMesa.MesaCreada.equals(evento)) {

        mesaVista.mostrarMensaje("La mesa ha sido creada con éxito.");
       
    }
    }
    
}
