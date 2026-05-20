package moduloCarga.dominio.repositorio;

import moduloCarga.dominio.EstacionCarga;
import moduloCarga.dominio.cliente.Cliente;

import java.util.ArrayList;
import java.util.List;

public interface RepoCarga {
    void registrarEstacion(EstacionCarga estacion);
    List<EstacionCarga> obtenerEstaciones();
    Cliente buscarPorCedula(String cedula);
    public List<Cliente> obtenerTodos();
    boolean registrarCliente(Cliente cli);
}
