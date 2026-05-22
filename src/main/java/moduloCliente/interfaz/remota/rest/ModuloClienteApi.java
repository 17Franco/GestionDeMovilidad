package moduloCliente.interfaz.remota.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import moduloCliente.aplicacion.ServicioCliente;
import moduloCliente.dominio.cliente.Cliente;
import moduloCliente.dominio.cliente.ClienteComun;
import moduloCliente.dominio.cliente.ClienteProfesional;

import java.util.List;
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
@Path("/clientes")
public class ModuloClienteApi {

    @Inject
    private ServicioCliente servicioCliente;

    @POST
    public Boolean resgistrarCliente(ClienteDTO clienteDTO){
        Cliente cliente;
        if("COMUN".equals(clienteDTO.getTipoCliente())){
            cliente = new ClienteComun(
                    clienteDTO.getCedula(),
                    clienteDTO.getNombre(),
                    clienteDTO.getApellido(),
                    clienteDTO.getNumTel(),
                    clienteDTO.getContra());
        }else if("PROFESIONAL".equals(clienteDTO.getTipoCliente())){
            cliente = new ClienteProfesional(
                    clienteDTO.getCedula(),
                    clienteDTO.getNombre(),
                    clienteDTO.getApellido(),
                    clienteDTO.getNumTel(),
                    clienteDTO.getContra(),
                    clienteDTO.getTipoProfesional(),
                    clienteDTO.getPorcentajeDescuento());
        } else {
            throw new IllegalArgumentException("Tipo de cliente inválido");
        }

        boolean resu = servicioCliente.registrarCliente(cliente);

        return  resu;

    }

   /* @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Cliente> verClientes(){
        return servicioCliente.obtenerClientes();
    }
    */
}
