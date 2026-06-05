package moduloCarga.dominio.cliente;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moduloCarga.dominio.Carga;
import moduloCarga.dominio.HistorialDeCargas;
import moduloCarga.dominio.medioPago.Tarjeta;


import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "cliente")
    private List<Tarjeta> tarjetas = new ArrayList<>();

    public Cliente(String cedula, String nombre, String apellido,String numTel, String contra) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.numTel = numTel;
        this.contra = contra;
    }
  
   // carga actual del cliente
    @OneToOne
    @JoinColumn(name = "carga_actual_id")
    private Carga cargaActual;

    // cada cliente tiene un historial de cargas asociado
    @OneToOne(mappedBy = "clienteAsociado", cascade = CascadeType.ALL)
    private HistorialDeCargas historialAsociado;

    
}
