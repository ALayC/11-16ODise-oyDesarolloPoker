/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package IU;

import Controladores.LoginAdministradorControlador;
import Controladores.LoginControlador;
import Dominio.Administrador;
import Dominio.Usuario;
import Servicios.Fachada;
import java.awt.Frame;

/**
 *
 * @author Santiago
 */
public class AdministradorLoginVistaDialog extends LoginVistaDialog {

    public AdministradorLoginVistaDialog() {
        setTitle("Login | Administrador");
    }

    @Override
    public void ejecutarSiguienteCU(Usuario usuario) {
        Administrador administrador = (Administrador) usuario;
        new VistaAdministrador(administrador).setVisible(true);
    }

    @Override
    protected LoginControlador crearControlador(LoginVistaDialog vista) {
        return new LoginAdministradorControlador(vista);
    }

}
