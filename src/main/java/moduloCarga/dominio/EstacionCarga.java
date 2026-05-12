package moduloCarga.dominio;

import java.util.List;

public class EstacionCarga {
    private String descripcion;
    private String calle;
    private String departamento;
    private int longitud;
    private int latitud;

    // Relación con Cargador
    private List<Cargador> cargadores;
}
