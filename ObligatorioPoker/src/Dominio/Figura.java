/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

/**
 *
 * @author alay
 */
import java.util.List;

public class Figura {
    private final TipoFigura tipoFigura;  // Tipo de la figura, como POKER, ESCALERA, etc.
    private final Valor valorPrincipal;   // Valor principal de la figura (ej., en Poker de K, sería K)
    private final List<Carta> cartas;     // Las cartas que conforman la figura

    // Constructor para inicializar la figura
    public Figura(TipoFigura tipoFigura, Valor valorPrincipal, List<Carta> cartas) {
        this.tipoFigura = tipoFigura;
        this.valorPrincipal = valorPrincipal;
        this.cartas = cartas;
    }

    // Métodos getters para acceder a los atributos
    public TipoFigura getTipoFigura() {
        return tipoFigura;
    }

    public Valor getValorPrincipal() {
        return valorPrincipal;
    }

    public List<Carta> getCartas() {
        return cartas;
    }

    @Override
    public String toString() {
        return tipoFigura + " con cartas: " + cartas;
    }
}

