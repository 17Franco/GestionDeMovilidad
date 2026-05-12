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

    public void pagarCarga(Cliente cliente, float importe, MedioPago medioPago){}

    public void consultarPagos(Cliente cliente, LocalDate fechaIni, LocalDate fechaFin){}
}
