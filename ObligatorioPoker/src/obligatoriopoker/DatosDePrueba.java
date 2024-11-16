/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package obligatoriopoker;

import Dominio.Administrador;
import Dominio.Jugador;
import Servicios.Fachada;

/**
 *
 * @author Santiago
 */
public class DatosDePrueba {
    public static void iniciar() {   
    
    Fachada f = Fachada.getInstancia();
    
    Administrador admin1 = new Administrador("b");
    admin1.setCedula("2024");
    admin1.setNombreCompleto("Administrador dos");
    admin1.setPassword("b");
    f.agregar(admin1);
   
   
   
    Administrador admin2 = new Administrador("b");
    admin2.setCedula("51734298");
    admin2.setNombreCompleto("Administrador dos");
    admin2.setPassword("b");
    f.agregar(admin2);
   
    
    Jugador jugador1 = new Jugador("j1");
    jugador1.setCedula("1");
    jugador1.setNombreCompleto("Jugador Uno");
    jugador1.setPassword("1");
    jugador1.setSaldo(2000);
    
    Jugador jugador2 = new Jugador("j2");
    jugador2.setCedula("2");
    jugador2.setNombreCompleto("Jugador Dos");
    jugador2.setPassword("2");
        jugador2.setSaldo(2000);
        
    Jugador jugador3 = new Jugador("j3");
    jugador3.setCedula("3");
    jugador3.setNombreCompleto("Jugador Tres");
    jugador3.setPassword("3");
    jugador3.setSaldo(2000);
    
    Jugador jugador4 = new Jugador("j4");
    jugador4.setCedula("4");
    jugador4.setNombreCompleto("Jugador Cuatro");
    jugador4.setPassword("4");
    jugador4.setSaldo(2000);
    f.agregar(jugador1);
    f.agregar(jugador2);
    f.agregar(jugador3);
    f.agregar(jugador4);
     
    
}
}
