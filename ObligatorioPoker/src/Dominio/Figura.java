package Dominio;

import java.util.List;

public abstract class Figura {
    private final TipoFigura tipoFigura;
    private final List<Carta> cartas;

    public Figura(TipoFigura tipoFigura, List<Carta> cartas) {
        this.tipoFigura = tipoFigura;
        this.cartas = cartas;
    }

    public TipoFigura getTipoFigura() {
        return tipoFigura;
    }

    public List<Carta> getCartas() {
        return cartas;
    }

    public abstract boolean esValida(List<Carta> cartas);

    public abstract int compararCon(Figura otraFigura);

    @Override
    public String toString() {
        return tipoFigura + " con cartas: " + cartas;
    }
    
    public boolean esMejorQue(Figura otraFigura) {
    if (this.tipoFigura.getValor() > otraFigura.getTipoFigura().getValor()) {
        return true;
    } else if (this.tipoFigura.getValor() == otraFigura.getTipoFigura().getValor()) {
        // Comparar valores principales en caso de empate
        return this.cartas.get(0).getValorCarta() > otraFigura.getCartas().get(0).getValorCarta();
    }
    return false;
}

    
}
