package moduloPago.aplicacion;

import java.time.LocalDate;

public interface ServicioPago {

    void pagarCarga(
            String cedulaCliente,
            int idCarga,
            float importe,
            String medioPago
    );

    void consultarPagos(
            String cedulaCliente,
            LocalDate fechaIni,
            LocalDate fechaFin
    );
}