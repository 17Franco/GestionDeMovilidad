package moduloCliente.dominio.cliente;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moduloCliente.dominio.CuentaUTE;
import moduloCliente.dominio.Reclamos;

import java.util.List;

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
