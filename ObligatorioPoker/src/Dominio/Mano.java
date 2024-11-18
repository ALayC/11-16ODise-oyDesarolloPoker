package Dominio;

import java.util.ArrayList;
import java.util.List;

public class Mano {

    private final int numero;             // Número de la mano
    private final List<Participacion> participaciones;  // Participaciones activas en la mano
    private double pozo;                  // Pozo acumulado en la mano
    private EstadoMano estado;            // Estado actual de la mano
    private Participacion ganador;        // Ganador de la mano
    private CreadorDeFiguras creadorDeFiguras;  // Generador de figuras para evaluar las cartas

    public Mano(int numero) {
        this.numero = numero;
        this.participaciones = new ArrayList<>();
        this.pozo = 0;
        this.estado = EstadoMano.EN_JUEGO;  // Estado inicial de la mano
        this.creadorDeFiguras = new CreadorDeFiguras(); // Instanciar el generador de figuras

    }

    // Getter para el número de la mano
    public int getNumero() {
        return numero;
    }

    // Getter y setter para el pozo
    public double getPozo() {
        return pozo;
    }

    public void agregarAlPozo(double monto) {
        this.pozo += monto;
    }

    // Getter y setter para el estado de la mano
    public EstadoMano getEstado() {
        return estado;
    }

    public void setEstado(EstadoMano estado) {
        this.estado = estado;
    }

    // Métodos para manejar las participaciones
    public List<Participacion> getParticipaciones() {
        return participaciones;
    }

    public void agregarParticipacion(Participacion participacion) {
        participaciones.add(participacion);
    }

    // Método para evaluar las figuras de los jugadores
    public void evaluarFiguras() {
        for (Participacion participacion : participaciones) {
            // Obtener las cartas de la participación
            List<Carta> cartasDelJugador = participacion.getCartas();
            // Generar figura usando CreadorDeFiguras
            Figura figura = CreadorDeFiguras.crearFigura(cartasDelJugador);
            // Asignar la figura a la participación
            participacion.setFigura(figura);
        }
    }

    // Método para determinar el ganador de la mano
    public Participacion determinarGanador() {
        Participacion mejorParticipacion = null;

        for (Participacion participacion : participaciones) {
            if (mejorParticipacion == null || participacion.getFigura().esMejorQue(
                    mejorParticipacion.getFigura())) {
                mejorParticipacion = participacion;
            }
        }

        return mejorParticipacion; // Devuelve la mejor participación
    }

    // Getter para el ganador
    public Participacion getGanador() {
        return ganador;
    }

    
    
    public boolean todosHanPasado() {
        return participaciones.stream().allMatch(p -> p.getEstado() == Participacion.Estado.PASA);
    }

    public void acumularPozo() {
        pozo = participaciones.stream()
                .mapToDouble(Participacion::getTotalApostado)
                .sum();
    }

    public boolean todosHanJugado() {
        return participaciones.stream()
                .allMatch(p -> p.getEstado() != Participacion.Estado.ESPERANDO);
    }
}
