package moduloCliente.interfaz.evento.out;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSException;
import jakarta.jms.MapMessage;
import jakarta.jms.Queue;


@ApplicationScoped
public class PublicadorMensajeReclamo {

    @Inject
    private JMSContext jmsContext;

    @Resource(lookup = "java:/jms/queue/reclamos")  //Busca la queue que configuré el config.cli
    private Queue reclamosQueue;

   
    public void publicarReclamo(Long id, String descripcion) {
        try {
            MapMessage mensaje = jmsContext.createMapMessage();
            mensaje.setLong("idReclamo", id);
            mensaje.setString("descripcion", descripcion);

            jmsContext.createProducer().send(reclamosQueue, mensaje);
        } catch (JMSException e) {
            throw new RuntimeException("Error publicando reclamo en JMS", e);
        }
    }
}