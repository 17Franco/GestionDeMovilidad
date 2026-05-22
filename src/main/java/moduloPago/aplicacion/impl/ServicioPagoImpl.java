package moduloPago.aplicacion.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import moduloCliente.dominio.MedioPago;
import moduloCliente.dominio.cliente.Cliente;
import moduloPago.aplicacion.ServicioPago;
import moduloPago.dominio.repositorio.RepoPago;

import java.time.LocalDate;

@ApplicationScoped
public class ServicioPagoImpl implements ServicioPago {

    @Inject
    private RepoPago repo;

    @Override
    public void pagarCarga(Cliente cliente, float importe, MedioPago medioPago){

        if (cliente == null || medioPago == null) {
            return;
        }

        System.out.println("Procesando pago...");

        System.out.println(
                "Cliente: " +
                cliente.getNombre() +
                " " +
                cliente.getApellido()
        );

        System.out.println("Importe total: $" + importe);

        System.out.println(
                "Medio de pago utilizado: " +
                medioPago.getClass().getSimpleName()
        );

        System.out.println("Pago realizado correctamente");
    }

    @Override
    public void consultarPagos(Cliente cliente, LocalDate fechaIni, LocalDate fechaFin){}
}