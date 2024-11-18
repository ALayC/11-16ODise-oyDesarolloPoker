package Dominio;

import Observador.Observable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase que representa la participación de un jugador en una mesa Extiende
 * Observable para notificar a los observadores sobre cambios importantes.
 */
public class Participacion extends Observable {

    private Jugador unJugador;
    private Mesa unaMesa;
    private Apuesta unaApuesta;
    private Mano unaMano;
    private Estado estado;
    private double totalApostado;
    private double totalGanado;
    private double saldoInicial;
    private List<Carta> cartas = new ArrayList<>();
    private Figura figura;

    public enum Estado {
        APUESTA, PASA, PAGA, NO_PAGA, ESPERANDO, JUGAR_DE_NUEVO, ESPERANDO_RONDA, CAMBIO_DE_CARTAS
    }

    public enum Eventos {
        APUESTA
    }

    public Participacion(Jugador jugador, Mesa mesa, double saldoInicial) {
        this.unJugador = jugador;
        this.unaMesa = mesa;
        this.saldoInicial = saldoInicial;
        this.estado = Estado.ESPERANDO;
        this.totalApostado = 0;
        this.totalGanado = 0;
        this.cartas = new ArrayList<>(cartas);
    }

    public Participacion() {
    }

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
        avisar("EstadoActualizado");
    }

    public Jugador getUnJugador() {
        return unJugador;
    }

    public String getNombreDeJugador(Jugador jugador) {
        return jugador.getNombreCompleto();
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
        avisar("SaldoActualizado");
    }

    public boolean apostar(double monto) {
        if (saldoInicial >= monto) {
            saldoInicial -= monto;
            totalApostado += monto;
            setEstado(Estado.APUESTA);
            avisar(Eventos.APUESTA);
            return true;
        } else {
            setEstado(Estado.NO_PAGA);
            return false;
        }
    }

    public void esGanador(double pozo) {
        System.out.println("Saldo antes " + unJugador.getSaldo() + unJugador.getNombre());
        saldoInicial = unJugador.getSaldo();
        totalGanado += pozo + saldoInicial;
        unJugador.setSaldo((float) totalGanado);
        System.out.println("Participacion" + pozo);
        System.out.println("Saldo despues " + unJugador.getSaldo() + unJugador.getNombre());
        avisar("Ganador de la mano");
    }

    public void noEsGanador() {
        totalGanado -= unaMesa.getMontoApostado();
        setEstado(Estado.NO_PAGA);
    }

    public boolean esperandoRonda() {
        return estado.equals(Estado.ESPERANDO_RONDA);
    }

    public List<Carta> getCartas() {
        return cartas;
    }

    public void setCartas(List<Carta> nuevasCartas) {
        this.cartas = nuevasCartas;
        calcularFigura();
        avisar("CartasActualizadas");
    }

    public void agregarCartas(List<Carta> nuevasCartas) {
        this.cartas.addAll(nuevasCartas);
        avisar("CartasActualizadas");
    }

    public void actualizarSaldo(double nuevoSaldo) {
        this.saldoInicial = nuevoSaldo;
        avisar("SaldoActualizado");
    }

    public Figura getFigura() {
        return figura;
    }

    public void setFigura(Figura figura) {
        this.figura = figura;
    }

    public List<Carta> obtenerCartasDeParticipacion() {
        return new ArrayList<>(cartas);
    }

    public void pasar() {
        setEstado(Estado.PASA);
        avisar("Jugador pasó el turno");
    }

    public boolean pagarApuesta(double monto) {
        if (saldoInicial >= monto) {
            saldoInicial -= monto;
            totalApostado += monto;
            setEstado(Estado.PAGA);
            avisar("Pago realizado");
            return true;
        }
        return false;
    }

    public void calcularFigura() {

        figura = CreadorDeFiguras.crearFigura(cartas);
    }

    public void reiniciarParaNuevaMano() {
        cartas.clear();
        figura = null;
        estado = Estado.ESPERANDO;
        avisar("Preparado para nueva mano.");
    }

    public boolean estaActivo() {
        return estado == Estado.APUESTA || estado == Estado.ESPERANDO || estado == Estado.PAGA;
    }

    public boolean descontarSaldo(double monto) {
        if (saldoInicial >= monto) {
            unJugador.setSaldo((float) (unJugador.getSaldo() - unaMesa.getApuestaBase()));
            System.out.println("Jugador Saldo" + unJugador.getSaldo());
            avisar("SaldoActualizado");
            return true;
        } else {
            return false;
        }
    }

    public void determinarYMostrarFigura() {
        Figura figura = getFigura();
        calcularFigura();

    }

    public List<Carta> intercambiarCartas(List<Carta> nuevasCartas) {
        List<Carta> cartasCambiadas = this.cartas.stream()
                .filter(Carta::estaVisible)
                .collect(Collectors.toList());

        this.cartas.removeAll(cartasCambiadas);
        this.cartas.addAll(nuevasCartas);
        return cartasCambiadas;
    }

}
