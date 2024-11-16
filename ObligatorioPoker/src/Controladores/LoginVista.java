/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package IU;

import Dominio.Usuario;

/**
 *
 * @author Santiago
 */
public interface LoginVista {
       
       public abstract void ejecutarSiguienteCU(Usuario usuario);
       
       public abstract void cerrar();
       
       public abstract void mostrarMensajeDeError(String mensaje);
       
       
}
