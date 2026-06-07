package moduloCarga.dominio;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moduloCarga.dominio.cliente.Cliente;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "MCarga_Cargas")
public class Carga {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora_inicio", nullable = false)
    private LocalDateTime horaInicio;
    
    @Column(name = "hora_fin")
    private LocalDateTime horaFin;

    @Column(name = "importe_total")
    private float importeTotal;

    @Column(name = "recargo_por_demora")
    private float recargoPorDemora;

    @Column(name = "porcentaje_avance")
    private float porcentajeAvance;

    @Column(name = "hora_estimada_fin")
    private LocalDateTime horaEstimadaFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoCarga estado;

    //cada carga se asocia a un cliente
    //ManyToOne
    @ManyToOne
    @JoinColumn(name = "cliente_cedula", nullable = false)
    private Cliente clienteAsociado; 

    //relacion con cargador
    @ManyToOne
    @JoinColumn(name = "cargador_id")
    private Cargador cargador;

    @Override
    public String toString() {
        return "Carga{" +
                "\n  fecha=" + fecha +
                "\n  horaInicio=" + horaInicio +
                "\n  horaFin=" + horaFin +
                "\n  importeTotal=" + importeTotal +
                "\n  recargoPorDemora=" + recargoPorDemora +
                "\n  porcentajeAvance=" + porcentajeAvance +
                "\n  horaEstimadaFin=" + horaEstimadaFin +
                "\n  estado=" + estado +
                "\n}";
    }


    //puedo dejar un toString en ves de definirlo yo a mano
}
