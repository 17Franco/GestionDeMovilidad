package moduloCliente.infraestructura.seguridad;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import moduloCliente.dominio.cliente.Cliente;
import moduloCliente.dominio.repositorio.ClienteRepositorio;

@ApplicationScoped
public class ValidadorCredenciales implements IdentityStore {

    @Inject
    private ClienteRepositorio repo; //para poder usar los metodos

    @Override
    public CredentialValidationResult validate(Credential credential) {
        System.out.println("IdentityStore ejecutado");
        CredentialValidationResult resultado = CredentialValidationResult.INVALID_RESULT;
        UsernamePasswordCredential credencial = (UsernamePasswordCredential) credential;
        String usr = credencial.getCaller();//separo usr osea ci
        String pass = credencial.getPasswordAsString(); //y su pass

        //ahora llamo a repo
        Cliente cliente = repo.buscarCliente(usr);

        if(cliente != null){
            //si existe compruebo contrasenia
            if(pass.equals(cliente.getContra())){
                //si es correcto
                resultado =  new CredentialValidationResult(usr);
            }else {
                System.out.println("password incorrecta");
            }

        }else{
            System.out.println("No existe usuario.");
        }
        return resultado;
    }


}
