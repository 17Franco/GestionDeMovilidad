package moduloCliente.interfaz.evento.out;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import moduloCliente.dominio.cliente.Cliente;
import moduloCliente.dominio.cliente.ClienteComun;
import moduloCliente.dominio.cliente.ClienteProfesional;

@ApplicationScoped
public class PublicadorEventoCliente {

    @Inject
    private Event<ClienteNuevoClienteComun> clienteComun;

    @Inject
    private Event<ClienteNuevoClienteProfesional> clienteProfesional;

    public void publicarEventoClienteComun(Cliente cliente){
        ClienteComun comun = (ClienteComun) cliente;
        System.out.println("hasta aca llege publicador");
        ClienteNuevoClienteComun evento = new ClienteNuevoClienteComun(
                comun.getCedula(),
                comun.getNombre(),
                comun.getApellido(),
                comun.getNumTel(),
                comun.getContra()
        );
        clienteComun.fire(evento);
    }

    public void publicarEventoClienteProfesional(Cliente cliente){
        ClienteProfesional profesional =(ClienteProfesional) cliente;
        ClienteNuevoClienteProfesional evento = new ClienteNuevoClienteProfesional(
                profesional .getCedula(),
                profesional.getNombre(),
                profesional.getApellido(),
                profesional.getNumTel(),
                profesional.getContra(),
                profesional.getTipo().name(),
                profesional.getPorcentajeDescuento()
        );
        clienteProfesional.fire(evento);
    }
}
