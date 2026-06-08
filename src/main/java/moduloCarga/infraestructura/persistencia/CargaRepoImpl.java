package moduloCarga.infraestructura.persistencia;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import moduloCarga.dominio.Carga;
import moduloCarga.dominio.Cargador;
import moduloCarga.dominio.ElementoHistorial;
import moduloCarga.dominio.EstacionCarga;
import moduloCarga.dominio.HistorialDeCargas;
import moduloCarga.dominio.cliente.Cliente;
import moduloCarga.dominio.cliente.ClienteComun;
import moduloCarga.dominio.cliente.ClienteProfesional;
import moduloCarga.dominio.medioPago.Tarjeta;
import moduloCarga.dominio.repositorio.RepoCarga;
import moduloCliente.exepciones.ClienteInvalidoException;

import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class CargaRepoImpl implements RepoCarga {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void registrarEstacion(EstacionCarga estacion) {
        if (estacion != null) {
            em.persist(estacion);
        }
    }
    @Override
    public EstacionCarga buscarEstacionPorId(int estacionId){
        return em.find(EstacionCarga.class, estacionId);
    }

    @Override
    @Transactional
    public void registrarCargador(Cargador cargador) {
        if (cargador != null) {
            em.persist(cargador);
        }
    }

    @Override
    public List<EstacionCarga> obtenerEstaciones() {
        return em.createQuery(
                "SELECT e FROM EstacionCarga e",
                EstacionCarga.class
        ).getResultList();
    }


    @Override
    public Cliente buscarPorCedula(String cedula) {
            return em.find(Cliente.class, cedula);
    }

    @Override
    public boolean actualizar(Cliente cliente) {
       return true;
    }

    @Override
    public void registrarCliente(Cliente cli) {
        if(cli == null){
            throw new ClienteInvalidoException("Cliente no puede ser null");
        }
        //cli.add(cli);
        em.persist(cli);
    }

    @Override
    @Transactional
    public void persistirCarga(Carga cargaNueva) {
        em.persist(cargaNueva);
    }

    @Override
    @Transactional
    public void persistirOActualizarHistorial(HistorialDeCargas historial) {
        if (historial.getId() == 0) {
            em.persist(historial);
        } else {
            em.merge(historial);
        }
    }

    @Override
    @Transactional
    public void persistirElementoHistorial(ElementoHistorial elemento) {
        em.merge(elemento);
    }

    @Override
    @Transactional
    public void ActualizarCliente(Cliente cli) {
        em.merge(cli);
    }


    @Override
    public Tarjeta buscarTarjetaClienteCI(String cedulaCliente, String numeroTarjeta) {

        if (cedulaCliente == null || cedulaCliente.isBlank()) {
            return null;
        }

        if (numeroTarjeta == null || !numeroTarjeta.matches("\\d{8}")) {
            return null;
        }

        Cliente clienteAux = this.buscarPorCedula(cedulaCliente);

        if (clienteAux == null) {
            return null;
        }

        List<Tarjeta> tarjetas = em.createQuery(
                "SELECT t FROM Tarjeta_Carga t " +
                "WHERE t.cliente = :cliente " +
                "AND t.numero = :numero",
                Tarjeta.class
        )
        .setParameter("cliente", clienteAux)
        .setParameter("numero", numeroTarjeta)
        .getResultList();

        if (tarjetas.isEmpty()) {
            return null;
        }

        return tarjetas.get(0); //retorno el 0 porque es la primera que encuentro que es la que cumple la conficion, y es la unica ya que el numero de tarjeta es unico
    }

    @Override
    public Cargador getCargador(Integer idCargador) {
        if (idCargador == null) {
            return null;
        }

        return em.find(Cargador.class, idCargador);
    }


    
    @Override
    public HistorialDeCargas buscarHistorialPorCedula(String cedula) {
        List<HistorialDeCargas> resultado = em.createQuery(
                "SELECT DISTINCT h FROM HistorialDeCargas h " +
                "LEFT JOIN FETCH h.historialCargas elementos " +
                "LEFT JOIN FETCH elementos.carga " +
                "LEFT JOIN FETCH elementos.medioPago " +
                "WHERE h.clienteAsociado.cedula = :cedula",
                HistorialDeCargas.class
        )
        .setParameter("cedula", cedula)
        .getResultList();

        if (resultado.isEmpty()) {
            return null;
        }

        return resultado.get(0);
    }

    @Override
    public List<ElementoHistorial> buscarElementosHistorialPorCedula(String cedula) {
        return em.createQuery(
                "SELECT e FROM ElementoHistorial e " +
                "JOIN FETCH e.carga " +
                "JOIN FETCH e.medioPago " +
                "JOIN e.historialAsociado h " +
                "WHERE h.clienteAsociado.cedula = :cedula " +
                "ORDER BY e.carga.fecha, e.carga.horaInicio",
                ElementoHistorial.class
        )
        .setParameter("cedula", cedula)
        .getResultList();
    }
}
