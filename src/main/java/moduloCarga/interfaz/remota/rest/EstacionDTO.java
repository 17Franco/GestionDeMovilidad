package moduloCarga.interfaz.remota.rest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EstacionDTO {

    private String descripcion;
    private String calle;
    private String departamento;
    private int longitud;
    private int latitud;
}