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

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ServicioClienteImpl implements ServicioCliente {

    @Inject
    private ClienteRepositorio repo;

    @Inject
    private PublicadorEventoCliente evento;

    @Transactional // hace que todo el metodo sea una transacción
    @Override
    public void registrarCliente(Cliente cliente) {
        //verifico que el cliente que viene de la api no sea null
        if(cliente == null){
            throw new ClienteInvalidoException("Cliente no puede ser null");
        }
        //verifico que no exista ya ese cliente
        Cliente cli = repo.buscarCliente(cliente.getCedula());
        if(cli != null){
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

        if(cliente instanceof ClienteComun){
            evento.publicarEventoClienteComun(cliente);
        }else{
            evento.publicarEventoClienteProfesional(cliente);
        }

    }

    public boolean altaMedioPago(String ci, MedioPago formaPago) {
        if (ci == null || ci.isBlank() || formaPago == null) {
            return false;
        }

        Cliente cliente = repo.buscarCliente(ci);
        if (cliente == null) {
            return false;
        }

        if (cliente instanceof ClienteComun clienteComun) {
            //cliente comun puede tener una cuentaUte y muchas tarjetas
            if (formaPago instanceof CuentaUTE cuentaUTE) {
                cuentaUTE.setCliente(clienteComun);
                clienteComun.setFormaPago(cuentaUTE);
                boolean resu = repo.actualizar(clienteComun);
                if (resu){
                    evento.publicarEventoClienteMetodoPago(cuentaUTE);
                }
                return resu;
            }
            return false;
        }

        if (cliente instanceof ClienteProfesional clienteProfesional) {
            //cliente Profesional solo puede tener Tarjetas
            if (formaPago instanceof Tarjeta tarjeta) {
                clienteProfesional.getTarjetas().add(tarjeta);
                boolean resu = repo.actualizar(clienteProfesional);
                if(resu){
                    evento.publicarEventoClienteMetodoPago(tarjeta);
                }
                return resu;
            }

        }

        return false;
    }

    public List<Cliente> obtenerClientes() {
        return repo.obtenerClientes();
    }

    @Override
    @Transactional
    public Reclamo realizarReclamo(String asunto, String descripcion, String ci) {

        //verifico ci si existe en el cliente
        Cliente c = repo.buscarCliente(ci);
        if( c == null){
            throw new ClienteNoExisteException("Cliente no existe");
        }
        Reclamo reclamo = null;
        //creamos reclamo y mandamos a guardar persistir
        reclamo = new Reclamo(asunto,descripcion,c);
        c.getReclamos().add(reclamo);
        repo.saveReclamo(reclamo);
        //llamo a repo creo el objeto reclamo y se lo asigno
       return  reclamo;
    }

    @Override
    @Transactional
    public List<Reclamo> obtenerReclamos(String ci){
        return new ArrayList<>();
    }
}
