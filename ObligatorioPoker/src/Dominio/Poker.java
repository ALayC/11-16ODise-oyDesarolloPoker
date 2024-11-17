/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import java.util.List;

public class Poker extends Figura {
    public Poker(List<Carta> cartas) {
        super(TipoFigura.POKER, cartas);
    }

    @Override
    public boolean esValida(List<Carta> cartas) {
        for (Carta carta : cartas) {
            long count = cartas.stream()
                .filter(c -> c.getValorCarta() == carta.getValorCarta())
                .count();
            if (count == 4) {
                return true;
            }
        }
        return false;
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
