package moduloCliente.interfaz.remota.rest;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReclamoDTO {
    private Long id;
    private String asunto;
    private String descripcion;
    private String clienteCi;

}
