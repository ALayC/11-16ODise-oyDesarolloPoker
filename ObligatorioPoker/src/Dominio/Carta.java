
package Dominio;

import panelCartasPoker.CartaPoker;


public class Carta implements panelCartasPoker.CartaPoker{

    private int numero;
    private String palo;
    private boolean visible = true;

    public Carta(int num, String palo) {
        this.numero = num;
        this.palo = palo;
    }
    
    @Override
    public int getValorCarta() {
        return numero;
    }

    @Override
    public String getPaloCarta() {
        return palo;
    }

    @Override
    public boolean estaVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean b) {
        visible = b;
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return "Carta Ejemplo{" + "numero=" + numero + ", palo=" + palo + ", visible=" + visible + '}';
    }
}