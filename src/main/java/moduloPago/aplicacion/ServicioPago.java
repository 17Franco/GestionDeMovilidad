package moduloPago.aplicacion;

import moduloPago.dominio.pagoRealizado;

import java.time.LocalDate;
import java.util.List;

public interface ServicioPago {

    void pagarCarga(String cedulaCliente, int idCarga, float importe, String medioPago);

    boolean pagarConTarjetaServicioExterno(String clienteId, String numeroTarjeta, float monto);


    List<pagoRealizado> consultarPagos(String cedulaCliente, LocalDate fechaIni, LocalDate fechaFin);
}