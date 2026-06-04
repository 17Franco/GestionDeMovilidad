package moduloCarga.dominio.medioPago;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moduloCarga.dominio.cliente.ClienteComun;


@Entity (name = "CuentaUTE_Carga")
@Getter
@Setter
@NoArgsConstructor
@Table(name = "MCarga_CuentUte")
public class CuentaUTE extends MedioPago {
    private String numeroCuenta;

    @OneToOne(mappedBy = "formaPago")
    private ClienteComun cliente;

    @Override
    public String getTipoMedioPago() {
        return "Cuenta UTE";
    }

    @Override
    public String toString(){
        return "Cuenta UTE";
    }
}
