package moduloCliente.dominio.repositorio;

import moduloCliente.dominio.Grupo;
import moduloCliente.dominio.Reclamo;
import moduloCliente.dominio.cliente.Cliente;

import java.util.List;

import moduloCliente.dominio.MedioPago;

public interface ClienteRepositorio {
    void saveCliente(Cliente cliente);
    boolean actualizar(Cliente cliente);
    Cliente buscarCliente(String cedula);
    List<Cliente> obtenerClientes();
    boolean saveReclamo(Reclamo reclamo);
    Grupo findGroup(String grupo);
    void saveMedioPago(MedioPago medioPago); //altaMedioPago
}
