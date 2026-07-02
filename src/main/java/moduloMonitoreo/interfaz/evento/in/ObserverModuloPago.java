package moduloMonitoreo.interfaz.evento.in;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import moduloMonitoreo.infraestructura.RegistradorDeMetricas;
import moduloPago.interfaz.evento.out.RealizaPagoCuentaUte;

@ApplicationScoped
public class ObserverModuloPago {
    @Inject
    private RegistradorDeMetricas registrador;

    public void accept(@Observes RealizaPagoCuentaUte evento) {
        registrador.registrarPagoConCuentaUte();
    }
}
