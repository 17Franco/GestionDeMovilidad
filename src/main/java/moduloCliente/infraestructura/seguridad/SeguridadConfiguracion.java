package moduloCliente.infraestructura.seguridad;

import jakarta.annotation.security.DeclareRoles;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;

@BasicAuthenticationMechanismDefinition(realmName = "ApplicationRealm")

@DeclareRoles({"appMovil", "gestorWeb", "admin"})

@ApplicationScoped
public class SeguridadConfiguracion {
}
