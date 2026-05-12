package moduloCarga.dominio;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Carga {
    private LocalDate fecha;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;
    private float importeTotal;
    private float recargoPorDemora;
    private float porcentajeAvance;
    private LocalDateTime horaEstimadaFin;
    private EstadoCarga estado;

    // Relación con Cargador
    private Cargador cargador;
}
