package moduloCliente.interfaz.remota.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moduloCliente.dominio.TipoProfesional;

@Getter
@Setter
@NoArgsConstructor
public class ClienteDTO {
    private String cedula;
    private String nombre;
    private String apellido;
    private String numTel;
    private String contra;
    private String tipoCliente;

    private TipoProfesional tipoProfesional;
    private float porcentajeDescuento;


}
