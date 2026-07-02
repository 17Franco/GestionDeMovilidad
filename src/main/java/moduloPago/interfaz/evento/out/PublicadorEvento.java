package moduloPago.interfaz.evento.out;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;


@ApplicationScoped
public class PublicadorEvento {
    @Inject
    private Event<RealizaPagoCuentaUte> pagoConCuentaUte;

    public void publicarPagoConCuentaUte() {
        pagoConCuentaUte.fire(new  RealizaPagoCuentaUte());
    }
}
