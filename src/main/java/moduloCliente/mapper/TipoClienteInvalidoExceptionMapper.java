package moduloCliente.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import moduloCliente.exepciones.GrupoNoExisteException;
import moduloCliente.exepciones.TipoClienteInvalidoException;

@Provider
public class TipoClienteInvalidoExceptionMapper implements ExceptionMapper<TipoClienteInvalidoException> {

    @Override
    public Response toResponse(TipoClienteInvalidoException e){
        return Response.status(400)
                .entity("{\"error\":\"" + e.getMessage() + "\"}")
                .build();
    }
}
