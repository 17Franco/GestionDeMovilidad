package moduloPago.interfaz.remota;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import moduloPago.aplicacion.ServicioPago;
import moduloPago.dominio.Pago;

import java.time.LocalDate;
import java.util.List;


@Path("/pagos")
public class ApiModuloPago {
    
    @Inject
    private ServicioPago servicios;

    @Inject
    private SecurityContext securityContext;

    //falta ver si funciona
    @GET
    public Response getPagos(String ci, String fechaIni, String fechaFin){

        try{
            LocalDate fechaI = LocalDate.parse(fechaIni);
            LocalDate fechaF = LocalDate.parse(fechaFin);
            List<Pago> list = servicios.consultarPagos(ci,fechaI,fechaF);


            return Response
                    .status(Response.Status.OK)
                    .entity(list)
                    .build();

        }catch (Exception e){
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


    /*
    Llamada:
    curl -i -u "1111111-1:1234" -X POST \
    -H "Content-Type: application/json" \
    -d '{"numeroTarjeta":"11111111","monto":500}' \
    http://localhost:8080/GestionDeMovilidad/movilidad/pagos/pagarDeuda 
    */
    @POST
    @Path("/pagarDeuda")
    @RolesAllowed("appMovil")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response pagarDeuda(PagoDeudaDTO datos) {

        String cedula = securityContext.getUserPrincipal().getName();

        boolean deudaActiva = servicios.pagarDeuda(
                cedula,
                datos.getNumeroTarjeta(),
                datos.getMonto()
        );

        if (!deudaActiva) {
            return Response.ok(
                    "{\"mensaje\":\"Deuda pagada correctamente\"}"
            ).build();
        }

        return Response.status(Response.Status.PAYMENT_REQUIRED)
                .entity("{\"error\":\"El pago fue rechazado\"}")
                .build();
    }
}
