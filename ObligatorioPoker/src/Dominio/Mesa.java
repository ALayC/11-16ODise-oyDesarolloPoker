package Dominio;

import Observador.Observable;
import java.util.ArrayList;
import java.util.List;

public class Mesa extends Observable {
    private int cantidadJugadores;         
    private double apuestaBase;            
    private double porcentajeComision;     
    private EstadoMesa estado;             
    private static int contadorMesas = 1;  
    private int numeroMesa;                
    private int cantidadActualJugadores;   
    private int numeroManoActual;          
    private double montoTotalApostado;     
    private double montoTotalRecaudado;    
    private List<Mano> manos;              
    private List<Participacion> participaciones;

    // Constructor vacío
    public Mesa() {
        this.manos = new ArrayList<>();
        this.participaciones = new ArrayList<>();
    }

    // Constructor completo
    public Mesa(int cantidadJugadores, double apuestaBase, double porcentajeComision) {
        if (!esValida(cantidadJugadores, apuestaBase, porcentajeComision)) {
            throw new IllegalArgumentException("Parámetros inválidos para crear la mesa.");
        }

        this.cantidadJugadores = cantidadJugadores;
        this.apuestaBase = apuestaBase;
        this.porcentajeComision = porcentajeComision;
        this.estado = new EstadoAbierta();
        this.numeroMesa = contadorMesas++;
        this.cantidadActualJugadores = 0;
        this.numeroManoActual = 0;
        this.montoTotalApostado = 0;
        this.montoTotalRecaudado = 0;
        this.manos = new ArrayList<>();
        this.participaciones = new ArrayList<>(); // Inicialización asegurada
        avisar(this); // Notificar que se ha creado una nueva mesa
    }

    public void agregarParticipacion(Participacion participacion) {
        // Solo agrega la participación sin modificar `cantidadActualJugadores`
        if (participaciones.size() < cantidadJugadores) {
            participaciones.add(participacion);
            avisar(this);
        }
    }
    
    public List<Participacion> getParticipaciones() {
        return participaciones;
    } 
    // Getters para los atributos requeridos
    public int getNumeroMesa() {
        return numeroMesa;
    }

    public int getCantidadJugadores() {
        return cantidadJugadores;
    }

    public double getApuestaBase() {
        return apuestaBase;
    }

    public int getCantidadActualJugadores() {
        return cantidadActualJugadores;
    }

    public int getNumeroManoActual() {
        return numeroManoActual;
    }

    public double getMontoTotalApostado() {
        return montoTotalApostado;
    }

    public double getPorcentajeComision() {
        return porcentajeComision;
    }

    public double getMontoTotalRecaudado() {
        return montoTotalRecaudado;
    }

    public EstadoMesa getEstado() {
        return estado;
    }

    public List<Mano> getManos() {
        return manos;
    }

    public void setEstado(EstadoMesa estado) {
        this.estado = estado;
    }
    
    
    // Métodos para cambiar el estado de la mesa
    public void abrirMesa() {
        estado.abrirMesa(this);
        avisar(EventosMesa.MesaCreada);
    }

    public void cerrarMesa() {
        estado.finalizarJuego(this);
        avisar("Mesa cerrada");  // Notificar que la mesa ha sido cerrada
    }

    private void iniciarMesa() {
        estado = new EstadoIniciada();
        estado.iniciarJuego(this);

        Mazo mazo = new Mazo();
        mazo.barajar();

        for (Participacion participacion : participaciones) {
            Mano mano = new Mano(numeroManoActual);
            mano.setCartas(new ArrayList<>(mazo.repartirMano(5))); // Asigna 5 cartas al jugador
            participacion.setUnaMano(mano); // Asigna la mano a la participación
        }

        avisar("La mesa ha iniciado el juego.");
    }

    
    
    
    
    // Método para agregar una mano a la mesa y actualizar los totales
    public void agregarMano(Mano mano) {
        this.manos.add(mano);
        this.numeroManoActual++;
        this.montoTotalApostado += mano.getTotalApostado();
        this.montoTotalRecaudado += mano.getTotalApostado() * (1 - porcentajeComision / 100);
    }

    // Método para actualizar la cantidad actual de jugadores
    public void actualizarCantidadJugadores(int cantidad) {
        this.cantidadActualJugadores = cantidad;
        avisar("Cantidad de jugadores actualizada");
  }
    private boolean esValida(int cantidadJugadores, double apuestaBase, double porcentajeComision) {
        return cantidadJugadores >= 2 && cantidadJugadores <= 5
            && apuestaBase >= 1
            && porcentajeComision >= 1 && porcentajeComision <= 50;
    }
    
    public boolean agregarJugador(Jugador jugador) {
        // Verificar si el jugador ya está en la mesa para evitar duplicados
        if (participaciones.stream().anyMatch(p -> p.getUnJugador().equals(jugador))) {
            return false;  // El jugador ya está en la mesa
        }

        if (jugador.getSaldo() < apuestaBase * 10) {
            return false;  // Saldo insuficiente
        }

        if (cantidadActualJugadores >= cantidadJugadores) {
            return false;  // La mesa ya está llena
        }

        // Crear y agregar la participación sin incrementar `cantidadActualJugadores` en `agregarParticipacion`
        Participacion participacion = new Participacion(jugador, this, jugador.getSaldo());
        agregarParticipacion(participacion);

        // Incrementar la cantidad de jugadores una vez, solo aquí
        cantidadActualJugadores++;
        avisar(this); 

        // Cambiar el estado si se alcanza la cantidad requerida de jugadores
        if (cantidadActualJugadores == cantidadJugadores) {
            iniciarMesa();
        }

        return true;
    }
        // Método para obtener el pozo actual de la mesa
    public double getPozo() {
        return montoTotalApostado; // Pozo total de la mesa
    }

    // Método para obtener el monto total apostado en la mesa
    public double getMontoApostado() {
        return montoTotalApostado; // Total acumulado de apuestas en la mesa
    }
    
    public void notificarObservadores(Object evento) {
    avisar(evento); // Llama al método protected avisar()
    }

    public void setCantidadJugadores(int cantidadJugadores) {
        this.cantidadJugadores = cantidadJugadores;
    }

    public void setApuestaBase(double apuestaBase) {
        this.apuestaBase = apuestaBase;
    }

    public void setPorcentajeComision(double porcentajeComision) {
        this.porcentajeComision = porcentajeComision;
    }

    public void setNumeroMesa(int numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    
    
    
    
    
    
}