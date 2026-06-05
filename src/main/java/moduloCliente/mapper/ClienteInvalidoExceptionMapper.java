package moduloCliente.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import moduloCliente.exepciones.ClienteInvalidoException;

@Provider
public class ClienteInvalidoExceptionMapper
        implements ExceptionMapper<ClienteInvalidoException> {

    @Override
    public Response toResponse(ClienteInvalidoException e) {
        return Response.status(400)
                .entity("{\"error\":\"" + e.getMessage() + "\"}")
                .build();
    }
}
