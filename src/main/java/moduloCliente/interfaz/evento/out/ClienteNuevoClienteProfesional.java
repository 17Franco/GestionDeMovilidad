package moduloCliente.interfaz.evento.out;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import moduloCarga.dominio.cliente.TipoProfesional;

@Getter
@Setter
@AllArgsConstructor
public class ClienteNuevoClienteProfesional {
    private String cedula;
    private String nombre;
    private String apellido;
    private String numTel;
    private String contra;
    private String tipo;
    private float porcentajeDescuento;
}
