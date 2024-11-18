/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import java.util.List;

public class Pierna extends Figura {

    public Pierna(List<Carta> cartas) {
        super(TipoFigura.PIERNA, cartas);
    }

    @Override
    public boolean esValida(List<Carta> cartas) {
        return cartas.stream()
                .anyMatch(carta -> cartas.stream()
                .filter(c -> c.getValorCarta() == carta.getValorCarta())
                .count() == 3);
    }

    @Override
    public int compararCon(Figura otraFigura) {
        if (this.getTipoFigura().getValor() > otraFigura.getTipoFigura().getValor()) {
            return 1;
        } else if (this.getTipoFigura().getValor() < otraFigura.getTipoFigura().getValor()) {
            return -1;
        }
        return Integer.compare(
                this.getCartas().get(0).getValorCarta(),
                otraFigura.getCartas().get(0).getValorCarta()
        );
    }
}
