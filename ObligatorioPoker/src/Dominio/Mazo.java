package Dominio;

import panelCartasPoker.CartaPoker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mazo {
    private final List<Carta> cartas;

    public Mazo() {
        cartas = new ArrayList<>();
        // Generar las 52 cartas del mazo usando los valores de `CartaPoker`
        int[] valores = {
            CartaPoker.AS, CartaPoker.DOS, CartaPoker.TRES, CartaPoker.CUATRO,
            CartaPoker.CINCO, CartaPoker.SEIS, CartaPoker.SIETE, CartaPoker.OCHO,
            CartaPoker.NUEVE, CartaPoker.DIEZ, CartaPoker.J, CartaPoker.Q, CartaPoker.K
        };
        String[] palos = {CartaPoker.CORAZON, CartaPoker.DIAMANTE, CartaPoker.TREBOL, CartaPoker.PIQUE};

        for (String palo : palos) {
            for (int valor : valores) {
                cartas.add(new Carta(valor, palo));
            }
        }
    }

    public void barajar() {
        Collections.shuffle(cartas);
    }

    public List<Carta> repartirMano(int cantidad) {
        if (cantidad > cartas.size()) {
            throw new IllegalArgumentException("No hay suficientes cartas en el mazo.");
        }
        List<Carta> mano = new ArrayList<>(cartas.subList(0, cantidad));
        cartas.removeAll(mano);
        return mano;
    }

    public void reiniciarMazo() {
        cartas.clear();
        int[] valores = {
            CartaPoker.AS, CartaPoker.DOS, CartaPoker.TRES, CartaPoker.CUATRO,
            CartaPoker.CINCO, CartaPoker.SEIS, CartaPoker.SIETE, CartaPoker.OCHO,
            CartaPoker.NUEVE, CartaPoker.DIEZ, CartaPoker.J, CartaPoker.Q, CartaPoker.K
        };
        String[] palos = {CartaPoker.CORAZON, CartaPoker.DIAMANTE, CartaPoker.TREBOL, CartaPoker.PIQUE};

        for (String palo : palos) {
            for (int valor : valores) {
                cartas.add(new Carta(valor, palo));
            }
        }
        barajar();
    }
}
