package moduloCliente.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import moduloCliente.exepciones.ClienteNoExisteException;


@Provider
public class ClienteNoExisteExceptionMapper implements ExceptionMapper<ClienteNoExisteException> {

    @Override
    public Response toResponse(ClienteNoExisteException e){
        return Response.status(404)
                .entity("{\"error\":\"" + e.getMessage() + "\"}")
                .build();
    }

}
