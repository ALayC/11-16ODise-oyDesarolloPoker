/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

/**
 *
 * @author Santiago
 */
public class EstadoAbierta implements EstadoMesa {

    @Override
    public void ingresarJugador(Mesa mesa, Jugador jugador) {
        System.out.println("Jugador ingresado en la mesa abierta.");
    }

    @Override
    public void iniciarJuego(Mesa mesa) {
        if (mesa.getCantidadActualJugadores() >= mesa.getCantidadJugadores()) {
            mesa.setEstado(new EstadoIniciada());
            System.out.println("La mesa ha sido iniciada.");
        } else {
            throw new IllegalStateException("No hay suficientes jugadores para iniciar el juego.");
        }
    }

    @Override
    public void finalizarJuego(Mesa mesa) {
         throw new IllegalStateException("No se puede finalizar una mesa que aún no ha iniciado.");
    }

    @Override
    public void abrirMesa(Mesa aThis) {
        throw new IllegalStateException("La mesa ya está abierta.");
    }
    
    public String toString() {
        return "Abierta";
    }


    
    
}
