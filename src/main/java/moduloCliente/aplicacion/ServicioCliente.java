package moduloCliente.aplicacion;

import moduloCliente.dominio.MedioPago;
import moduloCliente.dominio.Reclamo;
import moduloCliente.dominio.cliente.Cliente;

import java.util.List;

public interface ServicioCliente {

    void registrarCliente(Cliente cliente);
    boolean altaMedioPago(String ci, MedioPago formaPago);
    List<Cliente> obtenerClientes();
    Reclamo realizarReclamo(String asunto, String descripcion, String ci);
    List<Reclamo> obtenerReclamos(String ci);
}
