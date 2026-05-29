package moduloPago.aplicacion.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import moduloPago.aplicacion.ServicioPago;
import moduloPago.dominio.pagoRealizado;
import moduloPago.dominio.repositorio.RepoPago;

import java.time.LocalDate;

@ApplicationScoped
public class ServicioPagoImpl implements ServicioPago {

    @Inject
    private RepoPago repo;

    @Override
    public void pagarCarga(
            String cedulaCliente,
            int idCarga,
            float importe,
            String medioPago
    ){

        pagoRealizado pago = new pagoRealizado();

        pago.setCedulaCliente(cedulaCliente);
        pago.setIdCarga(idCarga);
        pago.setMonto(importe);
        pago.setFecha(LocalDate.now());
        pago.setMedioPago(medioPago);

        System.out.println("Pago realizado correctamente");
    }

    @Override
    public void consultarPagos(
            String cedulaCliente,
            LocalDate fechaIni,
            LocalDate fechaFin
    ){}
}