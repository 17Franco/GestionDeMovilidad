package moduloCliente.infraestructura.seguridad;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;

@BasicAuthenticationMechanismDefinition(realmName = "ApplicationRealm")

@ApplicationScoped
public class ConfigSeguridad {
}
