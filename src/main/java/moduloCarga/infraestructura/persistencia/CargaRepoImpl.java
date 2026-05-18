package moduloCarga.infraestructura.persistencia;

import jakarta.enterprise.context.ApplicationScoped;
import moduloCarga.dominio.EstacionCarga;
import moduloCarga.dominio.repositorio.RepoCarga;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CargaRepoImpl implements RepoCarga {
    private final List<EstacionCarga> estaciones = new ArrayList<>();

    public void registrarEstacion(EstacionCarga estacion) {
        if (estacion != null) {
            estaciones.add(estacion);
        }
    }

    public List<EstacionCarga> obtenerEstaciones() {
        return new ArrayList<>(estaciones);
    }
}
