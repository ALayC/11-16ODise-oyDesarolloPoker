/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import IU.LoginVista;
import Dominio.Administrador;
import Dominio.Jugador;
import Dominio.Usuario;
import DominioExceptions.UsuarioConectadoException;
import DominioExceptions.UsuarioException;
import IU.AdministradorLoginVistaDialog;
import IU.JugadorLoginVistaDialog;
import IU.LoginVistaDialog;
import IU.VistaAdministrador;
import IU.VistaJugador;
import Observador.Observable;
import Observador.Observador;
import Servicios.Fachada;

/**
 *
 * @author Santiago
 */
public abstract class LoginControlador {
    
     private LoginVista vista;
     private Fachada fachada;

    public LoginControlador(LoginVista vista) {
        this.vista = vista;
        fachada = Fachada.getInstancia();
    }
     
    protected abstract Usuario loginInterno(String cedula, String password) throws UsuarioException, UsuarioConectadoException;
      
     public void login(String cedula, String password) {
        try {
            Usuario usuario = loginInterno(cedula, password);
            if (usuario != null) {
                vista.ejecutarSiguienteCU(usuario);
                vista.cerrar();
            }
        } catch (UsuarioException e) {
            vista.mostrarMensajeDeError(e.getMessage());
        } catch (UsuarioConectadoException e) {
            vista.mostrarMensajeDeError(e.getMessage());
        }
    }
  
    
}
