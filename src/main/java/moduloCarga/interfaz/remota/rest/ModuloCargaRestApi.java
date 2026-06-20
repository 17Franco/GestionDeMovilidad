package moduloCarga.interfaz.remota.rest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import moduloCarga.aplicacion.ServicioCarga;
import moduloCarga.dominio.Cargador;
import moduloCarga.dominio.EstacionCarga;
import moduloCarga.dominio.EstadoCargador;

@ApplicationScoped
@Path("/carga")
public class ModuloCargaRestApi {

    @Inject
    private ServicioCarga servicioCarga;

    @POST
    @Path("/estacion")
    @Consumes(MediaType.APPLICATION_JSON)
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

        return Response.status(Response.Status.CREATED)
                .entity("{\"mensaje\":\"Estacion creada\",\"id\":"
                        + estacion.getId() + "}")
                .build();
    }

    /*
    Ejemplo de petición HTTP:

    curl -X POST -H "Content-Type: application/json" \
    -d '{
        "tipo": "NORMAL",
        "tieneCable": true,
        "tipoConector": "GRANDE",
        "potenciaMinima": 22,
        "estacionCarga": 1
    }' \
    http://localhost:8080/GestionDeMovilidad/movilidad/carga/cargador
    */
   /*
   Verion en 1 sola lína:
   curl -X POST -H "Content-Type: application/json" -d '{"tipo":"NORMAL","tieneCable":true,"tipoConector":"GRANDE","potenciaMinima":22,"estacionCarga":1}' http://localhost:8080/GestionDeMovilidad/movilidad/carga/cargador
   */
    @POST
    @Path("/cargador")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response altaCargador(CargadorDTO cargadorDTO) {

        System.out.println("ENTRÓ CARGADOR");

        Cargador cargador = new Cargador();
        cargador.setTipo(cargadorDTO.getTipo());
        cargador.setTipoConector(cargadorDTO.getTipoConector());
        //Lo seteo en operativo ya que lo acabo de crear
        cargador.setEstado(EstadoCargador.OPERATIVO);
        //Elimino "cargador.setId(1);" porque Cargador ya tiene "@GeneratedValue"

        cargador.setTieneCable(cargadorDTO.isTieneCable());
        cargador.setPotenciaMinima(cargadorDTO.getPotenciaMinima());
        servicioCarga.altaCargador(cargadorDTO.getEstacionCarga(),cargador);

        return Response.status(Response.Status.CREATED)
        .entity("{\"mensaje\":\"Cargador creado\",\"id\":"
                + cargador.getId() + "}")
        .build();
    }
}
