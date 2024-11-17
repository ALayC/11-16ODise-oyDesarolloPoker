/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import java.util.List;

public class SinFigura extends Figura {
    public SinFigura(List<Carta> cartas) {
        super(TipoFigura.SIN_FIGURA, cartas);
    }

    @Override
    public boolean esValida(List<Carta> cartas) {
        return true;  // Siempre válida cuando no hay otra figura
    }

    @Override
    public int compararCon(Figura otraFigura) {
        if (this.getTipoFigura().getValor() > otraFigura.getTipoFigura().getValor()) {
            return 1;
        } else if (this.getTipoFigura().getValor() < otraFigura.getTipoFigura().getValor()) {
            return -1;
        }
        return Integer.compare(
            this.getCartas().stream().mapToInt(Carta::getValorCarta).max().orElse(0),
            otraFigura.getCartas().stream().mapToInt(Carta::getValorCarta).max().orElse(0)
        );
    }
}

