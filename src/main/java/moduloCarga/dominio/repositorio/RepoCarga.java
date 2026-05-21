package moduloCarga.dominio.repositorio;


import moduloCarga.dominio.Cargador;
import moduloCarga.dominio.EstacionCarga;
import moduloCarga.dominio.cliente.Cliente;
import java.util.ArrayList;
import java.util.List;

public interface RepoCarga {
  
    void guardarEstacion(EstacionCarga estacion);
    void guardarCargador(Cargador cargador);

    void registrarEstacion(EstacionCarga estacion);

    void registrarCargador(Cargador cargador);

    List<EstacionCarga> obtenerEstaciones();

    Cliente buscarPorCedula(String cedula);

    List<Cliente> obtenerTodos();

    boolean registrarCliente(Cliente cli);
}
