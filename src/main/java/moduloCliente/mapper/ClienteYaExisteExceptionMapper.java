package moduloCliente.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import moduloCliente.exepciones.ClienteYaExisteException;

@Provider
//esta class intercepta cuando se lanza la exepcionde tipo que tiene como parametro y devuelve response
public class ClienteYaExisteExceptionMapper
        implements ExceptionMapper<ClienteYaExisteException> {

    @Override
    public Response toResponse(ClienteYaExisteException e) {
        return Response.status(409)
                .entity("{\"error\":\"" + e.getMessage() + "\"}")
                .build();
    }
}