package moduloCliente.interfaz.remota.rest;

import java.util.List;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import jakarta.ws.rs.core.SecurityContext;
import moduloCliente.dominio.MedioPago;
import moduloCliente.dominio.CuentaUTE;
import moduloCliente.aplicacion.ServicioCliente;
import moduloCliente.dominio.MedioPagoDTO;
import moduloCliente.dominio.Reclamo;
import moduloCliente.dominio.Tarjeta;
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
import moduloCliente.dominio.repositorio.ClienteRepositorio;

@ApplicationScoped
@Path("/clientes")
public class ModuloClienteApi {

  @Inject
  private ClienteRepositorio repoCliente;

  @Inject
  private ServicioCliente servicioCliente;

  @Inject
  SecurityContext securityContext;

  @POST
  @PermitAll
  @Consumes(MediaType.APPLICATION_JSON) // recibe JSON
  @Produces(MediaType.APPLICATION_JSON) // devuelve JSON
  public Response resgistrarCliente(@Valid ClienteDTO clienteDTO) {
    // pasamos el dto al objeto dominio que le toca
    Cliente cliente = ClienteMapper.toDomain(clienteDTO);

    // llamo al servicio
    servicioCliente.registrarCliente(cliente);

    // si todo sale bien respondo con codigo created y mensaje cliente registrado
    return Response
        .status(Response.Status.CREATED)
        .entity("{\"mensaje\":\"Cliente registrado\"}")
        .build();
  }

  /*
   * curl -v -u 49876541:1234 -H "Content-Type: application/json" -X POST -d '{
   * "asunto": "PrimerReclamo",
   * "descripcion": "Hola este es el primer reclamo",
   * 
   * }' http://localhost:8080/GestionDeMovilidad/movilidad/clientes/reclamos/
   */

  @POST
  @Consumes(MediaType.APPLICATION_JSON) // recibe JSON
  @Produces(MediaType.APPLICATION_JSON) // devuelve JSON
  @Path("/reclamos")
  @RolesAllowed("appMovil") // enpoint se fija si el usuario tiene este rol si lo tiene sigue si no manda
                            // forbidden
  public Response registrarReclamo(ReclamoDTO reclamo)  {
    String ci = securityContext.getUserPrincipal().getName(); // obtengo ci
    Reclamo r = servicioCliente.realizarReclamo(reclamo.getAsunto(), reclamo.getDescripcion(), ci);
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
  @Path("/obtenerClientes")
  @RolesAllowed("appMovil")
  public List<Cliente> verClientes() {
    return servicioCliente.obtenerClientes();
  }

  //altamediopago
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/medioPago")
  @RolesAllowed("appMovil")
  public Response altaMedioPago(MedioPagoDTO medioPagoDTO) {

    String ci = securityContext.getUserPrincipal().getName();

    MedioPago medioPago;

    if (medioPagoDTO.getTipoMedioPago().equals("CUENTA_UTE")) {
      CuentaUTE cuentaUTE = new CuentaUTE();
      cuentaUTE.setNumeroCuenta(medioPagoDTO.getNumeroCuenta());
      medioPago = cuentaUTE;

    } else if (medioPagoDTO.getTipoMedioPago().equals("TARJETA")) {
      Tarjeta tarjeta = new Tarjeta();
      tarjeta.setNumero(medioPagoDTO.getNumero());
      tarjeta.setFechaVencimiento(medioPagoDTO.getFechaVencimiento());
      tarjeta.setDigitoVerificacion(medioPagoDTO.getDigitoVerificacion());
      tarjeta.setTipo(medioPagoDTO.getTipoTarjeta());
      medioPago = tarjeta;

    } else {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("{\"error\":\"Tipo de medio de pago invalido\"}")
          .build();
    }

    boolean resultado = servicioCliente.altaMedioPago(ci, medioPago);

    if (!resultado) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("{\"error\":\"No se pudo registrar el medio de pago\"}")
          .build();
    }

    return Response.status(Response.Status.CREATED)
        .entity("{\"mensaje\":\"Medio de pago registrado\"}")
        .build();
  }

  /*
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/obtenerReclamos")
  @RolesAllowed("appMovil")
  public List<ReclamoConClienteDTO> mostrarReclamos() {
      return servicioCliente.mostrarReclamos()
              .stream()
              .map(ReclamoConClienteDTO::new)
              .toList();
  }
  */
  

}
