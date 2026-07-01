package moduloPago.interfaz.local;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import moduloPago.aplicacion.ServicioPago;

//esto es otra forma de comunicar modulos en este caso
//comunicamos módulo carga con módulo pago
//usamos esto porque necesitamos respuesta
@ApplicationScoped
public class ServicioPagoLocal {
    @Inject
    private ServicioPago pagoServicio;

    public boolean pagarConTarjeta(String clienteId,int idCarga, String numeroTarjeta, float monto){
        return pagoServicio.pagarConTarjeta(clienteId,idCarga,numeroTarjeta,monto);
    }

    public boolean pagarConCuentaUte(String clienteId,int idCarga, String numeroCuenta, float monto){
        return pagoServicio.pagarConCuentUte(clienteId,idCarga,numeroCuenta,monto);
    }

    public boolean tieneDeuda(String clienteId){
        return pagoServicio.tieneDeuda(clienteId);
    }
}
