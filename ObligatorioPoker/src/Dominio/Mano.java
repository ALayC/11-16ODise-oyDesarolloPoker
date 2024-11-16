package Dominio;

import java.util.ArrayList;

public class Mano {

    private int numero;                   // Número de la mano
    private ArrayList<Carta> cartas;      // Lista de cartas en la mano
    private Figura unaFigura;             // Figura obtenida en la mano
    private double totalApostado;         // Total apostado en esta mano
    private String estado;                // Estado de la mano (ej., "En Juego", "Finalizada")
    private Jugador ganador;              // Ganador de la mano

    public Mano(int numero) {
        this.numero = numero;
        this.cartas = new ArrayList<>();
        this.estado = "En Juego";  // Estado inicial de la mano
    }

    // Getter para el número de la mano
    public int getNumero() {
        return numero;
    }

    // Getter y setter para las cartas de la mano
    public ArrayList<Carta> getCartas() {
        return cartas;
    }

    public void setCartas(ArrayList<Carta> cartas) {
        this.cartas = cartas;
    }

    // Getter y setter para la figura obtenida en la mano
    public Figura getUnaFigura() {
        return unaFigura;
    }

    public void setUnaFigura(Figura unaFigura) {
        this.unaFigura = unaFigura;
    }

    // Getter para el total apostado en la mano
    public double getTotalApostado() {
        return totalApostado;
    }

    // Método para agregar una apuesta a esta mano
    public void agregarApuesta(double monto) {
        this.totalApostado += monto;
    }

    // Getter y setter para el estado de la mano
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Getter y setter para el ganador de la mano
    public Jugador getGanador() {
        return ganador;
    }

    public void setGanador(Jugador ganador) {
        this.ganador = ganador;
    }

    // Getter para el nombre de la figura ganadora
   /* public String getFiguraGanadora() {
        return (unaFigura != null) ? unaFigura.getNombre() : "N/A";
    }*/

    // Método para obtener la cantidad de jugadores en esta mano (en este ejemplo, igual a la cantidad de cartas)
    public int getCantidadJugadores() {
        return cartas.size();
  }

     




}
