package moduloCarga.interfaz.evento.out;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

@ApplicationScoped
public class PublicadorEvento {
    @Inject private Event<CargaIniciada> cargaIniciada;
    @Inject private Event<CargaFinalizada> cargaFinalizada;

    public void publicarCargaIniciada(int idCarga) {
        cargaIniciada.fire(new CargaIniciada(idCarga));
    }

    public void publicarCargaFinalizada(int idCarga) {
        cargaFinalizada.fire(new CargaFinalizada(idCarga));
    }
}