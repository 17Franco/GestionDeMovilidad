package moduloCliente.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Grupo {
    @Id
    private String nombre;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
