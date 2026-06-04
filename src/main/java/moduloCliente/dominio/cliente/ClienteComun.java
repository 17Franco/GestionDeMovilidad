package moduloCliente.dominio.cliente;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moduloCliente.dominio.CuentaUTE;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "MCliente_ClienteComun")
public class ClienteComun extends Cliente{

    @OneToOne
    @JoinColumn(name = "forma_pago_id")
    private CuentaUTE formaPago;


    public ClienteComun(String cedula, String nombre, String apellido, String numTel, String contra) {
        super(cedula, nombre, apellido, numTel, contra);
    }
}
