package moduloCliente.dominio;

import java.time.LocalDate;

public class Tarjeta extends MedioPago {
    private String numero;
    private LocalDate fechaVencimiento;
    private String digitoVerificacion;
    private TipoTarjeta tipo;
}
