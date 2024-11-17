/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import java.util.List;

public class Trio extends Figura {

    public Trio(List<Carta> cartas) {
        super(TipoFigura.TRIO, cartas);
    }

    @Override
    public boolean esValida(List<Carta> cartas) {
        // Validar si las cartas contienen exactamente un trío
        return cartas.stream()
                .filter(c -> cartas.stream().filter(c2 -> c2.getValorCarta() == c.getValorCarta()).count() == 3)
                .count() == 3;
    }

    @Override
    public int compararCon(Figura otraFigura) {
        if (!(otraFigura instanceof Trio)) {
            return this.getTipoFigura().getValor() - otraFigura.getTipoFigura().getValor();
        }

        // Comparar el valor del trío
        int miTrio = this.getCartas().get(0).getValorCarta();
        int otroTrio = otraFigura.getCartas().get(0).getValorCarta();

        return Integer.compare(miTrio, otroTrio);
    }
}

