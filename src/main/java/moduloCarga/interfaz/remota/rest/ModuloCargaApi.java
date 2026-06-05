package moduloCarga.interfaz.remota.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
    @Produces(MediaType.APPLICATION_JSON)
    public Response altaEstacion(EstacionDTO estacionDTO) {

        System.out.println("ENTRÓ ESTACION");

        EstacionCarga estacion = new EstacionCarga();

        estacion.setDescripcion(estacionDTO.getDescripcion());
        estacion.setCalle(estacionDTO.getCalle());
        estacion.setDepartamento(estacionDTO.getDepartamento());
        estacion.setLongitud(estacionDTO.getLongitud());
        estacion.setLatitud(estacionDTO.getLatitud());

        servicioCarga.altaEstacion(estacion);

        return Response.ok("{\"mensaje\":\"Estacion creada\"}").build();
    }

    @POST
    @Path("/cargador")
    @Produces(MediaType.APPLICATION_JSON)
    public Response altaCargador(CargadorDTO cargadorDTO) {

        System.out.println("ENTRÓ CARGADOR");

        Cargador cargador = new Cargador();

        cargador.setTieneCable(cargadorDTO.isTieneCable());
        cargador.setPotenciaMinima(cargadorDTO.getPotenciaMinima());

        servicioCarga.altaCargador(cargador);

        return Response.ok("{\"mensaje\":\"Cargador creado\"}").build();
    }
}