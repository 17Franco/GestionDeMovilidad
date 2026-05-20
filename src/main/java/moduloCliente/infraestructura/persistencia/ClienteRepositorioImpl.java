package moduloCliente.infraestructura.persistencia;

import jakarta.enterprise.context.ApplicationScoped;
import moduloCliente.dominio.Reclamos;
import moduloCliente.dominio.cliente.Cliente;
import moduloCliente.dominio.repositorio.ClienteRepositorio;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class ClienteRepositorioImpl implements ClienteRepositorio {
    private final List<Cliente> clientes = new ArrayList<>();

    public boolean registrar(Cliente cliente) {
        if (cliente == null || cliente.getCedula() == null || cliente.getCedula().isBlank()) {
            return false;
        }
        if (buscarPorCedula(cliente.getCedula()) != null) {
            return false;
        }
        clientes.add(cliente);
        //lanzo evento que recibe modulocarga y creo al cliente en se modulo
        return true;
    }

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

    public Cliente buscarPorCedula(String cedula) {
        if (cedula == null) {
            return null;
        }
        return clientes.stream()
                .filter(c -> cedula.equals(c.getCedula()))
                .findFirst()
                .orElse(null);
    }

    public List<Cliente> obtenerTodos() {
        return new ArrayList<>(clientes);
    }

    public Reclamos hacerReclamo(String asunto, String descripcion, String ci){
        Cliente cli=buscarPorCedula(ci);
        Reclamos rec = null;
        if (cli != null){
            rec = new Reclamos(asunto,descripcion,cli);
            //como manejo memoria lo guardo en la lista de cliente
            cli.getReclamos().add(rec);
        }
        return rec;
    }
}
