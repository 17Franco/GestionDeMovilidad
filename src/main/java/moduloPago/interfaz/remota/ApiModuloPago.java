package moduloPago.interfaz.remota;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import moduloPago.aplicacion.ServicioPago;
import moduloPago.dominio.Pago;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
@Path("/pagos")
public class ApiModuloPago {
    
    @Inject
    private ServicioPago servicios;

    @Inject
    private SecurityContext securityContext;

    //falta ver si funciona
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPagos(@QueryParam("ci") String ci, @QueryParam("fechaIni") String fechaIni, @QueryParam("fechaFin") String fechaFin) {


        if (ci == null || fechaIni == null || fechaFin == null || fechaIni.isEmpty() || fechaFin.isEmpty()) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Faltan parámetros obligatorios: ci, fechaIni o fechaFin, o están mal escritos.\"}")
                    .build();
        }

        try {

            LocalDate fechaI = LocalDate.parse(fechaIni);
            LocalDate fechaF = LocalDate.parse(fechaFin);
            List<Pago> list = servicios.consultarPagos(ci, fechaI, fechaF);

            return Response
                    .status(Response.Status.OK)
                    .entity(list)
                    .build();

        } catch (java.time.format.DateTimeParseException dtpe) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"El formato de fecha es incorrecto. Debe ser YYYY-MM-DD.\"}")
                    .build();

        } catch (Exception e) {

            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    //mepa qeu no es nesesario el pago se hace cuando finaliza carga
    /*
    @POST
    @Path("/pagarConTarjeta")
    @RolesAllowed("appMovil")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response pagarConTarjeta(PagoConTarjetaDTO datos) {

      /*  String cedula = securityContext.getUserPrincipal().getName();

        boolean autorizado = servicios.pagarConTarjeta(
                cedula,
                datos.getNumeroTarjeta(),
                datos.getMonto()
        );

        if (autorizado) {
            return Response.ok(
                    "{\"mensaje\":\"Pago realizado correctamente\"}"
            ).build();
        }

        return Response.status(Response.Status.PAYMENT_REQUIRED)
                .entity("{\"error\":\"El pago fue rechazado y se generó una deuda\"}")
                .build();


    }
    */



}
