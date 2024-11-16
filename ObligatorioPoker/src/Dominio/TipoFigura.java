/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

/**
 *
 * @author alay
 */
public enum TipoFigura {
    POKER(4), ESCALERA(3), PIERNA(2), PAR(1), SIN_FIGURA(0);

    private final int valor;

    TipoFigura(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }
}
