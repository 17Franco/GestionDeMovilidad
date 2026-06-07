package moduloCarga.interfaz.remota.rest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moduloCarga.dominio.EstacionCarga;
import moduloCarga.dominio.TipoCargador;
import moduloCarga.dominio.TipoConector;

@Getter
@Setter
@NoArgsConstructor
public class CargadorDTO {
    private String  tipo;
    private boolean tieneCable;
    private String tipoConector;
    private int potenciaMinima;
    private int estacionCarga; //el id de la estacion
}