package moduloCarga.infraestructura.persistencia;

import jakarta.enterprise.context.ApplicationScoped;
import moduloCarga.dominio.Cargador;
import moduloCarga.dominio.EstacionCarga;
import moduloCarga.dominio.cliente.Cliente;
import moduloCarga.dominio.repositorio.RepoCarga;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CargaRepoImpl implements RepoCarga {

    private final List<EstacionCarga> estaciones = new ArrayList<>();

    private final List<Cargador> cargadores = new ArrayList<>();

    private final List<Cliente> clientes = new ArrayList<>();

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

        if (cedula == null) {
            return null;
        }

        return clientes.stream()
                .filter(c -> cedula.equals(c.getCedula()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Cliente> obtenerTodos() {
        return new ArrayList<>(clientes);
    }

    @Override
    public boolean registrarCliente(Cliente cliente){

        if (cliente == null || cliente.getCedula() == null || cliente.getCedula().isBlank()) {
            System.out.println("entre vacio");
            return false;
        }

        if (buscarPorCedula(cliente.getCedula()) != null) {
            return false;
        }

        clientes.add(cliente);

        return true;
    }
}