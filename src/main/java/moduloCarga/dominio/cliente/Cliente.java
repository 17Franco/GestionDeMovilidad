package moduloCarga.dominio.cliente;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moduloCarga.dominio.Carga;
import moduloCarga.dominio.HistorialDeCargas;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "MCarga_Cliente")
@Entity (name = "Cliente_Carga")
public abstract class Cliente {
    @Id
    private String cedula;

    private String nombre;
    private String apellido;
    private String numTel;
    private String contra;
    //carga actual, (la ultima carga del historial)
    Carga cargaActual;
    //cada cliente tiene un historial de cargas asociado
    //@OneToMany
    HistorialDeCargas historialAsociado;

    //cuando este la bd implementada en ves de pedirle al cliente su
    //carga se lo pido al manejador de persistencia pasandole
    //cliente y que me de su clave asociada

}
