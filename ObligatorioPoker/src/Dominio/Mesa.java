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
    private double ultimaApuesta = 0; 
    private Participacion jugadorUltimaApuesta; 
    // Constructor vacío

    public Mesa() {
        this.manos = new ArrayList<>();
        this.participaciones = new ArrayList<>();
    }

  
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
        this.participaciones = new ArrayList<>(); 
        avisar(this); 
    }

    public void agregarParticipacion(Participacion participacion) {
        
        if (participaciones.size() < cantidadJugadores) {
            participaciones.add(participacion);
            avisar(this);
        }
    }

    public List<Participacion> getParticipaciones() {
        return participaciones;
    }


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

    
    public void abrirMesa() {
        estado.abrirMesa(this);
        avisar(EventosMesa.MesaCreada);
    }

    public void cerrarMesa(Mesa mesa) {
        estado.finalizarJuego(this);
        avisar("Mesa cerrada"); 
    }

    public void iniciarMesa() {
        montoTotalApostado = 0; 
        estado = new EstadoIniciada();
        estado.iniciarJuego(this);
        Mazo mazo = new Mazo();
        mazo.barajar();

        Mano mano = new Mano(numeroManoActual);

       
        List<Participacion> participantesEliminados = new ArrayList<>();
        for (Participacion participacion : participaciones) {
            double luz = apuestaBase;
            if (participacion.descontarSaldo(luz)) {
                montoTotalApostado += luz; 
                
               
                List<Carta> cartasDelJugador = mazo.repartirMano(5);
                participacion.setCartas(cartasDelJugador);
                mano.agregarParticipacion(participacion); 
            } else {
                participantesEliminados.add(participacion); 
            }
        }

    
        participaciones.removeAll(participantesEliminados);

        manos.add(mano);
        avisar("La mesa ha iniciado el juego.");
    }

    
    public void agregarMano(Mano mano) {
        this.manos.add(mano);
        this.numeroManoActual++;

        
        double totalApostadoEnMano = mano.getParticipaciones()
                .stream()
                .mapToDouble(Participacion::getTotalApostado)
                .sum();

        this.montoTotalApostado += totalApostadoEnMano;
        this.montoTotalRecaudado += totalApostadoEnMano * (1 - porcentajeComision / 100);
    }

    
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
        
        if (participaciones.stream().anyMatch(p -> p.getUnJugador().equals(jugador))) {
            return false;  
        }

        if (jugador.getSaldo() < apuestaBase * 10) {
            return false; 
        }

        if (cantidadActualJugadores >= cantidadJugadores) {
            return false;  
        }

        
        Participacion participacion = new Participacion(jugador, this, jugador.getSaldo());
        agregarParticipacion(participacion);

       
        cantidadActualJugadores++;
        avisar(this);

        
        if (cantidadActualJugadores == cantidadJugadores) {
            iniciarMesa();
        }

        return true;
    }
   
    public double getPozo() {
        return montoTotalApostado; 
    }

    public void agregarAlPozo(double monto) {
        this.montoTotalApostado += monto;
        avisar(this); 
    }


    public double getMontoApostado() {
        return montoTotalApostado; 
    }

    public void notificarObservadores(Object evento) {
        avisar(evento); 
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

    public void eliminarParticipacion(Participacion participacion) {
        participaciones.remove(participacion); 
        cantidadActualJugadores--;
        avisar(this); 
    }

    public Mano getManoActual() {
        if (manos.isEmpty()) {
            return null; 
        }
        return manos.get(manos.size() - 1); 
    }

    public void iniciarNuevaMano() {
        Mazo mazo = new Mazo();
        mazo.barajar();

        Mano nuevaMano = new Mano(numeroManoActual);
        for (Participacion participacion : participaciones) {
            List<Carta> nuevasCartas = mazo.repartirMano(5);
            participacion.setCartas(nuevasCartas); 
            nuevaMano.agregarParticipacion(participacion);
        }
        System.out.println("inicia nueva mano");
        manos.add(nuevaMano);
        numeroManoActual++;
        avisar("Nueva mano iniciada.");
    }

    public void barajarNuevaMano() {
        Mazo mazo = new Mazo();
        mazo.barajar();

        Mano nuevaMano = new Mano(numeroManoActual);
        for (Participacion participacion : participaciones) {
            participacion.setCartas(mazo.repartirMano(5)); 
            nuevaMano.agregarParticipacion(participacion);
        }

        manos.add(nuevaMano);
        numeroManoActual++;
        avisar("Nueva mano iniciada.");
    }

    public boolean estaListaParaIniciar() {
        return this.getCantidadActualJugadores() == this.getCantidadJugadores();
    }

    public void incrementarPozo(double monto) {
        montoTotalApostado += monto; 
        avisar("PozoActualizado"); 
    }

    public void registrarJugadorPasa(Participacion participacion) {
        participacion.pasar(); 
        verificarEstadoMano(); 
    }

    private void verificarEstadoMano() {
        boolean todosPasaron = participaciones.stream()
                .allMatch(p -> p.getEstado() == Participacion.Estado.PASA);

        if (todosPasaron) {
            finalizarMano(); 
        }
    }

    public void finalizarMano() {
        if (todosHanPasado()) {
            System.out.println("Todos han pasado. Iniciando nueva mano...");


            for (Participacion participacion : participaciones) {
                participacion.reiniciarParaNuevaMano(); 
                participacion.setCartas(new Mazo().repartirMano(5)); 
            }

           
            iniciarNuevaMano();
        } else {
            System.out.println("Error: No todos han pasado.");
        }
    }

    private void verificarSaldoJugadores() {
        participaciones.removeIf(p -> p.getUnJugador().getSaldo() < apuestaBase);
    }

    public void registrarJugadorPasaInicioApuesta(Participacion participacion) {
        participacion.pasar(); 
        verificarTodosPasaronInicioApuesta(); 
    }

    public void verificarTodosPasaronInicioApuesta() {
        boolean todosPasaron = participaciones.stream()
                .allMatch(p -> p.getEstado() == Participacion.Estado.PASA);

        if (todosPasaron) {
            finalizarMano(); 
        }
    }

    public double getUltimaApuesta() {
        return ultimaApuesta;
    }

    public void setUltimaApuesta(double ultimaApuesta) {
        this.ultimaApuesta = ultimaApuesta;
    }

    public Participacion getJugadorUltimaApuesta() {
        return jugadorUltimaApuesta;
    }

    public void setJugadorUltimaApuesta(Participacion jugadorUltimaApuesta) {
        this.jugadorUltimaApuesta = jugadorUltimaApuesta;
    }

    public void determinarGanador() {
        Participacion ganador = null;
        for (Participacion participacion : participaciones) {
            if (ganador == null || participacion.getFigura().esMejorQue(ganador.getFigura())) {
                ganador = participacion;
            }
        }
        if (ganador != null) {
            double pozo = getPozo();
            ganador.esGanador(pozo); 
            setUltimaApuesta(0); 
            setJugadorUltimaApuesta(null); 
            System.out.println("Ganador: " + ganador.getUnJugador().getNombreCompleto() + " | Figura: " + ganador.getFigura());
        } else {
            System.out.println("No hay ganador.");
        }
    }

    public boolean todosHanPasado() {
        boolean todosPasaron = participaciones.stream()
                .allMatch(p -> p.getEstado() == Participacion.Estado.PASA);

        if (todosPasaron) {
           
            participaciones.forEach(p -> p.setEstado(Participacion.Estado.ESPERANDO));
        }

        return todosPasaron;
    }
        public void iniciarCambioDeCartas() {
        for (Participacion participacion : participaciones) {
            if (participacion.getEstado() == Participacion.Estado.PAGA) {
                participacion.setEstado(Participacion.Estado.CAMBIO_DE_CARTAS);
            }
        }
        avisar("Cambio de cartas iniciado.");
    }

}
