/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import java.util.List;

public class Par extends Figura {

    public Par(List<Carta> cartas) {
        super(TipoFigura.PAR, cartas);
    }

    @Override
    public boolean esValida(List<Carta> cartas) {
        // Validar si las cartas contienen exactamente un par
        return cartas.stream()
                .filter(c -> cartas.stream().filter(c2 -> c2.getValorCarta() == c.getValorCarta()).count() == 2)
                .count() == 2;
    }

    @Override
    public int compararCon(Figura otraFigura) {
        if (!(otraFigura instanceof Par)) {
            return this.getTipoFigura().getValor() - otraFigura.getTipoFigura().getValor();
        }

        // Comparar el valor del par
        int miPar = this.getCartas().get(0).getValorCarta();
        int otroPar = otraFigura.getCartas().get(0).getValorCarta();

        return Integer.compare(miPar, otroPar);
    }
}

