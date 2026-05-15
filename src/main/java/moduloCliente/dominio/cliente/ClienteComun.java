package moduloCliente.dominio.cliente;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
}
