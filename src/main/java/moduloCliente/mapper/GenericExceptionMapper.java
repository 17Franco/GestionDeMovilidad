package moduloCliente.mapper;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        e.printStackTrace();

        String error = e.getClass().getSimpleName();
        String mensaje = e.getMessage();

        if (mensaje == null) {
            mensaje = "Sin mensaje";
        }

        String json = "{\"error\":\"" + error + "\",\"mensaje\":\"" + mensaje + "\"}";

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(json)
                .build();
    }
}

/*
    @Override
    public Response toResponse(Exception e) {
        return Response.status(500)
                .entity("{\\\"error\\\":\\\"\" + e.getClass().getSimpleName() + \"\\\", \\\"mensaje\\\":\\\"\" + e.getMessage() + \"\\\"}")
                .build();
    }
*/
