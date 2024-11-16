/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

/**
 *
 * @author Santiago
 */
public abstract class Usuario {
    private String cedula;
    private String password;
    private String nombreCompleto;
    private String nombre;
   
    
    public enum Eventos {
        jugadorLogueado, adminLogueado
    };
    
    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public Usuario(String nombre) {
        this.nombre = nombre;
    }
    
    public boolean esPasswordValido(String password) {
        return getPassword().equals(password);
    }

    public String getNombre() {
        return nombre;
    }

    
    

}
