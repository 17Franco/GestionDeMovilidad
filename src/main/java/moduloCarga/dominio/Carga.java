package moduloCarga.dominio;

import java.time.LocalDate;
import java.time.LocalDateTime;

import moduloCarga.dominio.cliente.Cliente;

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
    private Cliente clienteAsociado;
}
