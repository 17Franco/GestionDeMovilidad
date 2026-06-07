package moduloCarga.aplicacion.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import FuncionalidadCargadorMOCK.aplicacion.FuncionalidadCargadorInterfaceMOCK;
import FuncionalidadCargadorMOCK.aplicacion.DTOs.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import moduloCarga.aplicacion.ServicioCarga;
import moduloCarga.dominio.Carga;
import moduloCarga.dominio.Cargador;
import moduloCarga.dominio.ElementoHistorial;
import moduloCarga.dominio.EstacionCarga;
import moduloCarga.dominio.EstadoCarga;
import moduloCarga.dominio.HistorialDeCargas;
import moduloCarga.dominio.cliente.Cliente;
import moduloCarga.dominio.cliente.ClienteComun;
import moduloCarga.dominio.cliente.ClienteProfesional;
import moduloCarga.dominio.medioPago.CuentaUTE;
import moduloCarga.dominio.medioPago.MedioPago;
import moduloCarga.dominio.medioPago.Tarjeta;
import moduloCarga.dominio.repositorio.RepoCarga;



@ApplicationScoped
public class ServicioCargaImpl implements ServicioCarga {

    @Inject
    private RepoCarga repo;

    @Inject
    private FuncionalidadCargadorInterfaceMOCK cargadorMock;
    
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
    public void iniciarCarga(Cliente cli, MedioPago formaPago, Integer idCargador) {
        DTOCarga dtoCarga = cargadorMock.iniciarCarga();

        // 1. Creo la carga
        Carga cargaNueva = convertirDTOCarga_a_Carga(dtoCarga);

        // 2. Asocio la carga al cliente
        cargaNueva.setClienteAsociado(cli);

        //3. Asocio el Cargador que me paso por id al cliente (no lo controlo porque eso lo hago en la API)
        Cargador cargador = repo.getCargador(idCargador);
        cargaNueva.setCargador(cargador);

        // 4. La carga pasa a ser la actual del cliente
        cli.setCargaActual(cargaNueva);

        // 5. Busco historial
        HistorialDeCargas historial = cli.getHistorialAsociado();

        // 6. Si no existe, lo creo
        if (historial == null) {
            historial = new HistorialDeCargas();
            historial.setClienteAsociado(cli);
            cli.setHistorialAsociado(historial);
        }

        // 7. Creo elemento historial
        ElementoHistorial elemento = new ElementoHistorial();

        // 8. Asocio carga, medio de pago e historial
        elemento.setCarga(cargaNueva);
        elemento.setMedioPago(formaPago);
        elemento.setHistorialAsociado(historial);

        // 9. Agrego el elemento al historial
        historial.getHistorialCargas().add(elemento);

        // 10. Persisto
        repo.persistirCarga(cargaNueva);
        repo.persistirOActualizarHistorial(historial);
        repo.persistirElementoHistorial(elemento);
        repo.ActualizarCliente(cli);//me acavki de dar cuenta que esto tiene que se actualizar unicamente, no persisitir, sino creo un usuario nuevo al iniciar una carga si lo hace un usuario sin registrar
}


    @Override
    public Carga verCargaActual(Cliente cli) {
        return cli.getCargaActual();
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
    public void altaCargador(int estacionId,Cargador datos) {
        if(datos == null ){
            throw new IllegalArgumentException("El cargador no puede ser null");
        }
        EstacionCarga estacion = repo.buscarEstacionPorId(estacionId);
        if(estacion == null){
            throw new IllegalArgumentException("La estacion no puede ser null");
        }
        datos.setEstacionCarga(estacion);
        estacion.getCargadores().add(datos);
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
    @Transactional
    public void altaCliente(Cliente cli){

        //verifico que el cliente que viene de la api no sea null
        if(cli == null){
            throw new IllegalArgumentException("Cliente no puede ser null");
        }
        //verifico que no exista ya ese cliente
        Cliente  cliente = repo.buscarPorCedula(cli.getCedula());
        if(cliente != null){
            throw new RuntimeException("Cliente ya existe");
        }

        repo.registrarCliente(cli);

    }

    @Override
    public boolean altaMedioPago(String ci, MedioPago formaPago) {
        if (ci == null || ci.isBlank() || formaPago == null) {
            return false;
        }

        Cliente cliente = repo.buscarPorCedula(ci);
        if (cliente == null) {
            return false;
        }

        if (cliente instanceof ClienteComun clienteComun) {
            //cliente comun puede tener una cuentaUte y muchas tarjetas
            if (formaPago instanceof CuentaUTE cuentaUTE) {
                cuentaUTE.setCliente(clienteComun);
                clienteComun.setFormaPago(cuentaUTE);

                return repo.actualizar(clienteComun);
            }
            return false;
        }

        if (cliente instanceof ClienteProfesional clienteProfesional) {
            //cliente Profesional solo puede tener Tarjetas
            if (formaPago instanceof Tarjeta tarjeta) {
                clienteProfesional.getTarjetas().add(tarjeta);

                return repo.actualizar(clienteProfesional);
            }

        }

        return false;
    }


}