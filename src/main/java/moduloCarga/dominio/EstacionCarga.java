package moduloCarga.dominio;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EstacionCarga {
    private String descripcion;
    private String calle;
    private String departamento;
    private int longitud;
    private int latitud;

    // Relación con Cargador
    private List<Cargador> cargadores;
}
