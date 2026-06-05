package moduloCliente.interfaz.remota.rest;

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
    private String cedula;
    private String nombre;
    private String apellido;
    private String numTel;
    private String contra;
    private String tipoCliente;

    private TipoProfesional tipoProfesional;
    private float porcentajeDescuento;

    public Cliente buildClienteComun(){
        Cliente cliente;
        return  cliente = new ClienteComun(
                this.cedula,
                this.nombre,
                this.apellido,
                this.numTel,
                this.contra
        );
    }

    public Cliente buildClienteProfesional(){
        Cliente cliente;
        return  cliente = new ClienteProfesional(
                this.cedula,
                this.nombre,
                this.apellido,
                this.numTel,
                this.contra,
                this.tipoProfesional,
                this.porcentajeDescuento
        );
    }
}
