package moduloCarga.interfaz.evento.in;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import moduloCarga.aplicacion.ServicioCarga;

import moduloCarga.dominio.cliente.ClienteComun;
import moduloCarga.dominio.cliente.ClienteProfesional;
import moduloCarga.dominio.cliente.TipoProfesional;
import moduloCliente.interfaz.evento.out.ClienteNuevoClienteComun;
import moduloCliente.interfaz.evento.out.ClienteNuevoClienteProfesional;

@ApplicationScoped
public class ObserverModuloCarga {
    @Inject
    private ServicioCarga servicioCarga;

    public void accept(@Observes ClienteNuevoClienteComun event) {
        //log.infof("Evento procesado: GestionNuevoVehiculo: %s", event.toString());
        ClienteComun cli = new ClienteComun(event.getCedula(), event.getNombre(), event.getApellido(), event.getNumTel(), event.getContra());
        servicioCarga.altaCliente(cli);
    }

    public void accept(@Observes ClienteNuevoClienteProfesional event) {
        //log.infof("Evento procesado: GestionNuevoVehiculo: %s", event.toString());
        TipoProfesional tipo = TipoProfesional.valueOf(event.getTipo());
        ClienteProfesional cli = new ClienteProfesional(
                event.getCedula(),
                event.getNombre(),
                event.getApellido(),
                event.getNumTel(),
                event.getContra(),
                tipo,
                event.getPorcentajeDescuento()
        );
        servicioCarga.altaCliente(cli);
    }
}
