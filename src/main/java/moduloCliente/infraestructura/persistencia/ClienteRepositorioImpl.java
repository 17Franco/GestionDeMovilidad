package moduloCliente.infraestructura.persistencia;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import moduloCliente.dominio.MedioPago;
import moduloCliente.dominio.Grupo;
import moduloCliente.dominio.Reclamo;
import moduloCliente.dominio.cliente.Cliente;
import moduloCliente.dominio.repositorio.ClienteRepositorio;
import moduloCliente.exepciones.ClienteInvalidoException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class ClienteRepositorioImpl implements ClienteRepositorio {

    @PersistenceContext
    private EntityManager em;

    private final List<Cliente> clientes = new ArrayList<>();

    @Override
    public void saveCliente(Cliente cliente) {
        if (cliente == null) {
            throw new ClienteInvalidoException("Cliente no puede ser null");
        }

        clientes.add(cliente);
        em.persist(cliente);

    }

    public Grupo findGroup(String grupo) {
        return em.find(Grupo.class, grupo);
    }

    @Override
    public boolean actualizar(Cliente cliente) {
        if (cliente == null) {
            return false;
        }

        em.merge(cliente);
        return true;
    }

    @Override
    public Cliente buscarCliente(String cedula) {
        return em.find(Cliente.class, cedula);
    }

    @Override
    public List<Cliente> obtenerClientes() {
        return em.createQuery("SELECT c FROM MCliente_Cliente c", Cliente.class)
                .getResultList();
    }

    @Override
    public boolean saveReclamo(Reclamo reclamo) {

        if (reclamo == null) {
            return false;
        }
        em.persist(reclamo);
        // em.persist(reclamo.getCliente());

        return true;
    }

    // altamediopago
    @Override
    public void saveMedioPago(MedioPago medioPago) {
        em.persist(medioPago);
    }

    @Override
    public Reclamo buscarReclamoPorID(Long idReclamo){
        return em.find(Reclamo.class, idReclamo);
    }

   @Override
    public List<Reclamo> mostrarReclamos() {
        return em.createQuery(
                "SELECT r FROM Reclamo r JOIN FETCH r.cliente ORDER BY r.id DESC",
                Reclamo.class
        ).getResultList();
    }

}
