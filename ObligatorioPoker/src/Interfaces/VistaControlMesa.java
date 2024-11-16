/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interfaces;

import Dominio.Mesa;
import java.util.List;

/**
 *
 * @author alay
 */
public interface VistaControlMesa {
    void mostrarMensaje(String mensaje);
    
    void mostrarError(String mensaje);
    
    void cargarMesas(List<Mesa> mesas);
    
    
}
