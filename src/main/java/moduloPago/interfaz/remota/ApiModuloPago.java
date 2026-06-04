package moduloPago.interfaz.remota;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import moduloPago.aplicacion.ServicioPago;
import moduloPago.dominio.pagoRealizado;

import java.time.LocalDate;
import java.util.List;


@Path("/pagos")
public class ApiModuloPago {
    @Inject
    private ServicioPago servicios;

    //falta ver si funciona
    @GET
    public Response getPagos(String ci, String fechaIni, String fechaFin){

        try{
            LocalDate fechaI = LocalDate.parse(fechaIni);
            LocalDate fechaF = LocalDate.parse(fechaFin);
            List<pagoRealizado> list = servicios.consultarPagos(ci,fechaI,fechaF);


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
}
