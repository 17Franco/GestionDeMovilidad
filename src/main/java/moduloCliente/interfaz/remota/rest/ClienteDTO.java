package moduloCliente.interfaz.remota.rest;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moduloCliente.dominio.TipoProfesional;
import moduloCliente.dominio.cliente.Cliente;
import moduloCliente.dominio.cliente.ClienteComun;
import moduloCliente.dominio.cliente.ClienteProfesional;

@Getter
@Setter
@NoArgsConstructor
public class ClienteDTO {
    @NotNull(message = "Cedula es obligatoria")
    private String cedula;
    @NotNull(message = "Nombre es obligatorio")
    private String nombre;
    @NotNull(message = "Apellido es obligatorio")
    private String apellido;
    @NotNull(message = "Numero telefono es obligatorio")
    private String numTel;
    @NotNull(message = "Contrasena es obligatorio")
    private String contra;
    @NotNull(message = "Tipo de cliente es obligatorio")
    private String tipoCliente;

    private TipoProfesional tipoProfesional;
    private float porcentajeDescuento;



}
