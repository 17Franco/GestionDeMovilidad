package moduloCliente.interfaz.evento.in;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.inject.Inject;
import jakarta.jms.MapMessage;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.transaction.Transactional;
import moduloCliente.dominio.Reclamo;
import moduloCliente.dominio.TipoReclamo;
import moduloCliente.dominio.repositorio.ClienteRepositorio;
import moduloCliente.interfaz.evento.out.PublicadorEventoCliente;
import moduloCliente.aplicacion.ClasificadorReclamos;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(
                propertyName = "destinationLookup",
                propertyValue = "java:/jms/queue/reclamos"
        ),
        @ActivationConfigProperty(
                propertyName = "destinationType",
                propertyValue = "jakarta.jms.Queue"
        )
})
public class ConsumidorMensajeReclamo implements MessageListener {

    @Inject
    private ClienteRepositorio repo;

    @Inject
    private ClasificadorReclamos clasificador;

    @Inject
    private PublicadorEventoCliente evento;

    @Override
    @Transactional
    public void onMessage(Message message) {
        try {
            MapMessage mapMessage = (MapMessage) message;

            Long idReclamo = mapMessage.getLong("idReclamo");
            String descripcion = mapMessage.getString("descripcion");

            TipoReclamo tipo = clasificador.clasificar(descripcion);

            Reclamo reclamo = repo.buscarReclamoPorID(idReclamo);
            if (reclamo == null) {
                throw new RuntimeException("No existe reclamo con id " + idReclamo);
            }

            reclamo.setTipoReclamo(tipo);

            if (tipo == TipoReclamo.NEGATIVO) {
                evento.publicarReclamoNegativo(idReclamo);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error consumiendo reclamo desde JMS", e);
        }
    }
}