/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import IU.LoginVista;
import Dominio.Usuario;
import DominioExceptions.UsuarioConectadoException;
import DominioExceptions.UsuarioException;
import Servicios.Fachada;

/**
 *
 * @author Santiago
 */
public class LoginJugadorControlador extends LoginControlador {
    
    private Fachada fachada = Fachada.getInstancia();

    public LoginJugadorControlador(LoginVista vista) {
        super(vista);
    }

    @Override
    protected Usuario loginInterno(String cedula, String password) throws UsuarioException, UsuarioConectadoException {
        return fachada.loginJugador(cedula, password);
    }

    


}
