/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

/**
 *
 * @author Santiago
 */
public class EstadoFinalizada implements EstadoMesa {

    @Override
    public void ingresarJugador(Mesa mesa, Jugador jugador) {
         throw new IllegalStateException("La mesa está finalizada y no permite nuevos jugadores.");
    }

    @Override
    public void iniciarJuego(Mesa mesa) {
        throw new IllegalStateException("La mesa está finalizada y no puede reiniciarse.");
    }

    @Override
    public void finalizarJuego(Mesa mesa) {
       System.out.println("La mesa ya está finalizada.");
    }

    @Override
    public void abrirMesa(Mesa aThis) {
        System.out.println("La mesa ya está finalizada. No se puede abrir");
    }
    
    public String toString() {
        return "Finalizada";
    }
}
