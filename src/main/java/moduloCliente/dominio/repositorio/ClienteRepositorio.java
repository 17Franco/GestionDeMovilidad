package moduloCliente.dominio.repositorio;

import moduloCliente.dominio.Reclamo;
import moduloCliente.dominio.cliente.Cliente;

import java.util.List;

public interface ClienteRepositorio {
    boolean saveCliente(Cliente cliente);
    boolean actualizar(Cliente cliente);
    Cliente buscarCliente(String cedula);
    List<Cliente> allcliente();
    boolean saveReclamo(Reclamo reclamo);

}