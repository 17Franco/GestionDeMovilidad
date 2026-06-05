package moduloCliente.dominio.repositorio;

import moduloCliente.dominio.Reclamos;
import moduloCliente.dominio.cliente.Cliente;

import java.util.List;

public interface ClienteRepositorio {
    boolean registrar(Cliente cliente);
    boolean actualizar(Cliente cliente);
    Cliente buscarCliente(String cedula);
    List<Cliente> allcliente();
    boolean saveReclamo(Reclamo reclamo);
    
}
