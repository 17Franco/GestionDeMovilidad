package moduloCliente.aplicacion.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import moduloCliente.aplicacion.ServicioCliente;
import moduloCliente.dominio.CuentaUTE;
import moduloCliente.dominio.MedioPago;
import moduloCliente.dominio.cliente.Cliente;
import moduloCliente.dominio.cliente.ClienteComun;
import moduloCliente.dominio.cliente.ClienteProfesional;
import moduloCliente.dominio.repositorio.ClienteRepositorio;

@ApplicationScoped
public class ServicioClienteImpl implements ServicioCliente {

    @Inject
    private ClienteRepositorio repo;

    public boolean registrarCliente(Cliente cliente) {
        return repo.registrar(cliente);
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
            System.out.printf("- %s %s %s\n", cliente.getCedula(), cliente.getNombre(), cliente.getApellido());
        }
    }

    public void realizarReclamo() {
    }
}
