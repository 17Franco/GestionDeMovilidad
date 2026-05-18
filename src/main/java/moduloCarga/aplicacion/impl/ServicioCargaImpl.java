package moduloCarga.aplicacion.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import moduloCarga.aplicacion.ServicioCarga;
import moduloCarga.dominio.Carga;
import moduloCarga.dominio.Cargador;
import moduloCarga.dominio.EstacionCarga;
import moduloCarga.dominio.repositorio.RepoCarga;
import moduloCliente.dominio.MedioPago;
import moduloCliente.dominio.cliente.Cliente;
import moduloPago.dominio.repositorio.RepoPago;

@ApplicationScoped
public class ServicioCargaImpl implements ServicioCarga {

    @Inject
    private RepoCarga repo;

    public void iniciarCarga(Cliente cli, MedioPago formaPago) {}

    public void verCargaActual(Cliente cli) {}

    public void verHistorico(Cliente cli, String fechaIni, String fechaFin) {}

    public void finalizarCarga(Cargador cargador, Carga carga, int recargo) {}

    public void altaEstacion(EstacionCarga datos) {
        if (datos != null) {
            repo.registrarEstacion(datos);
        }
    }

    public void altaCargador(Cargador datos) {}

    public void obtenerEstaciones() {
        var estaciones = repo.obtenerEstaciones();
        System.out.println("Estaciones de carga disponibles:");
        for (EstacionCarga estacion : estaciones) {
            System.out.printf("- %s en %s\n", estacion.getDescripcion(), estacion.getCalle());
        }
    }
}
