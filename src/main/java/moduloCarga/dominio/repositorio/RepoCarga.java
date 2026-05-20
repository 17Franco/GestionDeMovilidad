package moduloCarga.dominio.repositorio;

import moduloCarga.dominio.Cargador;
import moduloCarga.dominio.EstacionCarga;

public interface RepoCarga {


    void guardarEstacion(EstacionCarga estacion);
    void guardarCargador(Cargador cargador);

}
