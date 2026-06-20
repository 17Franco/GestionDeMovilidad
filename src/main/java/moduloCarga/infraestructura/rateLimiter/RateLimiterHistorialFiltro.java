package moduloCarga.infraestructura.rateLimiter;

import java.io.IOException;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
@LimitarHistorial
public class RateLimiterHistorialFiltro implements ContainerRequestFilter {

    @Inject
    private RateLimiterHistorial rateLimiterHistorial;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        System.out.println("Rate limiter de historial ejecutado");

        if (rateLimiterHistorial.isActivo()
                && !rateLimiterHistorial.consumir()) {

            System.out.println("Consulta a verHistorial rechazada por rate limit");

            requestContext.abortWith(
                Response.status(Response.Status.TOO_MANY_REQUESTS)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"Demasiadas consultas al historial. Intente nuevamente más tarde.\"}")
                    .build()
            );
        }
    }
}