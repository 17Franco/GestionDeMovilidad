package moduloCliente.interfaz.evento.out;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import moduloCliente.dominio.CuentaUTE;
import moduloCliente.dominio.MedioPago;
import moduloCliente.dominio.Tarjeta;
import moduloCliente.dominio.cliente.Cliente;
import moduloCliente.dominio.cliente.ClienteComun;
import moduloCliente.dominio.cliente.ClienteProfesional;

@ApplicationScoped
public class PublicadorEventoCliente {

    @Inject
    private Event<ClienteNuevoClienteComun> clienteComun;

    @Inject
    private Event<ClienteNuevoClienteProfesional> clienteProfesional;

    @Inject
    private  Event<ClienteMetodoDePago> metodoPago;

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

    public void publicarEventoClienteMetodoPago(MedioPago medioPago){

        if(medioPago instanceof CuentaUTE cuenta){
            ClienteMetodoDePago evento = new ClienteMetodoDePago();
            evento.setId(cuenta.getId());
            evento.setFechaCreacion(cuenta.getFechaCreacion());
            evento.setTipoMedioPago("CUENTA_UTE");
            evento.setNumeroCuenta(cuenta.getNumeroCuenta());
            evento.setClienteCUte(cuenta.getCliente().getCedula());
            metodoPago.fire(evento);
        }else if(medioPago instanceof Tarjeta tarjeta){
            ClienteMetodoDePago evento = new ClienteMetodoDePago();
            evento.setId(tarjeta.getId());
            evento.setFechaCreacion(tarjeta.getFechaCreacion());
            evento.setTipoMedioPago("TARJETA");
            evento.setNumero(tarjeta.getNumero());
            evento.setFechaVencimiento(tarjeta.getFechaVencimiento());
            evento.setDigitoVerificacion(tarjeta.getDigitoVerificacion());
            evento.setTipo(tarjeta.getTipo().name());
            evento.setClienteTarjeta(tarjeta.getCliente().getCedula());
            metodoPago.fire(evento);
        }


    }
}
