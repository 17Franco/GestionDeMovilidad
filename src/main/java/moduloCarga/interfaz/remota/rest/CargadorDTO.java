package moduloCarga.interfaz.remota.rest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CargadorDTO {

    private boolean tieneCable;
    private int potenciaMinima;
}