package moduloCliente.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moduloCliente.dominio.cliente.ClienteComun;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "MCliente_CuentaUte")
public class CuentaUTE extends MedioPago {
    private String numeroCuenta;

    @OneToOne(mappedBy = "formaPago")
    private ClienteComun cliente;
}
