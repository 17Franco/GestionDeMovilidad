package moduloCarga.aplicacion.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import CargadorMock.aplicacion.CargadorInterfaceMOCK;
//puedo importar los DTO del Cargador y sigue estando desacoplado porque es una respuesta de la interface
import CargadorMock.aplicacion.DTOs.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import moduloCarga.aplicacion.ServicioCarga;
import moduloCarga.dominio.Carga;
import moduloCarga.dominio.Cargador;
import moduloCarga.dominio.ElementoHistorial;
import moduloCarga.dominio.EstacionCarga;
import moduloCarga.dominio.EstadoCarga;
import moduloCarga.dominio.HistorialDeCargas;
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
    public void iniciarCarga(Cliente cli, MedioPago formaPago) {
        DTOCarga DTOCarga = cargadorMock.iniciarCarga();
        Carga cargaNueva = new Carga();
        cargaNueva = convertirDTOCarga_a_Carga(DTOCarga);
        cli.setCargaActual(cargaNueva);
        if (cli.getHistorialAsociado() == null){
            HistorialDeCargas nuevoHistorial = new HistorialDeCargas();
            nuevoHistorial.setClienteAsociado(cli);
            nuevoHistorial.agregarCarga(cargaNueva , formaPago);
            cli.setHistorialAsociado(nuevoHistorial);
        }
        else{
            HistorialDeCargas historial = cli.getHistorialAsociado();
            if (historial.getClienteAsociado() == null){
                historial.setClienteAsociado(cli);
            }
            historial.agregarCarga(cargaNueva, formaPago);
        }
    }


    @Override
    public void verCargaActual(Cliente cli) {
        System.out.print(cli.getCargaActual());
    }


    @Override
    public void verHistorico(Cliente cli, String fechaIni, String fechaFin) {
        HistorialDeCargas historial =  cli.getHistorialAsociado();
        List<ElementoHistorial> listaHistorial = historial.getHistorialCargas();
        //parseo las fechas de string a LocalDate
        LocalDate fechaInicio = LocalDate.parse(fechaIni);
        LocalDate fechaFinal = LocalDate.parse(fechaFin);

        List<ElementoHistorial> listaCargasEnFecha = new ArrayList<>();
        for (ElementoHistorial elemento_aux : listaHistorial) {
            LocalDate fechaCarga = elemento_aux.getCarga().getFecha();
            if (
            (fechaCarga.isEqual(fechaInicio) || fechaCarga.isAfter(fechaInicio))
            &&
            (fechaCarga.isEqual(fechaFinal) || fechaCarga.isBefore(fechaFinal))
            ) {
                listaCargasEnFecha.add(elemento_aux);
                System.out.print(elemento_aux);
            }
        }

    }

    @Override
    public void finalizarCarga(Cargador cargador, Carga carga, int recargo) {

        if (carga == null) {
            return;
        }

        carga.setHoraFin(LocalDateTime.now());

        carga.setEstado(EstadoCarga.TERMINADO);

        carga.setRecargoPorDemora((float) recargo);

        float importeBase = 500f;

        carga.setImporteTotal(importeBase + recargo);

        System.out.println("Carga finalizada correctamente");
    }

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

        //verifico que el cliente que viene de la api no sea null
        if(cli == null){
            throw new IllegalArgumentException("Cliente no puede ser null");
        }
        //verifico que no exista ya ese cliente
        Cliente  cliente = repo.buscarPorCedula(cli.getCedula());
        if(cliente != null){
            throw new RuntimeException("Cliente ya existe");
        }

        return repo.registrarCliente(cli);

    }

    @Override
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