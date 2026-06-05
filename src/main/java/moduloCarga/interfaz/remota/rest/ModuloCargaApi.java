package moduloCarga.interfaz.remota.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import moduloCarga.aplicacion.ServicioCarga;
import moduloCarga.dominio.Cargador;
import moduloCarga.dominio.EstacionCarga;

@ApplicationScoped
@Path("/carga")
public class ModuloCargaApi {

    @Inject
    private ServicioCarga servicioCarga;

    @POST
    @Path("/estacion")
    public void altaEstacion(EstacionDTO estacionDTO) {

        EstacionCarga estacion = new EstacionCarga();

        estacion.setDescripcion(estacionDTO.getDescripcion());
        estacion.setCalle(estacionDTO.getCalle());
        estacion.setDepartamento(estacionDTO.getDepartamento());
        estacion.setLongitud(estacionDTO.getLongitud());
        estacion.setLatitud(estacionDTO.getLatitud());

        servicioCarga.altaEstacion(estacion);
    }

    @POST
    @Path("/cargador")
    public void altaCargador(CargadorDTO cargadorDTO) {

        Cargador cargador = new Cargador();

        //cargador.setTieneCable(cargadorDTO.isTieneCable());
        //cargador.setPotenciaMinima(cargadorDTO.getPotenciaMinima());

        servicioCarga.altaCargador(cargador);
    }
}