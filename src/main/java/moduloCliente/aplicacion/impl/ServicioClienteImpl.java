package moduloCliente.aplicacion.impl;

import infraestructura.seguridad.HashFunctionUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import moduloCliente.aplicacion.ServicioCliente;
import moduloCliente.dominio.*;
import moduloCliente.dominio.cliente.Cliente;
import moduloCliente.dominio.cliente.ClienteComun;
import moduloCliente.dominio.cliente.ClienteProfesional;
import moduloCliente.dominio.repositorio.ClienteRepositorio;
import moduloCliente.exepciones.ClienteInvalidoException;
import moduloCliente.exepciones.ClienteNoExisteException;
import moduloCliente.exepciones.ClienteYaExisteException;
import moduloCliente.exepciones.GrupoNoExisteException;
import moduloCliente.interfaz.evento.out.PublicadorEventoCliente;

import moduloCliente.interfaz.evento.out.PublicadorMensajeReclamo;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ServicioClienteImpl implements ServicioCliente {

    @Inject
    private PublicadorMensajeReclamo publicadorMensajeReclamo;

    @Inject
    private ClienteRepositorio repo;

    @Inject
    private PublicadorEventoCliente evento;


    @Transactional // hace que todo el metodo sea una transacción
    @Override
    public void registrarCliente(Cliente cliente) {
        // verifico que el cliente que viene de la api no sea null
        if (cliente == null) {
            throw new ClienteInvalidoException("Cliente no puede ser null");
        }
        // verifico que no exista ya ese cliente
        Cliente cli = repo.buscarCliente(cliente.getCedula());
        if (cli != null) {
            throw new ClienteYaExisteException("Cliente ya existe");
        }
        Grupo g = repo.findGroup("appMovil");
        if (g == null) {
            throw new GrupoNoExisteException("Grupo no existe");
        }
        if (cliente.getGrupos() == null) {
            cliente.setGrupos(new ArrayList<>());
        }
        cliente.getGrupos().add(g);
        String hash = HashFunctionUtil.convertToHas(cliente.getContra());// genero pass hasheada
        cliente.setContra(hash);
        repo.saveCliente(cliente);

        if (cliente instanceof ClienteComun) {
            evento.publicarEventoClienteComun(cliente);
        } else {
            evento.publicarEventoClienteProfesional(cliente);
        }

    }

@Override
@Transactional
public boolean altaMedioPago(String ci, MedioPago formaPago) {
    System.out.println("=== altaMedioPago ===");
    System.out.println("CI = " + ci);
    System.out.println("formaPago = " + formaPago);

    if (ci == null || ci.isBlank() || formaPago == null) {
        System.out.println("FALLA: ci vacio o formaPago null");
        return false;
    }

    Cliente cliente = repo.buscarCliente(ci);

    if (cliente == null) {
        System.out.println("FALLA: cliente no existe");
        return false;
    }

    System.out.println("Cliente clase = " + cliente.getClass().getName());
    System.out.println("MedioPago clase = " + formaPago.getClass().getName());

    if (formaPago.getFechaCreacion() == null) {
        formaPago.setFechaCreacion(java.time.LocalDate.now());
    }

    if (cliente instanceof ClienteComun clienteComun) {
        System.out.println("Es ClienteComun");

        if (formaPago instanceof CuentaUTE cuentaUTE) {
            System.out.println("Alta CuentaUTE");

            cuentaUTE.setCliente(clienteComun);
            repo.saveMedioPago(cuentaUTE);
            clienteComun.setFormaPago(cuentaUTE);

            boolean resu = repo.actualizar(clienteComun);
            System.out.println("Resultado actualizar = " + resu);

            if (resu) {
                evento.publicarEventoClienteMetodoPago(cuentaUTE);
            }

            return resu;
        }

        if (formaPago instanceof Tarjeta tarjeta) {
            System.out.println("Alta Tarjeta ClienteComun");

            tarjeta.setCliente(clienteComun);
            repo.saveMedioPago(tarjeta);
            clienteComun.getTarjetas().add(tarjeta);

            boolean resu = repo.actualizar(clienteComun);
            System.out.println("Resultado actualizar = " + resu);

            if (resu) {
                evento.publicarEventoClienteMetodoPago(tarjeta);
            }

            return resu;
        }

        System.out.println("FALLA: MedioPago no valido para ClienteComun");
        return false;
    }

    if (cliente instanceof ClienteProfesional clienteProfesional) {
        System.out.println("Es ClienteProfesional");

        if (formaPago instanceof Tarjeta tarjeta) {
            System.out.println("Alta Tarjeta ClienteProfesional");

            tarjeta.setCliente(clienteProfesional);
            repo.saveMedioPago(tarjeta);
            clienteProfesional.getTarjetas().add(tarjeta);

            boolean resu = repo.actualizar(clienteProfesional);
            System.out.println("Resultado actualizar = " + resu);

            if (resu) {
                evento.publicarEventoClienteMetodoPago(tarjeta);
            }

            return resu;
        }

        System.out.println("FALLA: ClienteProfesional no acepta CuentaUTE");
        return false;
    }

    System.out.println("FALLA: tipo de cliente no reconocido");
    return false;
}

    public List<Cliente> obtenerClientes() {
        return repo.obtenerClientes();
    }

    @Override
    @Transactional
    public Reclamo realizarReclamo(String asunto, String descripcion, String ci) {

        // verifico ci si existe en el cliente
        Cliente c = repo.buscarCliente(ci);
        if (c == null) {
            throw new ClienteNoExisteException("Cliente no existe");
        }
        Reclamo reclamo = null;
        // creamos reclamo y mandamos a guardar persistir
        reclamo = new Reclamo(asunto, descripcion, c);
        c.getReclamos().add(reclamo);
        repo.saveReclamo(reclamo);
        //Publico el reclamo en la queue
        publicadorMensajeReclamo.publicarReclamo(reclamo.getId(), descripcion);
        // llamo a repo creo el objeto reclamo y se lo asigno
        return reclamo;
    }

    @Override
    @Transactional
    public List<Reclamo> obtenerReclamos(String ci) {
        return new ArrayList<>();
    }

    @Override
    public Reclamo buscarReclamoPorID(Long idReclamo){
        return repo.buscarReclamoPorID(idReclamo);
    }

    @Override
    public List<Reclamo> mostrarReclamos(){
        return repo.mostrarReclamos();
    }

}
