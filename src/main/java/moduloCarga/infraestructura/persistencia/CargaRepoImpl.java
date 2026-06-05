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
import moduloCarga.dominio.repositorio.RepoCarga;

import java.util.List;

@ApplicationScoped
public class CargaRepoImpl implements RepoCarga {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void guardarEstacion(EstacionCarga estacion) {
        em.persist(estacion);
    }

    @Override
    @Transactional
    public void guardarCargador(Cargador cargador) {
        em.persist(cargador);
    }

    @Override
    @Transactional
    public void registrarEstacion(EstacionCarga estacion) {
        if (estacion != null) {
            em.persist(estacion);
        }
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
        if (cedula == null || cedula.isBlank()) {
            return null;
        }

        return em.find(Cliente.class, cedula);
    }

    @Override
    public List<Cliente> obtenerTodos() {
        return em.createQuery(
                "SELECT c FROM Cliente_Carga c",
                Cliente.class
        ).getResultList();
    }

    @Override
    @Transactional
    public boolean registrarCliente(Cliente cli) {
        if (cli == null) {
            return false;
        }

        em.persist(cli);
        return true;
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
        em.persist(elemento);
    }

    @Override
    @Transactional
    public void ActualizarCliente(Cliente cli) {
        em.merge(cli);
    }
}