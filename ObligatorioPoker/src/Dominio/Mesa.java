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
    private double ultimaApuesta = 0; // Almacena el monto de la última apuesta
    private Participacion jugadorUltimaApuesta; // Almacena quién hizo la última apuesta
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

    public void cerrarMesa(Mesa mesa) {
        estado.finalizarJuego(this);
        avisar("Mesa cerrada");  // Notificar que la mesa ha sido cerrada
    }

    public void iniciarMesa() {
        montoTotalApostado = 0; // Inicializa el pozo en 0
        estado = new EstadoIniciada();
        estado.iniciarJuego(this);
        Mazo mazo = new Mazo();
        mazo.barajar();

        Mano mano = new Mano(numeroManoActual);

        // Usa un iterador para evitar problemas al modificar la lista
        List<Participacion> participantesEliminados = new ArrayList<>();
        for (Participacion participacion : participaciones) {
            double luz = apuestaBase;
            if (participacion.descontarSaldo(luz)) {
                montoTotalApostado += luz; // Incrementar el pozo con la luz

                // Asigna cartas a la participación
                List<Carta> cartasDelJugador = mazo.repartirMano(5);
                participacion.setCartas(cartasDelJugador); // Asigna las cartas
                mano.agregarParticipacion(participacion); // Agrega la participación a la mano
            } else {
                participantesEliminados.add(participacion); // Agrega a una lista temporal para eliminar después
            }
        }

        // Elimina las participaciones que no pudieron pagar la luz
        participaciones.removeAll(participantesEliminados);

        manos.add(mano);
        avisar("La mesa ha iniciado el juego.");
    }

    // Método para agregar una mano a la mesa y actualizar los totales
    public void agregarMano(Mano mano) {
        this.manos.add(mano);
        this.numeroManoActual++;

        // Calcular el total apostado en la mano a partir de las participaciones
        double totalApostadoEnMano = mano.getParticipaciones()
                .stream()
                .mapToDouble(Participacion::getTotalApostado)
                .sum();

        this.montoTotalApostado += totalApostadoEnMano;
        this.montoTotalRecaudado += totalApostadoEnMano * (1 - porcentajeComision / 100);
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

    public void agregarAlPozo(double monto) {
        this.montoTotalApostado += monto;
        avisar(this); // Notificar el cambio a los observadores
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

    public void eliminarParticipacion(Participacion participacion) {
        participaciones.remove(participacion); // Elimina la participación del jugador
        cantidadActualJugadores--; // Reduce la cantidad de jugadores
        avisar(this); // Notifica que la mesa ha cambiado
    }

    public Mano getManoActual() {
        if (manos.isEmpty()) {
            return null; // Si no hay manos activas, devuelve null
        }
        return manos.get(manos.size() - 1); // Devuelve la última mano de la lista
    }

    public void iniciarNuevaMano() {
        Mazo mazo = new Mazo();
        mazo.barajar();

        Mano nuevaMano = new Mano(numeroManoActual);
        for (Participacion participacion : participaciones) {
            // Reparte nuevas cartas
            List<Carta> nuevasCartas = mazo.repartirMano(5);
            participacion.setCartas(nuevasCartas); // Actualiza las cartas en la participación
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
            participacion.setCartas(mazo.repartirMano(5)); // Reparte nuevas cartas
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
        montoTotalApostado += monto; // Incrementa el pozo
        avisar("PozoActualizado"); // Notifica a las vistas si es necesario
    }

    public void registrarJugadorPasa(Participacion participacion) {
        participacion.pasar(); // Actualiza el estado del jugador a PASA
        verificarEstadoMano(); // Verifica si todos los jugadores pasaron
    }

    private void verificarEstadoMano() {
        boolean todosPasaron = participaciones.stream()
                .allMatch(p -> p.getEstado() == Participacion.Estado.PASA);

        if (todosPasaron) {
            finalizarMano(); // Termina la mano actual si todos pasan
        }
    }

    public void finalizarMano() {
        if (todosHanPasado()) {
            System.out.println("Todos han pasado. Iniciando nueva mano...");

            // Reiniciar estado de jugadores a ESPERANDO y repartir nuevas cartas
            for (Participacion participacion : participaciones) {
                participacion.reiniciarParaNuevaMano(); // Limpia estado y figura
                participacion.setCartas(new Mazo().repartirMano(5)); // Nuevas cartas
            }

            // Iniciar una nueva mano
            iniciarNuevaMano();
        } else {
            System.out.println("Error: No todos han pasado.");
        }
    }

    private void verificarSaldoJugadores() {
        participaciones.removeIf(p -> p.getUnJugador().getSaldo() < apuestaBase);
    }

    public void registrarJugadorPasaInicioApuesta(Participacion participacion) {
        participacion.pasar(); // Cambia el estado del jugador a PASA
        verificarTodosPasaronInicioApuesta(); // Verifica si todos pasaron
    }

    public void verificarTodosPasaronInicioApuesta() {
        boolean todosPasaron = participaciones.stream()
                .allMatch(p -> p.getEstado() == Participacion.Estado.PASA);

        if (todosPasaron) {
            finalizarMano(); // Termina la mano si todos pasan
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
            ganador.esGanador(pozo); // Otorga el pozo al ganador
            setUltimaApuesta(0); // Reinicia la última apuesta
            setJugadorUltimaApuesta(null); // Limpia el registro de la última apuesta
            System.out.println("Ganador: " + ganador.getUnJugador().getNombreCompleto() + " | Figura: " + ganador.getFigura());
        } else {
            System.out.println("No hay ganador.");
        }
    }

    public boolean todosHanPasado() {
        boolean todosPasaron = participaciones.stream()
                .allMatch(p -> p.getEstado() == Participacion.Estado.PASA);

        if (todosPasaron) {
            // Cambiar a ESPERANDO antes de reiniciar
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
