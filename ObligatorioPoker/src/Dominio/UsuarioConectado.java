
package Dominio;
import Dominio.Usuario;
import java.time.LocalDateTime;


public class UsuarioConectado {
    private LocalDateTime fechaDeIngreso;
    private Usuario usuario;

    public UsuarioConectado(Usuario usuario) {
        this.usuario = usuario;
        this.fechaDeIngreso= LocalDateTime.now();
    }

    public LocalDateTime getFechaDeIngreso() {
        return fechaDeIngreso;
    }

    public Usuario getUsuario() {
        return usuario;
    }
    
    
}
