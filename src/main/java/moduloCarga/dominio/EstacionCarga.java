package moduloCarga.dominio;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MCarga_EstacionCarga")
public class EstacionCarga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "calle", nullable = false)
    private String calle;

    @Column(name = "departamento", nullable = false)
    private String departamento;

    @Column(name = "longitud", nullable = false)
    private int longitud;

    @Column(name = "latitud", nullable = false)
    private int latitud;

    // Relación con Cargador
    @OneToMany(mappedBy = "estacionCarga")
    private List<Cargador> cargadores = new ArrayList<>();
}