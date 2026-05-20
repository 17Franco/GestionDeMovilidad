package moduloCarga.aplicacion.impl;

import CargadorMock.aplicacion.CargadorInterfaceMOCK;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import moduloCarga.aplicacion.ServicioCarga;
import moduloCarga.dominio.Carga;
import moduloCarga.dominio.Cargador;
import moduloCarga.dominio.EstacionCarga;
import moduloCarga.dominio.cliente.Cliente;
import moduloCarga.dominio.medioPago.MedioPago;
import moduloCarga.dominio.repositorio.RepoCarga;

@ApplicationScoped
public class ServicioCargaImpl implements ServicioCarga {

    @Inject
    private RepoCarga repo;

    @Inject
    private CargadorInterfaceMOCK cargadorMock;

    @Override
    public void iniciarCarga(Cliente cli, MedioPago formaPago) {
        // Envío un evento o una interfaz mockeada del cargador.
        // Espero una respuesta del cargador, así que uso la interfaz.
        boolean respuestaCargador = cargadorMock.iniciarCarga();

        if (respuestaCargador) {
            System.out.print("El cliente " + cli.getNombre() + " " + cli.getApellido()
                    + " inició correctamente la carga con " + formaPago.getTipoMedioPago());
        } else {
            System.out.print("No se pudo inicializar la carga correctamente");
        }
    }

    @Override
    public void verCargaActual(Cliente cli) {}

    @Override
    public void verHistorico(Cliente cli, String fechaIni, String fechaFin) {}

    @Override
    public void finalizarCarga(Cargador cargador, Carga carga, int recargo) {}

    @Override
    public void altaEstacion(EstacionCarga datos) {
        if (datos != null) {
            repo.registrarEstacion(datos);
        }
    }

    @Override
    public void altaCargador(Cargador datos) {}

    @Override
    public void obtenerEstaciones() {
        var estaciones = repo.obtenerEstaciones();

        System.out.println("Estaciones de carga disponibles:");
        for (EstacionCarga estacion : estaciones) {
            System.out.printf("- %s en %s\n", estacion.getDescripcion(), estacion.getCalle());
        }
    }

    @Override
    public boolean altaCliente(Cliente cli){

        return repo.registrarCliente(cli);
    }

    public void obtenerClientes() {
        var clientes = repo.obtenerTodos();
        System.out.println("Clientes registrados Modulo Carga:");
        for (Cliente cliente : clientes) {
            System.out.printf("- %s %s %s\n", cliente.getCedula(), cliente.getNombre(), cliente.getApellido());
        }
    }
}