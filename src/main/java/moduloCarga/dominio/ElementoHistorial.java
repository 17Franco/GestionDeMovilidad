package moduloCarga.dominio;

import moduloCarga.dominio.medioPago.MedioPago;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
@Table(name = "MCarga_ElementoHistorial")
public class ElementoHistorial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "carga_id", nullable = false)
    private Carga carga;

    @ManyToOne
    @JoinColumn(name = "medio_pago_id", nullable = false)
    private MedioPago medioPago;

    @ManyToOne
    @JoinColumn(name = "historial_id", nullable = false)
    private HistorialDeCargas historialAsociado;



    @Override
    public String toString() {
        return "Carga{" +
                "\n  fecha=" + carga.getFecha() +
                "\n  horaInicio=" + carga.getHoraInicio() +
                "\n  horaFin=" + carga.getHoraFin() +
                "\n  importeTotal=" + carga.getImporteTotal()+
                "\n  recargoPorDemora=" + carga.getRecargoPorDemora() +
                "\n  porcentajeAvance=" + carga.getPorcentajeAvance() +
                "\n  horaEstimadaFin=" + carga.getHoraEstimadaFin() +
                "\n  estado=" + carga.getEstado() +
                "\n}" +
                "Medio de Pago = " + medioPago +
                "\n" + "\n";

    }

    


}
