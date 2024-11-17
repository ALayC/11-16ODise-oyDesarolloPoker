/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import java.util.List;

public class CreadorDeFiguras {
    /**
     * Determina la figura que corresponde a una lista de cartas.
     *
     * @param cartas Lista de cartas que se analizarán.
     * @return La figura correspondiente (Poker, Escalera, etc.).
     */
    public static Figura crearFigura(List<Carta> cartas) {
        if (new Poker(cartas).esValida(cartas)) {
            return new Poker(cartas);
        }
        if (new Escalera(cartas).esValida(cartas)) {
            return new Escalera(cartas);
        }
        if (new Pierna(cartas).esValida(cartas)) {
            return new Pierna(cartas);
        }
        return new SinFigura(cartas);  // Si no se encuentra ninguna figura válida
    }
}

