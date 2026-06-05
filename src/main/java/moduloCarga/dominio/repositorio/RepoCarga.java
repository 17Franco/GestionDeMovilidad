package moduloCarga.dominio.repositorio;


import moduloCarga.dominio.Carga;
import moduloCarga.dominio.Cargador;
import moduloCarga.dominio.ElementoHistorial;
import moduloCarga.dominio.EstacionCarga;
import moduloCarga.dominio.HistorialDeCargas;
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

    void persistirCarga(Carga cargaNueva);
    void persistirOActualizarHistorial(HistorialDeCargas historial);
    void persistirElementoHistorial(ElementoHistorial elemento);
    void ActualizarCliente(Cliente cli);

}
