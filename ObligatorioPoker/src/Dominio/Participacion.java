package Dominio;

import Observador.Observable;

/**
 * Clase que representa la participación de un jugador en una mesa de póker.
 * Extiende Observable para notificar a los observadores sobre cambios importantes.
 */
public class Participacion extends Observable {
    private Jugador unJugador;            // Jugador asociado a esta participación
    private Mesa unaMesa;                 // Mesa en la que participa
    private Apuesta unaApuesta;           // Apuesta actual del jugador en la mano
    private Mano unaMano;                 // Mano actual del jugador
    private Estado estado;                // Estado de la participación en la mano
    private double totalApostado;         // Total de apuestas realizadas por el jugador
    private double totalGanado;           // Total ganado en la mesa
    private double saldoInicial;          // Saldo del jugador al inicio de la participación

    // Estados posibles para una participación
    public enum Estado {
        APUESTA, PASA, PAGA, NO_PAGA, ESPERANDO, JUGAR_DE_NUEVO, ESPERANDO_RONDA
    }

    // Eventos posibles para notificar a los observadores
    public enum Eventos {
        APUESTA
    }

    // Constructor que inicializa una participación con un jugador y una mesa
    public Participacion(Jugador jugador, Mesa mesa, double saldoInicial) {
        this.unJugador = jugador;
        this.unaMesa = mesa;
        this.saldoInicial = saldoInicial;
        this.estado = Estado.ESPERANDO;
        this.totalApostado = 0;
        this.totalGanado = 0;
        
    }

    // Constructor vacío para otros usos o pruebas
    public Participacion() {
    }

    // Getters y Setters
    public double getTotalGanado() {
        return totalGanado;
    }

    public void setTotalGanado(double totalGanado) {
        this.totalGanado = totalGanado;
    }

    public double getTotalApostado() {
        return totalApostado;
    }

    public void setTotalApostado(double totalApostado) {
        this.totalApostado = totalApostado;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado state) {
        this.estado = state;
        avisar(Eventos.APUESTA); // Notifica a los observadores cuando se cambia el estado
    }

    public Jugador getUnJugador() {
        return unJugador;
    }

    public void setUnJugador(Jugador unJugador) {
        this.unJugador = unJugador;
    }

    public Mesa getUnaMesa() {
        return unaMesa;
    }

    public void setUnaMesa(Mesa unaMesa) {
        this.unaMesa = unaMesa;
    }

    public Apuesta getUnaApuesta() {
        return unaApuesta;
    }

    public void setUnaApuesta(Apuesta unaApuesta) {
        this.unaApuesta = unaApuesta;
    }

    public Mano getUnaMano() {
        return unaMano;
    }

    public void setUnaMano(Mano unaMano) {
        this.unaMano = unaMano;
    }

    public double getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(double saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    // Método para realizar una apuesta
    public boolean apostar(double monto) {
        if (saldoInicial >= monto) {
            saldoInicial -= monto; // Descontamos el monto del saldo inicial
            totalApostado += monto;
            setEstado(Estado.APUESTA); // Cambiar el estado a APUESTA
            avisar(Eventos.APUESTA); // Notificar sobre la apuesta
            return true;
        } else {
            setEstado(Estado.NO_PAGA);
            return false; // Saldo insuficiente para la apuesta
        }
    }

    // Método para gestionar cuando el jugador gana la mano
    public void esGanador() {
        double pozo = unaMesa.getPozo();
        saldoInicial += pozo; // Se aumenta el saldo del jugador
        totalGanado += pozo;
        avisar("Ganador de la mano"); // Notificar a los observadores
    }

    // Método para gestionar cuando el jugador no gana la mano
    public void noEsGanador() {
        totalGanado -= unaMesa.getMontoApostado();
        setEstado(Estado.NO_PAGA);
    }

    // Verifica si el jugador está esperando la ronda actual
    public boolean esperandoRonda() {
        return estado.equals(Estado.ESPERANDO_RONDA);
    }
    
    
    
}
