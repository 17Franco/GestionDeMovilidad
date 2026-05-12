package moduloCliente.aplicacion.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import moduloCliente.aplicacion.ServicioCliente;
import moduloCliente.dominio.MedioPago;
import moduloCliente.dominio.cliente.Cliente;
import moduloCliente.dominio.repositorio.ClienteRepositorio;

@ApplicationScoped
public class ServicioClienteImpl implements ServicioCliente {

    @Inject
    private ClienteRepositorio repo;

    public boolean registrarCliente(Cliente cliente){
        return false;
    }

    public boolean altaMedioPago(String ci, MedioPago formaPago){
        return  false;
    }

    public void obtenerClientes(){

    }
    public void realizarReclamo(){}
}
