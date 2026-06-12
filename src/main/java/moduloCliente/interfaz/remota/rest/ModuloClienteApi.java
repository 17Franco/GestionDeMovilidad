package moduloCliente.interfaz.remota.rest;

import java.util.List;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import jakarta.ws.rs.core.SecurityContext;
import moduloCliente.aplicacion.ServicioCliente;
import moduloCliente.dominio.Reclamo;
import moduloCliente.dominio.cliente.Cliente;
// URL http://localhost:8080/GestionDeMovilidad/movilidad/clientes
/*JSON*/
//clienteComun
/*
{
  "cedula": "",
  "nombre": "",
  "apellido": "",
  "numTel": "",
  "contra": "",
  "tipoCliente": "COMUN"
}
*/
//clienteProfsional
/*
{
  "cedula": "",
  "nombre": "",
  "apellido": "",
  "numTel": "",
  "contra": "",
  "tipoCliente": "PROFESIONAL",
  "tipoProfesional": "BASICO",
  "porcentajeDescuento":
}
*/

@ApplicationScoped
@DenyAll
@Path("/clientes")
public class ModuloClienteApi {

    @Inject
    private ServicioCliente servicioCliente;

    @Inject
    SecurityContext securityContext;


    @POST
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)//recibe JSON
    @Produces(MediaType.APPLICATION_JSON)//devuelve JSON
    public Response resgistrarCliente(@Valid ClienteDTO clienteDTO){
        //pasamos el dto al objeto dominio que le toca
        Cliente cliente = ClienteMapper.toDomain(clienteDTO);

        //llamo al servicio
        servicioCliente.registrarCliente(cliente);

        //si todo sale bien respondo con codigo created y mensaje cliente registrado
        return Response
                .status(Response.Status.CREATED)
                .entity("{\"mensaje\":\"Cliente registrado\"}")
                .build();
    }

    /*
    * curl -v -u 49876541:1234 -H "Content-Type: application/json" -X POST -d '{
      "asunto": "PrimerReclamo",
      "descripcion": "Hola este es el primer reclamo",

    }' http://localhost:8080/GestionDeMovilidad/movilidad/clientes/reclamos/
    */

    @POST
    @Consumes(MediaType.APPLICATION_JSON)//recibe JSON
    @Produces(MediaType.APPLICATION_JSON)//devuelve JSON
    @Path("/reclamos")
    @RolesAllowed("appMovil")//enpoint se fija si el usuario tiene este rol si lo tiene sigue si no manda forbidden
    public Response registrarReclamo(ReclamoDTO reclamo){
        String ci = securityContext.getUserPrincipal().getName(); //obtengo ci
        Reclamo r = servicioCliente.realizarReclamo(reclamo.getAsunto(),reclamo.getDescripcion(),ci);
        ReclamoDTO reclamoDTO = new ReclamoDTO();
        reclamoDTO.setId(r.getId());
        reclamoDTO.setAsunto(r.getAsunto());
        reclamoDTO.setDescripcion(r.getDescripcion());
        reclamoDTO.setClienteCi(r.getCliente().getCedula());

        return Response
                .status(Response.Status.CREATED)
                .entity(reclamoDTO)
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/obtener")
    @RolesAllowed("appMovil")
    public List<Cliente> verClientes(){
        return servicioCliente.obtenerClientes();
    }
    

}
