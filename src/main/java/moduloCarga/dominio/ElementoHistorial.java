package moduloCarga.dominio;

import moduloCarga.dominio.medioPago.MedioPago;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ElementoHistorial {
    private Carga carga;
    private MedioPago medioPago;


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
