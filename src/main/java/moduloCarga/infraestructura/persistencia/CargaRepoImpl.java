package moduloCarga.infraestructura.persistencia;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import moduloCarga.dominio.Cargador;
import moduloCarga.dominio.EstacionCarga;
import moduloCarga.dominio.cliente.Cliente;
import moduloCarga.dominio.repositorio.RepoCarga;
import moduloCarga.dominio.Cargador;
import moduloCarga.dominio.EstacionCarga;

import java.util.*;

@ApplicationScoped
public class CargaRepoImpl implements RepoCarga {

    @PersistenceContext
    private EntityManager em;

    private final List<EstacionCarga> estaciones = new ArrayList<>();

    private final List<Cargador> cargadores = new ArrayList<>();


    private final List<Cliente> clientes = new ArrayList<>();
    
    @Override
    public void guardarEstacion(EstacionCarga estacion) {
        estaciones.add(estacion);
    }

    @Override
    public void guardarCargador(Cargador cargador) {
        cargadores.add(cargador); 
    }

   
    @Override
    public void registrarEstacion(EstacionCarga estacion) {
        if (estacion != null) {
            estaciones.add(estacion);
        }
    }

    @Override
    public void registrarCargador(Cargador cargador) {
        if (cargador != null) {
            cargadores.add(cargador);
        }
    }

    @Override
    public List<EstacionCarga> obtenerEstaciones() {
        return new ArrayList<>(estaciones);
    }


    @Override
    public Cliente buscarPorCedula(String cedula) {

        return em.find(Cliente.class, cedula);
    }

    @Override
    public void registrarCliente(Cliente cliente){
        if(cliente == null){
            throw new IllegalArgumentException("Cliente no puede ser null");
        }
        clientes.add(cliente);
        em.persist(cliente);


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
}