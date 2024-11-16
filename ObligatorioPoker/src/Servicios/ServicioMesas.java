package Servicios;

import Dominio.Jugador;
import Dominio.Mesa;
import Observador.Observable;
import java.util.ArrayList;

public class ServicioMesas extends Observable {
    private int numeroMesaActual = 1;
    private ArrayList<Mesa> mesas = new ArrayList<>();

    public void agregarMesa(Mesa mesa) {
        mesas.add(mesa);
        mesa.setNumeroMesa(numeroMesaActual++); 
        avisar(mesa);  // Notificar a los observadores cuando se agrega una mesa
        
    }

    public ArrayList<Mesa> getMesas() {
        return new ArrayList<>(mesas);  // Devuelve una copia para evitar modificaciones directas
    }
    
    public Mesa getMesaPorNumero(int numeroMesa) {
        for (Mesa mesa : mesas) {
            if (mesa.getNumeroMesa() == numeroMesa) {
                return mesa;
            }
        }
        return null;  // Devuelve null si no encuentra la mesa
    }
    
    public boolean agregarJugadorAMesa(int numeroMesa, Jugador jugador) {
        Mesa mesa = getMesaPorNumero(numeroMesa);
        if (mesa != null && mesa.agregarJugador(jugador)) {
            avisar(mesa);  // Notificar a los observadores
            return true;
        }
        return false;
    }
}
