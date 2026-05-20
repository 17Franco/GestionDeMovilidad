package moduloCliente.dominio.cliente;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moduloCliente.dominio.Reclamos;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "MCliente_Cliente")
@Entity
public abstract class Cliente {
    @Id
    private String cedula;
    private String nombre;
    private String apellido;
    private String numTel;
    private String contra;

    @OneToMany(mappedBy = "cliente")
    private List<Reclamos> reclamos = new ArrayList<>();

    public Cliente(String cedula, String nombre, String apellido,String numTel, String contra) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.numTel = numTel;
        this.contra = contra;
    }
}
