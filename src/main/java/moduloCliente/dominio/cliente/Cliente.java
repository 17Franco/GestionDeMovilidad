package moduloCliente.dominio.cliente;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moduloCliente.dominio.Grupo;
import moduloCliente.dominio.Reclamo;
import moduloCliente.dominio.Tarjeta;

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
    private List<Reclamo> reclamos = new ArrayList<>();

    @OneToMany(mappedBy = "cliente")
    private List<Tarjeta> tarjetas = new ArrayList<>();

    @ManyToMany (fetch = FetchType.EAGER)
    private List<Grupo> grupos;

    public Cliente(String cedula, String nombre, String apellido,String numTel, String contra) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.numTel = numTel;
        this.contra = contra;
    }

    public List<String> gruposAsString() {
        List<String> grupos = new ArrayList<String>();
        for (Grupo grupo : this.grupos) {
            grupos.add(grupo.getNombre());
        }
        //System.out.println("Lista de grupos:" + grupos.toString());
        return grupos;
    }
}
