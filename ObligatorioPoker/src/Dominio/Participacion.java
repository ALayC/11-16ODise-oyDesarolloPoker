package Dominio;

import Observador.Observable;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa la participación de un jugador en una mesa de póker.
 * Extiende Observable para notificar a los observadores sobre cambios
 * importantes.
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
    private List<Carta> cartas = new ArrayList<>();
    private Figura figura;

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
        this.cartas = new ArrayList<>(cartas); // Inicializa las cartas
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
        avisar("EstadoActualizado"); // Notifica los cambios en el estado
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
        avisar("SaldoActualizado"); // Notifica los cambios en el saldo
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
    public void esGanador(double pozo) {
        saldoInicial += pozo;
        totalGanado += pozo;
        avisar("Ganador de la mano");
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

    public List<Carta> getCartas() {
        return cartas;
    }

    public void setCartas(List<Carta> nuevasCartas) {
        this.cartas = nuevasCartas;
        avisar("CartasActualizadas"); // Notifica a los observadores sobre las nuevas cartas
    }

    public void agregarCartas(List<Carta> nuevasCartas) {
        this.cartas.addAll(nuevasCartas);
        avisar("CartasActualizadas"); // Notifica los cambios en las cartas
    }

    public void actualizarSaldo(double nuevoSaldo) {
        this.saldoInicial = nuevoSaldo;
        avisar("SaldoActualizado"); // Notifica cambios en el saldo
    }

    public Figura getFigura() {
        return figura;
    }

    public void setFigura(Figura figura) {
        this.figura = figura;
    }

    public List<Carta> obtenerCartasDeParticipacion() {
        return new ArrayList<>(cartas); // Devuelve una copia de las cartas
    }

    public void pasar() {
        setEstado(Estado.PASA); // Cambiar el estado a PASA
        avisar("Jugador pasó el turno");
    }

    public boolean pagarApuesta(double monto) {
        if (saldoInicial >= monto) {
            saldoInicial -= monto;
            totalApostado += monto;
            setEstado(Estado.PAGA);
            return true;
        }
        return false;
    }

    public void calcularFigura() {
        System.out.println("Calculando figura para cartas: " + cartas);
        figura = CreadorDeFiguras.crearFigura(cartas);
    }

    public void reiniciarParaNuevaMano() {
        cartas.clear();
        figura = null;
        estado = Estado.ESPERANDO;
    }

    public boolean estaActivo() {
        return estado == Estado.APUESTA || estado == Estado.ESPERANDO || estado == Estado.PAGA;
    }

    public boolean descontarSaldo(double monto) {
        if (saldoInicial >= monto) {
            saldoInicial -= monto; // Descuenta el monto del saldo inicial
            avisar("SaldoActualizado"); // Notifica a los observadores sobre el cambio en el saldo
            return true; // Indica que el descuento fue exitoso
        } else {
            return false; // Indica que no hay saldo suficiente
        }
    }

}
