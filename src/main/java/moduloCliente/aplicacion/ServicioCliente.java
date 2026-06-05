package moduloCliente.aplicacion;

import moduloCliente.dominio.MedioPago;
import moduloCliente.dominio.Reclamo;
import moduloCliente.dominio.cliente.Cliente;

import java.util.List;

public interface ServicioCliente {
    //FALTARIA VER COMO PASAMOS DATOS CON DTO? o el cliente mismo y aca smplemente controlamos
    //y persistimos
    void registrarCliente(Cliente cliente);
    boolean altaMedioPago(String ci, MedioPago formaPago);
    void obtenerClientes();
    Reclamo realizarReclamo(String asunto, String descripcion, String ci);
    List<Reclamo> obtenerReclamos(String ci);
}
