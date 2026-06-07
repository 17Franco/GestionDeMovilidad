package moduloCliente.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import moduloCliente.exepciones.GrupoNoExisteException;

@Provider
public class GrupoNoExisteExceptionMapper implements ExceptionMapper<GrupoNoExisteException> {

    @Override
    public Response toResponse(GrupoNoExisteException e){
        return Response.status(404)
                .entity("{\"error\":\"" + e.getMessage() + "\"}")
                .build();
    }
}
