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
        //el cargador me devuleve un DTO de la carga
        DTOCarga DTOCarga = cargadorMock.iniciarCarga();
        //creo la carga nueva
        Carga cargaNueva = new Carga();
        //ahora vuelco los datos el DTO en la carga nueva con la funcion que cree arriba
        cargaNueva = convertirDTOCarga_a_Carga(DTOCarga);
        //ahora le seteo el cliente
        cli.setCargaActual(cargaNueva);
        //pido el Historial de cargas del cliente
        //si el historial asociado es null es porque es la primer carga que genero, asi que tambien le tengo que generar un historial
        if (cli.getHistorialAsociado() == null){
            HistorialDeCargas nuevoHistorial = new HistorialDeCargas();
            //tambien tengo que hacer la asociacion inversa, asociarle al historial el cliente
            nuevoHistorial.setClienteAsociado(cli);
            //le agrego la carga nueva al historial que no existía
            nuevoHistorial.agregarCarga(cargaNueva , formaPago);
            cli.setHistorialAsociado(nuevoHistorial);
        }
        else{
            HistorialDeCargas historial = cli.getHistorialAsociado();
            //si es la primera carga que genero no tendra cliente asociado en el historial asi que lo agrego, si ya hay cliente no
            if (historial.getClienteAsociado() == null){
                historial.setClienteAsociado(cli);
            }
            //agrego la carga nueva al historial
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
        List<ElementoHistorial> listaHistorial = historial.getHisorialCargas();
        //parseo las fechas de string a LocalDate
        LocalDate fechaInicio = LocalDate.parse(fechaIni);
        LocalDate fechaFinal = LocalDate.parse(fechaFin);

        //creo la lista que contendrá las cargas que esten entre fechaIni y fechaFin
        List<ElementoHistorial> listaCargasEnFecha = new ArrayList<>();
        //busco las cargas que coincidan y las agrego
        for (ElementoHistorial elemento_aux : listaHistorial) {
            //obtengo la fecha de la carga del elemento de historial
            LocalDate fechaCarga = elemento_aux.getCarga().getFecha();
            if (
            (fechaCarga.isEqual(fechaInicio) || fechaCarga.isAfter(fechaInicio))
            &&
            (fechaCarga.isEqual(fechaFinal) || fechaCarga.isBefore(fechaFinal))
            ) {
                listaCargasEnFecha.add(elemento_aux);
                //muesto el elemento con la funcion toString, esto muestra los datos de la carga y el medio de pago utilizado
                System.out.print(elemento_aux);
            }
        }

    }

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