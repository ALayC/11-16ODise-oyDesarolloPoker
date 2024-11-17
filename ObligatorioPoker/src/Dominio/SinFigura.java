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
        // Asegura que las cartas estén ordenadas antes de validarlas
        cartas.sort((c1, c2) -> Integer.compare(c1.getValorCarta(), c2.getValorCarta()));

        // Verifica si las cartas están en secuencia consecutiva
        for (int i = 1; i < cartas.size(); i++) {
            if (cartas.get(i).getValorCarta() - cartas.get(i - 1).getValorCarta() != 1) {
                return false;
            }
        }
        return true;
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

