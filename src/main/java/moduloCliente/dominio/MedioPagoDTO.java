package moduloCliente.dominio;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moduloCliente.dominio.TipoTarjeta;

@Getter
@Setter
@NoArgsConstructor
public class MedioPagoDTO {

    private String tipoMedioPago;

    // Cuenta UTE
    private String numeroCuenta;

    // Tarjeta
    private String numero;
    private LocalDate fechaVencimiento;
    private String digitoVerificacion;
    private TipoTarjeta tipoTarjeta;
}