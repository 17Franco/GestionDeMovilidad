package moduloCliente.infraestructura.persistencia;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import moduloCliente.dominio.Grupo;
import moduloCliente.dominio.Reclamo;
import moduloCliente.dominio.cliente.Cliente;
import moduloCliente.dominio.repositorio.ClienteRepositorio;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class ClienteRepositorioImpl implements ClienteRepositorio {

    @PersistenceContext
    private EntityManager em;

    private final List<Cliente> clientes = new ArrayList<>();

    @Override
    public boolean saveCliente(Cliente cliente) {
        if(cliente == null){
            return false;
        }
        Grupo g = em.find(Grupo.class, "appMovil");
        if (g == null) {
            throw new RuntimeException("Grupo no existe");
        }

        if (cliente.getGrupos() == null) {
            cliente.setGrupos(new ArrayList<>());
        }

        cliente.getGrupos().add(g);

        clientes.add(cliente);
        em.persist(cliente);
        return true;
    }
    @Override
    public boolean actualizar(Cliente cliente) {
        if (cliente == null || cliente.getCedula() == null) {
            return false;
        }
        for (int i = 0; i < clientes.size(); i++) {
            if (Objects.equals(clientes.get(i).getCedula(), cliente.getCedula())) {
                clientes.set(i, cliente);
                return true;
            }
        }
        return false;
    }

    @Override
    public Cliente buscarCliente(String cedula) {
        if (cedula == null) {
            return null;
        }
        return clientes.stream()
                .filter(c -> cedula.equals(c.getCedula()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Cliente> allcliente() {
        return new ArrayList<>(clientes);
    }

    @Override
    public boolean saveReclamo(Reclamo reclamo){

        if(reclamo == null){
            return false;
        }

            reclamo.getCliente().getReclamos().add(reclamo);
            em.persist(reclamo);
            //em.persist(reclamo.getCliente());

        return true;
    }
}
