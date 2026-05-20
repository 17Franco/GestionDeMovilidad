package moduloCarga.dominio.medioPago;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity (name = "CuentaUTE_Carga")
@Getter
@Setter
@NoArgsConstructor
@Table(name = "MCarga_CuentUte")
public class CuentaUTE extends MedioPago {
    private String numeroCuenta;

    @Override
    public String getTipoMedioPago() {
        return "Cuenta UTE";
    }
}
