package moduloCliente.dominio.repositorio;

import moduloCliente.dominio.cliente.Cliente;

import java.util.List;

public interface ClienteRepositorio {
    boolean registrar(Cliente cliente);
    boolean actualizar(Cliente cliente);
    Cliente buscarPorCedula(String cedula);
    List<Cliente> obtenerTodos();
}
