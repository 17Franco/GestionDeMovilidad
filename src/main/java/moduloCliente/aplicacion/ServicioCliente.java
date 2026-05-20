package moduloCliente.aplicacion;

import moduloCliente.dominio.MedioPago;
import moduloCliente.dominio.cliente.Cliente;

public interface ServicioCliente {
    //FALTARIA VER COMO PASAMOS DATOS CON DTO? o el cliente mismo y aca smplemente controlamos
    //y persistimos
    boolean registrarCliente(Cliente cliente);
    boolean altaMedioPago(String ci, MedioPago formaPago);
    void obtenerClientes();
    void realizarReclamo(String asunto, String descripcion,String ci);
}
