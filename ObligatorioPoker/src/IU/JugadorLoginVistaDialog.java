/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package IU;

import Controladores.LoginControlador;
import Controladores.LoginJugadorControlador;
import Dominio.Jugador;
import Dominio.Usuario;
import Servicios.Fachada;
import java.awt.Frame;

/**
 *
 * @author Santiago
 */
public class JugadorLoginVistaDialog extends LoginVistaDialog {
    
    public JugadorLoginVistaDialog() {
        setTitle("Login | Jugador");
    }

    @Override
    public void ejecutarSiguienteCU(Usuario usuario) {
        Jugador jugador = (Jugador) usuario;
        new VistaJugador(jugador).setVisible(true);
    }

   
    @Override
    protected LoginControlador crearControlador(LoginVistaDialog vista) {
       return new LoginJugadorControlador(vista);
    }


    
}
