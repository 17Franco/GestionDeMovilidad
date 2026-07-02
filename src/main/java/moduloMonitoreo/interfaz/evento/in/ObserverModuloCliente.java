package moduloMonitoreo.interfaz.evento.in;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import moduloCliente.interfaz.evento.out.ReclamoNegativoEtiquetado;
import moduloMonitoreo.infraestructura.RegistradorDeMetricas;

@ApplicationScoped
public class ObserverModuloCliente {

    @Inject
    private RegistradorDeMetricas registrador;

    public void accept(@Observes ReclamoNegativoEtiquetado evento) {
        registrador.registrarReclamoNegativo();
    }
}