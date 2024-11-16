/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

/**
 *
 * @author Santiago
 */
public class EstadoIniciada implements EstadoMesa {

    @Override
    public void ingresarJugador(Mesa mesa, Jugador jugador) {
        throw new IllegalStateException("La mesa ya ha comenzado, no se pueden unir más jugadores.");
    }

    @Override
    public void iniciarJuego(Mesa mesa) {
        System.out.println("El juego ya está en curso.");
    }

    @Override
    public void finalizarJuego(Mesa mesa) {
        mesa.setEstado(new EstadoFinalizada());
        System.out.println("La mesa ha sido finalizada.");
    }

    @Override
    public void abrirMesa(Mesa aThis) {
        throw new IllegalStateException("No se puede abrir una mesa que ya está iniciada.");
    }

    public String toString() {
        return  "Iniciada";
    }

    
}
