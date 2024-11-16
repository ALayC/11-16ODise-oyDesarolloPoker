/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interfaces;

import Dominio.Mesa;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Santiago
 */
public interface AdministrarMesasVista {
    
    void cargarMesas(ArrayList<Mesa> mesas);

    public void mostrarError(String saldo_insuficiente_para_ingresar_a_la_mes);

    public void mostrarMensaje(String la_mesa_ha_sido_iniciada);

    public void mostrarInformacionMano(Mesa mesa);
    
   
}
