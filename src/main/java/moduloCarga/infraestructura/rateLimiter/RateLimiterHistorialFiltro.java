package moduloCarga.infraestructura.rateLimiter;

import java.io.IOException;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@PreMatching
@Provider
public class RateLimiterHistorialFiltro implements ContainerRequestFilter {

    @Inject
    private RateLimiterHistorial rateLimiterHistorial;
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String path = requestContext.getUriInfo().getPath();

        System.err.println("\u001B[91m================ RATE LIMITER HISTORIAL EJECUTADO ================\u001B[0m");
        System.err.println("\u001B[91mPATH = " + path + "\u001B[0m");
        if (!path.equals("/cargas/verHistorial")) {
            return;
        }

        if (rateLimiterHistorial.isActivo()) {
            boolean sePermiteEjecutar = rateLimiterHistorial.consumir();

            if (!sePermiteEjecutar) {
                System.out.println("Se rechazó una consulta a verHistorial por rate limit");

                requestContext.abortWith(
                        Response.status(Response.Status.TOO_MANY_REQUESTS)
                                .entity("{\"error\":\"Demasiadas consultas al historial. Intente nuevamente más tarde.\"}")
                                .build()
                );
            }
        }
    }
}