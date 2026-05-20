package moduloCarga.dominio.cliente;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
