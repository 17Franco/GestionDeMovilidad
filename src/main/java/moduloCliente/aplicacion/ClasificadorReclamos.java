package moduloCliente.aplicacion;

import moduloCliente.dominio.TipoReclamo;

public interface ClasificadorReclamos {
    TipoReclamo clasificar(String descripcion);
}