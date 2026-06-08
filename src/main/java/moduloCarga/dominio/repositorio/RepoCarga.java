package moduloCarga.dominio.repositorio;


import moduloCarga.dominio.Carga;
import moduloCarga.dominio.Cargador;
import moduloCarga.dominio.ElementoHistorial;
import moduloCarga.dominio.EstacionCarga;
import moduloCarga.dominio.HistorialDeCargas;
import moduloCarga.dominio.cliente.Cliente;
import moduloCarga.dominio.medioPago.Tarjeta;

import java.util.List;

public interface RepoCarga {


    void registrarEstacion(EstacionCarga estacion);

    void registrarCargador(Cargador cargador);

    EstacionCarga buscarEstacionPorId(int estacionId);

    List<EstacionCarga> obtenerEstaciones();

    Cliente buscarPorCedula(String cedula);

    boolean actualizar(Cliente cliente);

    void registrarCliente(Cliente cli);

    void persistirCarga(Carga cargaNueva);
    void persistirOActualizarHistorial(HistorialDeCargas historial);
    void persistirElementoHistorial(ElementoHistorial elemento);
    void ActualizarCliente(Cliente cli);

    Tarjeta buscarTarjetaClienteCI(String CedulaCliente, String numeroTarjeta);
    Cargador getCargador(Integer idCargador);
    HistorialDeCargas buscarHistorialPorCedula(String cedula);
    List<ElementoHistorial> buscarElementosHistorialPorCedula(String cedula);
}
