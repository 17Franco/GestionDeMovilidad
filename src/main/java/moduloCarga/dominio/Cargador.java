package moduloCarga.dominio;

import java.time.LocalDateTime;
import java.util.List;

public class Cargador {
    private TipoCargador tipo;
    private boolean tieneCable;
    private TipoConector tipoConector;
    private EstadoCargador estado;
    private LocalDateTime fechaEstimadaFinalizacion;
    private int potenciaMinima;

    // Relación con EstacionCarga
    private EstacionCarga estacionCarga;

    // Relación con Carga
    private List<Carga> cargas;
}
