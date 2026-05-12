package moduloPago.aplicacion;

import moduloCliente.dominio.MedioPago;
import moduloCliente.dominio.cliente.Cliente;

import java.time.LocalDate;

public interface ServicioPago {

    void pagarCarga(Cliente cliente, float importe, MedioPago medioPago);

    void consultarPagos(Cliente cliente, LocalDate fechaIni, LocalDate fechaFin);
}
