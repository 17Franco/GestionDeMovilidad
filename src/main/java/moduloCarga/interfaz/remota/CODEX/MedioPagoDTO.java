/*package moduloCarga.interfaz.remota.CODEX;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moduloCarga.dominio.medioPago.CuentaUTE;
import moduloCarga.dominio.medioPago.MedioPago;
import moduloCarga.dominio.medioPago.Tarjeta;
import moduloCarga.dominio.medioPago.TipoTarjeta;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class MedioPagoDTOCODEX {
    private int id;
    private LocalDate fechaCreacion;
    private String tipoMedioPago;
    private String numeroCuenta;
    private String numero;
    private LocalDate fechaVencimiento;
    private String digitoVerificacion;
    private TipoTarjeta tipoTarjeta;

    public MedioPago buildMedioPago() {
        if ("CUENTA_UTE".equals(tipoMedioPago)) {
            CuentaUTE cuentaUTE = new CuentaUTE();
            cuentaUTE.setId(id);
            cuentaUTE.setFechaCreacion(fechaCreacion);
            cuentaUTE.setNumeroCuenta(numeroCuenta);
            return cuentaUTE;
        }
        if ("TARJETA".equals(tipoMedioPago)) {
            Tarjeta tarjeta = new Tarjeta();
            tarjeta.setId(id);
            tarjeta.setFechaCreacion(fechaCreacion);
            tarjeta.setNumero(numero);
            tarjeta.setFechaVencimiento(fechaVencimiento);
            tarjeta.setDigitoVerificacion(digitoVerificacion);
            tarjeta.setTipo(tipoTarjeta);
            return tarjeta;
        }
        throw new IllegalArgumentException("Tipo de medio de pago invalido");
    }
}
*/