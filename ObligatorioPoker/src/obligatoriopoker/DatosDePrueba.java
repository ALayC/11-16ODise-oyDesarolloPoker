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

        // Administradores
        Administrador admin1 = new Administrador("100");
        admin1.setCedula("100");
        admin1.setNombreCompleto("A 100");
        admin1.setPassword("100");
        f.agregar(admin1);

        Administrador admin2 = new Administrador("200");
        admin2.setCedula("200");
        admin2.setNombreCompleto("A 200");
        admin2.setPassword("101");
        f.agregar(admin2);

        // Jugadores
        Jugador jugador0 = new Jugador("0");
        jugador0.setCedula("0");
        jugador0.setNombreCompleto("J0");
        jugador0.setPassword("0");
        jugador0.setSaldo(0);
        f.agregar(jugador0);

        Jugador jugador1 = new Jugador("1");
        jugador1.setCedula("1");
        jugador1.setNombreCompleto("J1");
        jugador1.setPassword("1");
        jugador1.setSaldo(1000);
        f.agregar(jugador1);

        Jugador jugador2 = new Jugador("2");
        jugador2.setCedula("2");
        jugador2.setNombreCompleto("J2");
        jugador2.setPassword("2");
        jugador2.setSaldo(2000);
        f.agregar(jugador2);

        Jugador jugador3 = new Jugador("3");
        jugador3.setCedula("3");
        jugador3.setNombreCompleto("J3");
        jugador3.setPassword("3");
        jugador3.setSaldo(3000);
        f.agregar(jugador3);

        Jugador jugador4 = new Jugador("4");
        jugador4.setCedula("4");
        jugador4.setNombreCompleto("J4");
        jugador4.setPassword("4");
        jugador4.setSaldo(4000);
        f.agregar(jugador4);

        Jugador jugador5 = new Jugador("5");
        jugador5.setCedula("5");
        jugador5.setNombreCompleto("J5");
        jugador5.setPassword("5");
        jugador5.setSaldo(5000);
        f.agregar(jugador5);

        Jugador jugador6 = new Jugador("6");
        jugador6.setCedula("6");
        jugador6.setNombreCompleto("J6");
        jugador6.setPassword("6");
        jugador6.setSaldo(6000);
        f.agregar(jugador6);

        Jugador jugador7 = new Jugador("7");
        jugador7.setCedula("7");
        jugador7.setNombreCompleto("J7");
        jugador7.setPassword("7");
        jugador7.setSaldo(7000);
        f.agregar(jugador7);

        Jugador jugador8 = new Jugador("8");
        jugador8.setCedula("8");
        jugador8.setNombreCompleto("J8");
        jugador8.setPassword("8");
        jugador8.setSaldo(8000);
        f.agregar(jugador8);

        Jugador jugador9 = new Jugador("9");
        jugador9.setCedula("9");
        jugador9.setNombreCompleto("J9");
        jugador9.setPassword("9");
        jugador9.setSaldo(9000);
        f.agregar(jugador9);
    }
}

