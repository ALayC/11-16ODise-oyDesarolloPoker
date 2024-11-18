package Dominio;

import java.util.List;

public class CreadorDeFiguras {

    public static Figura crearFigura(List<Carta> cartas) {
        if (new Poker(cartas).esValida(cartas)) {
            return new Poker(cartas);
        }
        if (new Escalera(cartas).esValida(cartas)) {
            return new Escalera(cartas);
        }
        if (new Trio(cartas).esValida(cartas)) {
            return new Trio(cartas);
        }
        if (new Par(cartas).esValida(cartas)) {
            return new Par(cartas);
        }
        return new SinFigura(cartas); // Si no se encuentra ninguna figura válida
    }

}
