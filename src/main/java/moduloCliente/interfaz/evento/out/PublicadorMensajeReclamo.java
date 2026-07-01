package moduloCliente.interfaz.evento.out;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;

@ApplicationScoped
public class PublicadorMensajeReclamo {

    @Inject
    private JMSContext jmsContext;

    @Resource(lookup = "java:/jms/queue/reclamos")  //Busca la queue que configuré el config.cli
    private Queue reclamosQueue;

    public void publicarReclamo(String textoReclamo) {
        jmsContext
                .createProducer()
                .send(reclamosQueue, textoReclamo);
    }
}