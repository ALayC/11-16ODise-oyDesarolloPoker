package Dominio;

import java.util.ArrayList;
import java.util.List;

public class Mano {

    private final int numero;             
    private final List<Participacion> participaciones;  
    private double pozo;                 
    private EstadoMano estado;           
    private Participacion ganador;        
    private CreadorDeFiguras creadorDeFiguras;  

    public Mano(int numero) {
        this.numero = numero;
        this.participaciones = new ArrayList<>();
        this.pozo = 0;
        this.estado = EstadoMano.EN_JUEGO;  
        this.creadorDeFiguras = new CreadorDeFiguras(); 

    }


    public int getNumero() {
        return numero;
    }


    public double getPozo() {
        return pozo;
    }

    public void agregarAlPozo(double monto) {
        this.pozo += monto;
    }


    public EstadoMano getEstado() {
        return estado;
    }

    public void setEstado(EstadoMano estado) {
        this.estado = estado;
    }


    public List<Participacion> getParticipaciones() {
        return participaciones;
    }

    public void agregarParticipacion(Participacion participacion) {
        participaciones.add(participacion);
    }


    public void evaluarFiguras() {
        for (Participacion participacion : participaciones) {
           
            List<Carta> cartasDelJugador = participacion.getCartas();
           
            Figura figura = CreadorDeFiguras.crearFigura(cartasDelJugador);
           
            participacion.setFigura(figura);
        }
    }

  
    public Participacion determinarGanador() {
        Participacion mejorParticipacion = null;

        for (Participacion participacion : participaciones) {
            if (mejorParticipacion == null || participacion.getFigura().esMejorQue(
                    mejorParticipacion.getFigura())) {
                mejorParticipacion = participacion;
            }
        }

        return mejorParticipacion; 
    }

   
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
