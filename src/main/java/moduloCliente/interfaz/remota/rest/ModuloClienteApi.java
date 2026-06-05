package moduloCliente.interfaz.remota.rest;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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
    public Response resgistrarCliente(ClienteDTO clienteDTO){
        Cliente cliente;
        try{
            if("COMUN".equals(clienteDTO.getTipoCliente())){
                cliente = clienteDTO.buildClienteComun();
            }else if("PROFESIONAL".equals(clienteDTO.getTipoCliente())){
                cliente = clienteDTO.buildClienteProfesional();
            } else {
                throw new IllegalArgumentException("Tipo de cliente inválido");
            }


            boolean resu = servicioCliente.registrarCliente(cliente);
            //si todo sale bienn devuelvo mensaje de usuario se registro
            if (resu) {
                return Response
                        .status(Response.Status.CREATED)
                        .entity("{\"mensaje\":\"Cliente registrado\"}")
                        .build();
            }
            //sino devuelvo que no se pudo
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"No se pudo registrar\"}")
                    .build();

            //en caso de que se lanze exepcion en el servicio lo capturo y devuelvo mensaje
        }catch (Exception e){
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno: " + e.getMessage() + "\"}")
                    .build();
        }

    }

    /*
    * curl -v -u 49876541:1234 -H "Content-Type: application/json" -X POST -d '{
      "asunto": "PrimerReclamo",
      "descripcion": "Hola este es el primer reclamo",
      "clienteCi": "49876541"
    }' http://localhost:8080/GestionDeMovilidad/movilidad/clientes/reclamos/
    */

    @POST
    @Consumes(MediaType.APPLICATION_JSON)//recibe JSON
    @Produces(MediaType.APPLICATION_JSON)//devuelve JSON
    @Path("/reclamos")
    @RolesAllowed("appMovil")//enpoint se fija si el usuario tiene este rol si lo tiene sigue si no manda forbidden
    public Response registrarReclamo(ReclamoDTO reclamo){

        try{
            Reclamo r = servicioCliente.realizarReclamo(reclamo.getAsunto(),reclamo.getDescripcion(),reclamo.getClienteCi());
            if(r != null){
                ReclamoDTO reclamoDTO= new ReclamoDTO();
                reclamoDTO.setId(r.getId());
                reclamoDTO.setAsunto(r.getAsunto());
                reclamoDTO.setDescripcion(r.getDescripcion());
                reclamoDTO.setClienteCi(r.getCliente().getCedula());
                return Response
                        .status(Response.Status.CREATED)
                        .entity(reclamoDTO)
                        .build();
            }

            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"No se pudo agregar el reclamo\"}")
                    .build();

        }catch(Exception e){
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno: " + e.getMessage() + "\"}")
                    .build();
        }
    }

   /* @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Cliente> verClientes(){
        return servicioCliente.obtenerClientes();
    }
    */

}
