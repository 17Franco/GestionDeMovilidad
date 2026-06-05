package moduloCliente.interfaz.remota.rest;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReclamoDTO {
    private Long id;
    @NotNull(message = "Asunto es un campo obligatorio")
    private String asunto;
    @NotNull(message = "Descripcion es un campo obligatorio")
    private String descripcion;
    private String clienteCi;

}
