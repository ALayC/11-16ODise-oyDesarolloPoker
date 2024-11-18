/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Poker extends Figura {

    public Poker(List<Carta> cartas) {
        super(TipoFigura.POKER, cartas);
    }

    @Override
    public boolean esValida(List<Carta> cartas) {
        Map<Integer, Integer> conteoValores = new HashMap<>();


        for (Carta carta : cartas) {
            int valor = carta.getValorCarta();
            conteoValores.put(valor, conteoValores.getOrDefault(valor, 0) + 1);
        }


        for (int cantidad : conteoValores.values()) {
            if (cantidad == 4) {
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
