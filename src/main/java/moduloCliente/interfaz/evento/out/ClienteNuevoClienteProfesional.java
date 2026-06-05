package moduloCliente.interfaz.evento.out;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
