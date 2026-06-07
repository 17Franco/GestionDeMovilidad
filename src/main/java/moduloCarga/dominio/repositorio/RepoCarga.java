package moduloCarga.dominio.repositorio;


import moduloCarga.dominio.Cargador;
import moduloCarga.dominio.EstacionCarga;
import moduloCarga.dominio.cliente.Cliente;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface RepoCarga {
  
    //void guardarEstacion(EstacionCarga estacion);
    //void guardarCargador(Cargador cargador);

    void registrarEstacion(EstacionCarga estacion);

    void registrarCargador(Cargador cargador);

    EstacionCarga buscarEstacionPorId(int estacionId);

    List<EstacionCarga> obtenerEstaciones();

    Cliente buscarPorCedula(String cedula);

    boolean actualizar(Cliente cliente);

    void registrarCliente(Cliente cli);
}
