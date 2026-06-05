package moduloCarga.dominio;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="MCarga_Cargadores")
public class Cargador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cargador", nullable = false)

    private TipoCargador tipo;

    @Column(name = "tiene_cable", nullable = false)
    private boolean tieneCable;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_conector", nullable = false)
    private TipoConector tipoConector;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_cargador", nullable = false)
    private EstadoCargador estado;

    @Column(name = "fecha_estimada_finalizacion")
    private LocalDateTime fechaEstimadaFinalizacion;

    @Column(name = "potencia_minima", nullable = false)
    private int potenciaMinima;
    
    // Relación con EstacionCarga
    @ManyToOne
    @JoinColumn(name = "estacion_carga_id", nullable = false)
    private EstacionCarga estacionCarga;

    // Relación con Cargas
    @OneToMany(mappedBy = "cargador")
    private List<Carga> cargas = new ArrayList<>();
}
