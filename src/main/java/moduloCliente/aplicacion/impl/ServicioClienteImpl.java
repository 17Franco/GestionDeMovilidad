package moduloCliente.aplicacion.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import moduloCliente.aplicacion.ServicioCliente;
import moduloCliente.dominio.CuentaUTE;
import moduloCliente.dominio.MedioPago;
import moduloCliente.dominio.Reclamos;
import moduloCliente.dominio.cliente.Cliente;
import moduloCliente.dominio.cliente.ClienteComun;
import moduloCliente.dominio.cliente.ClienteProfesional;
import moduloCliente.dominio.repositorio.ClienteRepositorio;
import moduloCliente.interfaz.evento.out.PublicadorEventoCliente;

@ApplicationScoped
public class ServicioClienteImpl implements ServicioCliente {

    @Inject
    private ClienteRepositorio repo;

    @Inject
    private PublicadorEventoCliente evento;

    @Transactional // hace que todo el metodo sea una transacción
    public boolean registrarCliente(Cliente cliente) {
        //verifico que el cliente que viene de la api no sea null
        if(cliente == null){
            throw new IllegalArgumentException("Cliente no puede ser null");
        }
        //verifico que no exista ya ese cliente
        Cliente cli = repo.buscarPorCedula(cliente.getCedula());
        if(cli != null){
            throw new RuntimeException("Cliente ya existe");
        }
        boolean resu = repo.registrar(cliente);

        if(resu){
            if(cliente instanceof ClienteComun){
                evento.publicarEventoClienteComun(cliente);
            }else{
                evento.publicarEventoClienteProfesional(cliente);
            }
        }
        return resu;
    }

    public boolean altaMedioPago(String ci, MedioPago formaPago) {
        if (ci == null || ci.isBlank() || formaPago == null) {
            return false;
        }

        Cliente cliente = repo.buscarPorCedula(ci);
        if (cliente == null) {
            return false;
        }

        if (cliente instanceof ClienteComun clienteComun) {
            if (formaPago instanceof CuentaUTE cuentaUTE) {
                cuentaUTE.setCiCli(ci);
                clienteComun.setFormaPago(cuentaUTE);
                return repo.actualizar(clienteComun);
            }
            return false;
        }

        if (cliente instanceof ClienteProfesional clienteProfesional) {
            clienteProfesional.getMetodosPago().add(formaPago);
            return repo.actualizar(clienteProfesional);
        }

        return false;
    }

    public void obtenerClientes() {
        var clientes = repo.obtenerTodos();
        System.out.println("Clientes registrados:");
        for (Cliente cliente : clientes) {
            System.out.printf("- %s %s %s\n", cliente.getCedula(), cliente.getNombre(), cliente.getApellido(),cliente.getReclamos());
        }
    }

    public void realizarReclamo(String asunto, String descripcion,String ci) {
        //llamo a repo creo el objeto reclamo y se lo asigno
        repo.hacerReclamo(asunto,descripcion,ci);
    }
}
