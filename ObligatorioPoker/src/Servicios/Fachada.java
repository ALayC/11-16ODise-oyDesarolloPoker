
package Servicios;

import Dominio.Administrador;
import Dominio.Jugador;
import Dominio.Mesa;
import Dominio.Usuario;
import Dominio.UsuarioConectado;
import DominioExceptions.UsuarioConectadoException;
import DominioExceptions.UsuarioException;
import java.util.ArrayList;


public class Fachada {
    private static Fachada instancia = new Fachada();
    private ServicioUsuarios servicioUsuarios = new ServicioUsuarios();
    private ServicioMesas servicioMesas = new ServicioMesas();

    public static Fachada getInstancia() {
        return instancia;
    }

    private Fachada() {

    }

    public Administrador loginAdministrador(String cedula, String password) throws UsuarioException, UsuarioConectadoException {
        return servicioUsuarios.loginAdministrador(cedula, password);
    }

    public Jugador loginJugador(String cedula, String password) throws UsuarioException, UsuarioConectadoException {
        return servicioUsuarios.loginJugador(cedula, password);
    }

    public void agregar(Usuario usuario) {
        servicioUsuarios.agregar(usuario);
    }
        public void quitar(Usuario usuario) {
        servicioUsuarios.quitar(usuario);
    }
    public ArrayList<UsuarioConectado> getUsuariosConectados() {
        return servicioUsuarios.getUsuariosConectados();
    }
        
    public UsuarioConectado obtenerUsuarioConectadoActual() {
        ArrayList<UsuarioConectado> usuariosConectados = servicioUsuarios.getUsuariosConectados();
        return usuariosConectados.isEmpty() ? null : usuariosConectados.get(usuariosConectados.size() - 1);
    }
     
         // Método para agregar una mesa nueva al sistema
    public void agregarMesa(Mesa mesa) {
       servicioMesas.agregarMesa(mesa);
    }

    // Método para obtener todas las mesas activas (opcional)
    public ArrayList<Mesa> getMesas() {
        return servicioMesas.getMesas();  // Devuelve una copia para evitar modificaciones directas
    }
    
    public ServicioMesas getServicioMesas() {
        return servicioMesas;
    }
        
    public Mesa getMesaPorNumero(int numeroMesa) {
        return servicioMesas.getMesaPorNumero(numeroMesa);
    }    
}
