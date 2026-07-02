package moduloMonitoreo.interfaz.evento.in;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import moduloCarga.interfaz.evento.out.CargaFinalizada;
import moduloCarga.interfaz.evento.out.CargaIniciada;
import moduloMonitoreo.infraestructura.RegistradorDeMetricas;

@ApplicationScoped
public class ObserverModuloCarga {
    @Inject
    private RegistradorDeMetricas registrador;

    public void accept(@Observes CargaIniciada evento) {
        registrador.incrementarCargasActivas();
    }

    public void accept(@Observes CargaFinalizada evento) {
        // Decrementa las que están en curso (Gauges)
        registrador.decrementarCargasActivas();
        
        // NUEVO: Incrementa el contador acumulativo usando el idCarga del record
        if (evento != null) {
            registrador.registrarCargaRealizada(evento.idCarga());
        }
    }
}