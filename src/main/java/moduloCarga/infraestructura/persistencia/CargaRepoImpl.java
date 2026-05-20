package moduloCarga.infraestructura.persistencia;

import jakarta.enterprise.context.ApplicationScoped;
import moduloCarga.dominio.repositorio.RepoCarga;
import moduloCarga.dominio.Cargador;
import moduloCarga.dominio.EstacionCarga;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CargaRepoImpl implements RepoCarga {
    private List<EstacionCarga> estaciones = new ArrayList<>();
    private List<Cargador> cargadores = new ArrayList<>();

    @Override
    public void guardarEstacion(EstacionCarga estacion) {
        estaciones.add(estacion);
    }

    @Override
    public void guardarCargador(Cargador cargador) {
        cargadores.add(cargador);
    }
}
