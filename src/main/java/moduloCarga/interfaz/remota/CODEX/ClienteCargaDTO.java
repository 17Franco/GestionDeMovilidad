/*package moduloCarga.interfaz.remota.CODEX;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moduloCarga.dominio.cliente.Cliente;
import moduloCarga.dominio.cliente.ClienteComun;
import moduloCarga.dominio.cliente.ClienteProfesional;
import moduloCarga.dominio.cliente.TipoProfesional;

@Getter
@Setter
@NoArgsConstructor
public class ClienteCargaDTOCOEX {
    private String cedula;
    private String nombre;
    private String apellido;
    private String numTel;
    private String contra;
    private String tipoCliente;
    private TipoProfesional tipoProfesional;
    private float porcentajeDescuento;

    public Cliente buildCliente() {
        if ("COMUN".equals(tipoCliente)) {
            return new ClienteComun(cedula, nombre, apellido, numTel, contra);
        }
        if ("PROFESIONAL".equals(tipoCliente)) {
            return new ClienteProfesional(
                    cedula,
                    nombre,
                    apellido,
                    numTel,
                    contra,
                    tipoProfesional,
                    porcentajeDescuento);
        }
        throw new IllegalArgumentException("Tipo de cliente invalido");
    }
}
*/