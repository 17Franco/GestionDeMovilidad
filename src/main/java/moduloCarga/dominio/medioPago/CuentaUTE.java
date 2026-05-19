package moduloCarga.dominio.medioPago;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "MCliente_CuentUte")
public class CuentaUTE extends MedioPago {
    private String numeroCuenta;

    @Override
    public String getTipoMedioPago() {
        return "Cuenta UTE";
    }
}
