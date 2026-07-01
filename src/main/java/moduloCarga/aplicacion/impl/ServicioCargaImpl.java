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
import moduloCarga.interfaz.evento.out.PublicadorEvento;
import moduloPago.interfaz.local.ServicioPagoLocal;

@ApplicationScoped
public class ServicioCargaImpl implements ServicioCarga {

    @Inject
    private PublicadorEvento publicadorEvento;
    
    @Inject
    private RepoCarga repo;

    @Inject
    private FuncionalidadCargadorInterfaceMOCK cargadorMock;

    @Inject
    private ServicioPagoLocal servicioPagoLocal;

    // esta funcion es para convertir el dtoEstado en un estado valido en el modulo
    // de carga
    // aunque es lo mismo realmente el package es distinto asi que esperar cosas
    // "distintas"
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

    // Funcion interna para parsear la respuesta del DTO a Carga
    private Carga convertirDTOCarga_a_Carga(DTOCarga dto) {
        Carga cargaNueva = new Carga();
        cargaNueva.setFecha(dto.getFecha());
        cargaNueva.setHoraInicio(dto.getHoraInicio());
        // hora fin sin setear
        cargaNueva.setImporteTotal(dto.getImporteTotal());
        cargaNueva.setRecargoPorDemora(dto.getRecargoPorDemora());
        cargaNueva.setPorcentajeAvance(dto.getPorcentajeAvance());
        cargaNueva.setHoraEstimadaFin(dto.getHoraEstimadaFin()); // le sumo 2 horas a la hora de inicio
        cargaNueva.setEstado(convertirEstado(dto.getEstado()));

        return cargaNueva;
    }

    @Override
    @Transactional
    public void iniciarCarga(Cliente cli, MedioPago formaPago, Integer idCargador) {
        DTOCarga dtoCarga = cargadorMock.iniciarCarga();

        // 1. Creo la carga
        Carga cargaNueva = convertirDTOCarga_a_Carga(dtoCarga);

        // 2. Asocio la carga al cliente
        cargaNueva.setClienteAsociado(cli);

        // 3. Asocio el Cargador que me paso por id al cliente (no lo controlo porque
        // eso lo hago en la API)
        Cargador cargador = repo.getCargador(idCargador);
        cargaNueva.setCargador(cargador);

        // 4. La carga pasa a ser la actual del cliente
        cli.setCargaActual(cargaNueva);

        // 5. Busco historial
        HistorialDeCargas historial = repo.buscarHistorialPorCedula(cli.getCedula());
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
        repo.ActualizarCliente(cli);// me acavki de dar cuenta que esto tiene que se actualizar unicamente, no
                                    // persisitir, sino creo un usuario nuevo al iniciar una carga si lo hace un
                                    // usuario sin registrar
    
        publicadorEvento.publicarCargaIniciada(cargaNueva.getId());
    }

    @Override
    @Transactional
    public boolean tieneDeuda(String idCLiente){
        return servicioPagoLocal.tieneDeuda(idCLiente);
    }
    @Override
    @Transactional
    public boolean pagarDeuda(String idCLiente){
        Cliente cli = repo.buscarPorCedula(idCLiente);
        if(cli == null){
            throw new IllegalArgumentException("El cliente no existe");
        }
        //me fijo si tiene deuda
        boolean tieneDeuda =  servicioPagoLocal.tieneDeuda(idCLiente);
        if(!tieneDeuda){
            throw new IllegalArgumentException("El cliente no tiene deuda");
        }

        //si la tiene me traigo su ultima carga o sea la que tiene deuda
        Carga cargaActual = cli.getCargaActual();

        Cliente clienteConHistorial = repo.buscarConHistorialPorCedula(idCLiente);
        HistorialDeCargas historial = clienteConHistorial.getHistorialAsociado();
        MedioPago medioPago = null;

        for (ElementoHistorial elemento : historial.getHistorialCargas()) {
            if (elemento.getCarga().getId() == cargaActual.getId()) {
                medioPago = elemento.getMedioPago();
                break;
            }
        }
        if(medioPago == null){
            throw new IllegalArgumentException("No se pudo acceder al medioDePago");
        }
        
        //nesesito el numero tarjeta
        Tarjeta tarjeta = null;
        if ("Tarjeta".equals(medioPago.getTipoMedioPago())){
             tarjeta = (Tarjeta) medioPago;
        }
        if(tarjeta == null){
            throw new IllegalArgumentException("El medio de pago no es tarjeta");
        }
        return servicioPagoLocal.pagarDeuda(idCLiente,cargaActual.getId(),tarjeta.getNumero(),cargaActual.getImporteTotal());
    }

    @Override
    public Carga verCargaActual(Cliente cli) {
        return cli.getCargaActual();
    }

    
    @Override
    @Transactional
    public void verHistorico(Cliente cli, String fechaIni, String fechaFin) {
        HistorialDeCargas historial = repo.buscarHistorialPorCedula(cli.getCedula());        
        List<ElementoHistorial> listaHistorial = historial.getHistorialCargas();
        // parseo las fechas de string a LocalDate
        LocalDate fechaInicio = LocalDate.parse(fechaIni);
        LocalDate fechaFinal = LocalDate.parse(fechaFin);

        List<ElementoHistorial> listaCargasEnFecha = new ArrayList<>();
        for (ElementoHistorial elemento_aux : listaHistorial) {
            LocalDate fechaCarga = elemento_aux.getCarga().getFecha();
            if ((fechaCarga.isEqual(fechaInicio) || fechaCarga.isAfter(fechaInicio))
                    &&
                    (fechaCarga.isEqual(fechaFinal) || fechaCarga.isBefore(fechaFinal))) {
                listaCargasEnFecha.add(elemento_aux);
                System.out.print(elemento_aux);
            }
        }

    }

    @Override
    @Transactional
    public boolean finalizarCarga(Cargador cargador, Carga carga, int recargo, MedioPago formaPago) {

        if (carga == null || carga.getEstado() != EstadoCarga.ENPROGRESO) {
            throw new IllegalArgumentException("La carga no existe.");
        }
        carga.setHoraFin(LocalDateTime.now());

        carga.setEstado(EstadoCarga.TERMINADO);

        carga.setRecargoPorDemora((float) recargo);

        float importeBase = 500f;

        carga.setImporteTotal(importeBase + recargo);

        repo.actualizarCarga(carga);
        //nesesito el metodo de pago para pagar

        publicadorEvento.publicarCargaFinalizada(carga.getId());
        boolean resuPago = false;
        if(formaPago instanceof Tarjeta){
            Tarjeta tarjeta = (Tarjeta)formaPago;
            resuPago= servicioPagoLocal.pagarConTarjeta(carga.getClienteAsociado().getCedula(),carga.getId(),tarjeta.getNumero(),importeBase + recargo);
        }else{
            CuentaUTE cuentaUte = (CuentaUTE) formaPago;
            resuPago= servicioPagoLocal.pagarConCuentaUte(carga.getClienteAsociado().getCedula(),carga.getId(),cuentaUte.getNumeroCuenta(),importeBase + recargo);
        }
        return resuPago;
       // System.out.println("Carga finalizada correctamente");
    }

    @Override
    public void altaEstacion(EstacionCarga datos) {

        if (datos != null) {
            repo.registrarEstacion(datos);
        }
    }

    @Override
    @Transactional
    public void altaCargador(int estacionId, Cargador datos) {
        if (datos == null) {
            throw new IllegalArgumentException("El cargador no puede ser null");
        }
        EstacionCarga estacion = repo.buscarEstacionPorId(estacionId);
        if (estacion == null) {
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
    public void altaCliente(Cliente cli) {

        // verifico que el cliente que viene de la api no sea null
        if (cli == null) {
            throw new IllegalArgumentException("Cliente no puede ser null");
        }
        // verifico que no exista ya ese cliente
        Cliente cliente = repo.buscarPorCedula(cli.getCedula());
        if (cliente != null) {
            throw new RuntimeException("Cliente ya existe");
        }

        repo.registrarCliente(cli);

    }

    @Override
    @Transactional
    public boolean altaMedioPago(String ci, MedioPago formaPago) {
        System.out.println("=== altaMedioPago CARGA ===");
        System.out.println("CI carga = " + ci);
        System.out.println("FormaPago carga = " + formaPago);

        if (ci == null || ci.isBlank() || formaPago == null) {
            System.out.println("FALLA CARGA: ci vacio o formaPago null");
            return false;
        }

        Cliente cliente = repo.buscarPorCedula(ci);

        if (cliente == null) {
            System.out.println("FALLA CARGA: cliente no existe");
            return false;
        }

        System.out.println("Cliente carga clase = " + cliente.getClass().getName());
        System.out.println("MedioPago carga clase = " + formaPago.getClass().getName());

        if (formaPago.getFechaCreacion() == null) {
            formaPago.setFechaCreacion(java.time.LocalDate.now());
        }

        if (cliente instanceof ClienteComun clienteComun) {
            System.out.println("CARGA: Es ClienteComun");

            if (formaPago instanceof CuentaUTE cuentaUTE) {
                System.out.println("CARGA: Alta CuentaUTE");

                cuentaUTE.setCliente(clienteComun);

                repo.saveMedioPago(cuentaUTE);

                clienteComun.setFormaPago(cuentaUTE);

                boolean resu = repo.actualizar(clienteComun);
                System.out.println("CARGA: Resultado actualizar UTE = " + resu);

                return resu;
            }

            if (formaPago instanceof Tarjeta tarjeta) {
                System.out.println("CARGA: Alta Tarjeta ClienteComun");

                tarjeta.setCliente(clienteComun);

                repo.saveMedioPago(tarjeta);

                clienteComun.getTarjetas().add(tarjeta);

                boolean resu = repo.actualizar(clienteComun);
                System.out.println("CARGA: Resultado actualizar tarjeta = " + resu);

                return resu;
            }

            System.out.println("FALLA CARGA: MedioPago no valido para ClienteComun");
            return false;
        }

        if (cliente instanceof ClienteProfesional clienteProfesional) {
            System.out.println("CARGA: Es ClienteProfesional");

            if (formaPago instanceof Tarjeta tarjeta) {
                System.out.println("CARGA: Alta Tarjeta ClienteProfesional");

                tarjeta.setCliente(clienteProfesional);

                repo.saveMedioPago(tarjeta);

                clienteProfesional.getTarjetas().add(tarjeta);

                boolean resu = repo.actualizar(clienteProfesional);
                System.out.println("CARGA: Resultado actualizar tarjeta profesional = " + resu);

                return resu;
            }

            System.out.println("FALLA CARGA: ClienteProfesional no acepta CuentaUTE");
            return false;
        }

        System.out.println("FALLA CARGA: tipo de cliente no reconocido");
        return false;
    }

}
