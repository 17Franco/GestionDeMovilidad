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
import moduloCarga.dominio.cliente.ClienteProfesional;
import moduloCarga.dominio.medioPago.CuentaUTE;
import moduloCarga.dominio.medioPago.MedioPago;
import moduloCarga.dominio.medioPago.Tarjeta;
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
        
        //valido la existencia del cliente
        String cedulaCliente = datos.getCedulaCliente();
        Cliente clienteBuscado = repoCarga.buscarPorCedula(cedulaCliente);
        if(clienteBuscado == null){
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity("{\"error\":\"No existe cliente con cédula " + cedulaCliente + "\"}")
                .build();
        }

        //si el cliente existe en la base de datos
        else{
            //pregunto si me manda los medios de pago que acepto
            String medioPagoString = datos.getMetodoPago();
            if(!medioPagoString.equals("TARJETA") && !medioPagoString.equals("CUENTA_UTE")){
                return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"El metodo de pago " + medioPagoString + " no es valido" + "\"}")
                    .build();
            }

            //pregunto si me esta pasando un cliente profesional con el metodo de pago que no puede
            if(medioPagoString.equals("CuentaUTE") && clienteBuscado instanceof ClienteProfesional){
                return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"El metodo de pago " + medioPagoString + " no es valido para el tipo de cliente seleccionado (Profesional)" + "\"}")
                    .build();
            }

            //si luego de todo esto, me pasa el cliente con el metodo de pago adecuado, busco si realmente tiene ese metodo de pago en la base de datos
            /*if(){

            }
            */
            //Tengo que transformar el string a un objeto de tipo MedioPago
            MedioPago medioPago;
            if ("TARJETA".equals(medioPagoString)) {
                medioPago = new Tarjeta();
            } else if ("CUENTA_UTE".equals(medioPagoString)) {
                medioPago = new CuentaUTE();
            } else {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Método de pago inválido\"}")
                        .build();
            }
            //se supone que si llego hasta acá es porque paso por todos los controles de arriba, asi que eu confio
            serivcioCarga.iniciarCarga(clienteBuscado, medioPago);
            return Response
                .status(Response.Status.CREATED)
                .entity("{\"mensaje\":\"Carga iniciada correctamente\"}")
                .build();
        }

       




        
    }
}
