package moduloCarga.aplicacion.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;

import CargadorMock.aplicacion.CargadorInterfaceMOCK;
//puedo importar los DTO del Cargador y sigue estando desacoplado porque es una respuesta de la interface
import CargadorMock.aplicacion.DTOs.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import moduloCarga.aplicacion.ServicioCarga;
import moduloCarga.dominio.Carga;
import moduloCarga.dominio.Cargador;
import moduloCarga.dominio.EstacionCarga;
import moduloCarga.dominio.EstadoCarga;
import moduloCarga.dominio.cliente.Cliente;
import moduloCarga.dominio.medioPago.MedioPago;
import moduloCarga.dominio.repositorio.RepoCarga;

@ApplicationScoped
public class ServicioCargaImpl implements ServicioCarga {

    @Inject
    private RepoCarga repo;

    @Inject
    private CargadorInterfaceMOCK cargadorMock;
    
    //esta funcion es para convertir el dtoEstado en un estado valido en el modulo de carga
    //aunque es lo mismo realmente el package es distinto asi que esperar cosas "distintas"
    private EstadoCarga convertirEstado(DTOEstadoCarga estadoDto) {
        switch (estadoDto) {
            case ENPROGRESO:
                return EstadoCarga.ENPROGRESO;
            case TERMINADO:
                return EstadoCarga.TERMINADO;
            default:
                return null;
        }
}
    //Funcion interna para parsear la respuesta del DTO a Carga
    private Carga convertirDTOCarga_a_Carga(DTOCarga dto){
        Carga cargaNueva = new Carga();
        cargaNueva.setFecha(dto.getFecha());
        cargaNueva.setHoraInicio(dto.getHoraInicio());
        //hora fin sin setear
        cargaNueva.setImporteTotal(dto.getImporteTotal());
        cargaNueva.setRecargoPorDemora(dto.getRecargoPorDemora());
        cargaNueva.setPorcentajeAvance(dto.getPorcentajeAvance());
        cargaNueva.setHoraEstimadaFin(dto.getHoraEstimadaFin()); //le sumo 2 horas a la hora de inicio
        cargaNueva.setEstado(convertirEstado(dto.getEstado()));
        
        return cargaNueva;
    }
    @Override
    public Carga iniciarCarga(Cliente cli, MedioPago formaPago) {
        //el cargador me devuleve un DTO de la carga
        DTOCarga DTOCarga = cargadorMock.iniciarCarga();
        //creo la carga nueva
        Carga cargaNueva = new Carga();
        //ahora vuelco los datos el DTO en la carga nueva con la funcion que cree arriba
        cargaNueva = convertirDTOCarga_a_Carga(DTOCarga);
        //ahora le seteo el cliente
        cli.setCargaActual(cargaNueva);
        return cargaNueva;
    }



   

    @Override
    public Carga verCargaActual(Cliente cli) {
        return cli.getCargaActual();
    }

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
    public void altaCargador(Cargador datos) {

        if (datos != null) {
            repo.registrarCargador(datos);
        }


    @Override
    public void obtenerEstaciones() {

        var estaciones = repo.obtenerEstaciones();

        System.out.println("Estaciones de carga disponibles:");

        for (EstacionCarga estacion : estaciones) {
            System.out.printf("- %s en %s\n",
                    estacion.getDescripcion(),
                    estacion.getCalle());
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
            System.out.printf("- %s %s %s\n",
                    cliente.getCedula(),
                    cliente.getNombre(),
                    cliente.getApellido());
        }
    }
}