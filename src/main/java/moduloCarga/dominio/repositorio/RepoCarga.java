package moduloCarga.dominio.repositorio;

import moduloCarga.dominio.EstacionCarga;

import java.util.List;

public interface RepoCarga {
    void registrarEstacion(EstacionCarga estacion);
    List<EstacionCarga> obtenerEstaciones();
}
