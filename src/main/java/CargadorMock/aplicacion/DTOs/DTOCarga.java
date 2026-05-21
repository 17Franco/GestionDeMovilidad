package CargadorMock.aplicacion.DTOs;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DTOCarga {
    private int id;
    private LocalDate fecha;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;
    private float importeTotal;
    private float recargoPorDemora;
    private float porcentajeAvance;
    private LocalDateTime horaEstimadaFin;
    private DTOEstadoCarga estado;
    /*
    La Carga del mock no lleva cliente ya que el que se encarga de asociar el cliente a la carga es el modulo de carga
    sino tendria el problema (que lo tuve) de que el Cargador asocia SU cliente del package "import CargadorMock.aplicacion.Dominio.cliente.Cliente;"
    mientras que en el modulo de que carga yo le paso un cliente del package "package moduloCarga.dominio.cliente;"
    private Cliente clienteAsociado; 
     */
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
