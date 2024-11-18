/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Par extends Figura {

    public Par(List<Carta> cartas) {
        super(TipoFigura.PAR, cartas);
    }

    @Override
    public boolean esValida(List<Carta> cartas) {
        // Mapa para contar las ocurrencias de cada valor
        Map<Integer, Integer> conteoValores = new HashMap<>();

        // Contar cuántas veces aparece cada valor
        for (Carta carta : cartas) {
            int valor = carta.getValorCarta();
            conteoValores.put(valor, conteoValores.getOrDefault(valor, 0) + 1);
        }

        // Verificar si existe exactamente un par (valor que aparece exactamente 2 veces)
        int pares = 0;
        for (int cantidad : conteoValores.values()) {
            if (cantidad == 2) {
                pares++;
            }
        }

        // Debe haber exactamente un par
        return pares == 1;
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
