package moduloPago.aplicacion;

import moduloPago.dominio.Pago;

import java.time.LocalDate;
import java.util.List;

public interface ServicioPago {

    boolean pagarConTarjeta(String clienteId,int idCarga, String numeroTarjeta, float monto);

    boolean pagarConCuentUte(String clienteId,int idCarga, String numeroCuenta, float monto);

    boolean tieneDeuda(String clienteId);

    boolean pagarDeuda(String cedulaCliente,int idCarga,String numeroTarjeta, float monto);

    //public boolean pagarConTarjeta(String cedulaCliente, String numeroTarjeta, float monto);

    List<Pago> consultarPagos(String cedulaCliente, LocalDate fechaIni, LocalDate fechaFin);
}