package moduloPago.aplicacion.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import moduloPago.aplicacion.ServicioPago;
import moduloPago.dominio.pagoRealizado;
import moduloPago.dominio.repositorio.RepoPago;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
   public List<pagoRealizado> consultarPagos(String cedulaCliente, LocalDate fechaIni, LocalDate fechaFin){

        return repo.getPagosPorFecha(cedulaCliente,fechaFin,fechaFin);
    }
}