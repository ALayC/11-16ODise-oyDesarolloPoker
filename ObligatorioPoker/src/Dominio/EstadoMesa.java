/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Dominio;

/**
 *
 * @author Santiago
 */
public interface EstadoMesa {
    void ingresarJugador(Mesa mesa, Jugador jugador);
    void iniciarJuego(Mesa mesa);
    void finalizarJuego(Mesa mesa);
    public String toString();
    public void abrirMesa(Mesa aThis);
    
    
  
}
