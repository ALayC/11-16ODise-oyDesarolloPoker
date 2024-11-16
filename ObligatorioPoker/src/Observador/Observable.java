/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Observador;

import java.util.ArrayList;

/**
 *
 * @author alay
 */
public abstract class Observable {
    
    private ArrayList<Observador> observadores = new ArrayList();
    
    public void agregarObservador(Observador o){
            observadores.add(o);
    }
    public void quitar(Observador o){
        observadores.remove(o);
    }
  
        protected void avisar(Object evento) {
        for (Observador observador : observadores) {
            observador.actualizar(this, evento);
        }
    }
    
}
