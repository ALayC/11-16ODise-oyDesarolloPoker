/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios;

import Dominio.Administrador;
import Dominio.Jugador;
import Dominio.Usuario;
import Dominio.UsuarioConectado;
import DominioExceptions.UsuarioConectadoException;
import DominioExceptions.UsuarioException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Santiago
 */
public class ServicioUsuarios {
    
    private HashMap<String, Usuario> usuarios = new HashMap<>();
    private ArrayList<UsuarioConectado> usuariosConectados = new ArrayList<>();
    
    public Administrador loginAdministrador(String cedula, String password) throws UsuarioException, UsuarioConectadoException {
        return (Administrador) login(cedula, password);
    }

    public Jugador loginJugador(String cedula, String password) throws UsuarioException, UsuarioConectadoException {
        return (Jugador) login(cedula, password);
    }

    private Usuario login(String cedula, String password) throws UsuarioException, UsuarioConectadoException {
        Usuario usuario = usuarios.get(cedula);

        // Verificar si el usuario existe y si la contraseña es válida
        if (usuario == null || !usuario.esPasswordValido(password)) {
            throw new UsuarioException("Credenciales incorrectas.");
        }

        // Verificar si el usuario ya está en la lista de usuarios conectados
        boolean yaConectado = usuariosConectados.stream()
                .anyMatch(u -> u.getUsuario().getCedula().equals(cedula));
        
        // Si ya está conectado, lanzamos una excepción
        if (yaConectado) {
            throw new UsuarioConectadoException("Acceso denegado. El usuario ya está logueado.");
        }
        
        // Añadir el usuario a la lista de conectados si no está ya conectado
        UsuarioConectado usuarioConectado = new UsuarioConectado(usuario);
        usuariosConectados.add(usuarioConectado);
        
        return usuario;
    }
    
    
    public void agregar(Usuario usuario) {
        usuarios.put(usuario.getCedula(), usuario);
    }

    void quitar(Usuario usuario) {
         usuariosConectados.removeIf(u -> u.getUsuario().equals(usuario));
    }
        ArrayList<UsuarioConectado> getUsuariosConectados() {
        return usuariosConectados;
    }
    
}
