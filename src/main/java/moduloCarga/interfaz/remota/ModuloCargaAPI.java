package moduloCarga.interfaz.remota;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import moduloCarga.aplicacion.ServicioCarga;
import moduloCarga.dominio.cliente.Cliente;
import moduloCarga.dominio.repositorio.RepoCarga;



@Path("/cargas")
@ApplicationScoped
public class ModuloCargaAPI {
    
    @Inject ServicioCarga serivcioCarga;
    @Inject RepoCarga repoCarga;


    /*  head-> http://localhost:8080/GestionDeMovilidad/movilidad/cargas/iniciar
        body-> {cedulaCliente:"1234567-8"
                metodoPago:"TARJETA" o "FACTURA_UTE"
                }
        (son Strings ambos)
    */
    @POST
    @Path("/iniciar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response iniciarCarga(iniciarCargaDatos datos) {               //"datos" es el json que me llega por http, el framwork jakarta lo traduce automaticamente al dto que yo le meta, podria trabajar con string, pero justamente el framework es para facilitarme esto
        //busco el cliente, si existe y el metodo de pago es correcto inicio la carga
        //tengo que verificar que el cliente tenga ese metodo de pago asociado, tambien tengo que verificar que no puede pasar que sea un cliente profesional y quiera pagar con FacutaUTE
        String cedulaCliente = datos.getCedulaCliente();
        Cliente clienteBuscado = repoCarga.buscarPorCedula(cedulaCliente);
        if(clienteBuscado == null){
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity("{\"error\":\"No existe cliente con cédula " + cedulaCliente + "\"}")
                .build();
        }

        else{
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity("{\"error\":\"TE RESPONDO LO MISMO WACHIIIIIIN " + cedulaCliente + "\"}")
                .build();
        }
    }
}
