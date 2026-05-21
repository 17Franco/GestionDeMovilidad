package moduloCarga.dominio;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moduloCarga.dominio.cliente.Cliente;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Carga {
    private LocalDate fecha;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;
    private float importeTotal;
    private float recargoPorDemora;
    private float porcentajeAvance;
    private LocalDateTime horaEstimadaFin;
    private EstadoCarga estado;

    //cada carga se asocia a un cliente
    //ManyToOne
    private Cliente clienteAsociado; //no hace falta que le asocie el historial ya que eso lo 
                                     //maneja el cliente (tiene una asociacion él)

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
