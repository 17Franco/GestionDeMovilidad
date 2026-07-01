package moduloCarga.interfaz.remota;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import moduloCarga.aplicacion.ServicioCarga;
import moduloCarga.dominio.*;
import moduloCarga.dominio.cliente.Cliente;
import moduloCarga.dominio.cliente.ClienteComun;
import moduloCarga.dominio.cliente.ClienteProfesional;
import jakarta.annotation.security.DenyAll;

import jakarta.ws.rs.core.SecurityContext;

import moduloCarga.dominio.medioPago.MedioPago;
import moduloCarga.dominio.medioPago.Tarjeta;
import moduloCarga.dominio.repositorio.RepoCarga;
import moduloCarga.infraestructura.rateLimiter.LimitarHistorial;



@DenyAll
@Path("/cargas")
@ApplicationScoped
public class ModuloCargaAPI {
    @Inject
    private SecurityContext securityContext;

    @Inject 
    ServicioCarga serivcioCarga;
    
    @Inject 
    RepoCarga repoCarga;

    //funcion para verificar formato de cedula -> 1234567-8
    private boolean verificarFormatoCedula(String cedula) {
        return cedula != null && cedula.matches("\\d{7}-\\d");
    }

    //funcion para verificar formato de tarjeta
    private boolean verificarFormatoTarjeta(String numeroTarjeta){
        return numeroTarjeta !=null && numeroTarjeta.matches("\\d{8}");
    }
    

    /*
       el JSON para tarjeta sería:
        head-> http://localhost:8080/GestionDeMovilidad/movilidad/cargas/iniciar
        body-> 
        {
        "cargadorID": "3",
        "metodoPago": "TARJETA",
        "numeroTarjeta": "12345678"
        
        }

        Y para cuenta UTE:

        {
        "cargadorID" : 3,
        "metodoPago": "CUENTA_UTE"
        }
    */

    /*
    -Como requisito debes tener:
    -Un cliente creado (no puede ser abstracto, de algun tipo), ambos sirven para tarjeta, pero solo el comun para factura de ute
    -Debes tener el metodo de pago que desees probar, o sea una cuenta de UTE o Tarjeta, y debe estar asociada al cliente
    -Debes tener un Cargador creado para poder iniciar la carga
    */
    @POST
    @Path("/iniciar")
    @RolesAllowed("appMovil")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response iniciarCarga(iniciarCargaDatos datos) {    //"datos" es el json que me llega por http, el framwork jakarta lo traduce automaticamente al dto que yo le meta, podria trabajar con string, pero justamente el framework es para facilitarme esto
        //Verifico que me lleguen los datos
        if (datos == null || datos.getMetodoPago() == null) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Faltan datos para iniciar la carga\"}")
                .build();
        }

        // La cédula ahora proviene del usuario autenticado, no del JSON.
        String cedulaCliente = securityContext.getUserPrincipal().getName();
        String medioPagoString = datos.getMetodoPago();
        Integer idCargador = datos.getCargadorID();
        
        
        //VERIFICO QUE EL CARGADOR POR EL QUE ME PASA LA ID EXISTA
        Cargador cargadorAux = repoCarga.getCargador(idCargador);
        if (cargadorAux == null){
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity("{\"error\":\"No existe el cargador solicitado\"}")
                .build();
        }
        

        //<----VERIFICO QUE EL CLIENTE EXISTA ----->
        // Busca la copia del cliente mantenida por el módulo Carga.
        // La cédula no viene en el JSON: se obtiene desde SecurityContext.
        Cliente clienteBuscado = repoCarga.buscarPorCedula(cedulaCliente);
        if(clienteBuscado == null){
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity("{\"error\":\"El cliente autenticado no está sincronizado con el módulo Carga\"}")                
                .build();
        }

        //<-------VEO QUE NO TENGA DEUDA----->

        if (serivcioCarga.tieneDeuda(cedulaCliente)) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"El cliente tiene deudas pendientes\"}")
                .build();
        }


        //<----CASOS EN QUE PAGA CON CUENTA DE UTE ---->
        //Verifico que no coninfida que sea un cliente Profesional y que me pase una cuenta de ute, si pasa eso retorno error
        else if("CUENTA_UTE".equals(medioPagoString) && clienteBuscado instanceof ClienteProfesional){
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"El metodo de pago " + medioPagoString + " no es valido para el tipo de cliente seleccionado (Profesional)" + "\"}")
                .build();
        }
        //Si me envian una cuenta de ute y existe el cliente y no es cliente profesional creo la carga 
        else if("CUENTA_UTE".equals(medioPagoString) && (clienteBuscado instanceof ClienteComun)){
            //transformo el cliente que me llega en cliente comun para poder usar la funcion gerFormaPago
            ClienteComun clienteBuscadoComun = (ClienteComun)clienteBuscado; 
            //Verifico que tenga cuenta de ute antes de iniciar la carga
            if (clienteBuscadoComun.getFormaPago() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"El cliente no tiene una Cuenta UTE asociada\"}")
                .build();
            }
            //Si tiene cuenta de UTE inicio la carga
            serivcioCarga.iniciarCarga(clienteBuscado, clienteBuscadoComun.getFormaPago(), idCargador);
            return Response
                .status(Response.Status.CREATED)
                .entity("{\"mensaje\":\"Carga iniciada correctamente con Cuenta de UTE\"}")
                .build();
        }
        
        //<----CASPS EM QUE PAGA CON TARJETA ---->
        
        //Si me envian una tarjeta y existe el cliente verifico que la tarjeta exista para ese cliente
        else if("TARJETA".equals(medioPagoString)){
            String numeroTarjeta = datos.getNumeroTarjeta();
            if(verificarFormatoTarjeta(numeroTarjeta)){
                Tarjeta tarjetaCliente = repoCarga.buscarTarjetaClienteCI(cedulaCliente, numeroTarjeta);
                //Si la tarjeta no existe mando error
                if (tarjetaCliente == null){
                    return Response
                            .status(Response.Status.BAD_REQUEST)
                            .entity("{\"error\":\"El numero de tarjeta proporcionado y el cliente no coindiden\"}")
                            .build();
                }
                else{
                //Si la tarjeta existe mando exito
                //inicio la carga con la tarjeta que me pasaron
                serivcioCarga.iniciarCarga(clienteBuscado, tarjetaCliente, idCargador);
                return Response
                    .status(Response.Status.CREATED)
                    .entity("{\"mensaje\":\"Carga iniciada correctamente con Tarjeta\"}")
                    .build();
            }
            }else{
                return Response
                            .status(Response.Status.BAD_REQUEST)
                            .entity("{\"error\":\"El formato de tarjeta es erroneo, debe ser en el siguiente formato '12345678'\"}")
                            .build();
            }
            
    
        //<---- CASO BORDE, NO ES TARJETA NI CUENTA UTE ---->
        }
        else {
            return Response.status(Response.Status.BAD_REQUEST)
            .entity("{\"error\":\"El metodo de pago "
                    + medioPagoString + " no es valido\"}")
            .build();
        }
        
    }

        

    

    
    //Ya no necesita consumir nada porque la cedula no viene en el json sino en el curl con basic auth
    /*
    La llamada ahora sería: 
    curl -u '1234567-8:contraseña' http://localhost:8080/GestionDeMovilidad/movilidad/cargas/verCarga 
    */
    @GET
    @Path("verCarga")
    @RolesAllowed("appMovil")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verCargaActualCliente(){
        // La cédula ahora proviene del usuario autenticado, no del JSON.
        String cedulaCliente = securityContext.getUserPrincipal().getName();

    
        //Me fijo que tenga una Carga Actual Asociada
        Cliente clienteBuscado = repoCarga.buscarPorCedula(cedulaCliente);
        if(clienteBuscado == null){
            return Response
            .status(Response.Status.NOT_FOUND)
            .entity("{\"error\":\"El cliente autenticado no está sincronizado con el módulo Carga\"}")  
            .build();
        }
        else{
            Carga cargaClienteBuscado = serivcioCarga.verCargaActual(clienteBuscado);
            if(cargaClienteBuscado == null){
                return Response
                .status(Response.Status.NOT_FOUND)
                .entity("{\"error\":\"El cliente solicitano no tiene Carga Actual asociada" + "\"}")
                .build();
            }
            else{
                //creo un CargaDTO a partir de una carga gracias al contructor que hice, esto para poder retornarlo, sino si quisiera retornar el objeto como JSON de una explota y morimos todos
                CargaDTO cargaDTO = new CargaDTO(cargaClienteBuscado);
                return Response
                    .status(Response.Status.OK)
                    .entity(cargaDTO)
                    .build();
            }
        }
    }


    
    //Ya no necesita consumir nada porque la cedula no viene en el json sino en el curl con basic auth
    /*
    La llamada ahora sería: 
    curl -u '1234567-8:contraseña' http://localhost:8080/GestionDeMovilidad/movilidad/cargas/verHistorial    */
    @GET
    @Path("verHistorial")
    @LimitarHistorial
    @RolesAllowed("appMovil")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verHistorialCliente(){
        // La cédula ahora proviene del usuario autenticado, no del JSON.
        String cedulaCliente = securityContext.getUserPrincipal().getName();

        //Verifico que el cliente exista
        Cliente clienteBuscado = repoCarga.buscarPorCedula(cedulaCliente);
        if(clienteBuscado == null){
            return Response
            .status(Response.Status.NOT_FOUND)
            .entity("{\"error\":\"El cliente autenticado no está sincronizado con el módulo Carga\"}")            
            .build();
        }
        
        List<ElementoHistorial> listaDeCargas = repoCarga.buscarElementosHistorialPorCedula(cedulaCliente);
        //uso esta lista para poder mostrar el historial como JSON
        List<ElementoHistorialDTO> historialDTO = new ArrayList<>();
        for (ElementoHistorial elemento : listaDeCargas) {
            historialDTO.add(new ElementoHistorialDTO(elemento));
        }
       //quité el if que retornaba error si la lista estaba vacía, si no tiene cargas solo retorno una lista vacía
        return Response
            .status(Response.Status.OK)
            .entity(historialDTO)
            .build();
    
    }

    //Formato del curl:
    //curl -u '1234567-8:contraseña' http://localhost:8080/GestionDeMovilidad/movilidad/cargas/finalizarCargaActual
    @POST
    @Path("finalizarCargaActual")
    @RolesAllowed("appMovil")
    @Produces(MediaType.APPLICATION_JSON)
    public Response finalizarCargaActualCliente() {
        String cedulaCliente = securityContext.getUserPrincipal().getName();

        Cliente clienteBuscado = repoCarga.buscarPorCedula(cedulaCliente);

        if (clienteBuscado == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"El cliente autenticado no está sincronizado con el módulo Carga\"}")
                    .build();
        }
        //quedó medio desactualizado y mal el nombre, debería ser obtenerCargaActual, pero bueno, quedó así
        Carga cargaBuscada = serivcioCarga.verCargaActual(clienteBuscado);

        if (cargaBuscada == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"El cliente no tiene una carga actual asociada\"}")
                    .build();
        }

        if (cargaBuscada.getEstado() != EstadoCarga.ENPROGRESO) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\":\"La carga ya fue finalizada\"}")
                    .build();
        }
        //nesesito el tipo medio de pago
        Cliente clienteConHistorial = repoCarga.buscarConHistorialPorCedula(cedulaCliente);
        HistorialDeCargas historial = clienteConHistorial.getHistorialAsociado();

        MedioPago medioPago = null;

        for (ElementoHistorial elemento : historial.getHistorialCargas()) {
            if (elemento.getCarga().getId() == cargaBuscada.getId()) {
                medioPago = elemento.getMedioPago();
                break;
            }
        }
        if (medioPago == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"No se encontró el medio de pago de la carga\"}")
                    .build();
        }

        Cargador cargadorAsociado = cargaBuscada.getCargador();
        boolean resu = serivcioCarga.finalizarCarga(cargadorAsociado, cargaBuscada, 0,medioPago);

        if (resu) {
            return Response.ok()
                    .entity("{\"mensaje\":\"Carga finalizada correctamente y pago aceptado\"}")
                    .build();
        } else {
            return Response.ok()
                    .entity("{\"mensaje\":\"Carga finalizada correctamente. El pago fue rechazado y quedó una deuda pendiente.\"}")
                    .build();
        }

    }


    /*
    Llamada:
    curl -i -u "1111111-1:1234" -X POST \
    -H "Content-Type: application/json" \
    -d '{"numeroTarjeta":"11111111","monto":500}' \
    http://localhost:8080/GestionDeMovilidad/movilidad/pagos/pagarDeuda
    */
    @POST
    @Path("/pagarDeuda")
    @RolesAllowed("appMovil")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response pagarDeuda() {

        String cedula = securityContext.getUserPrincipal().getName();

        boolean resuDeuda = serivcioCarga.pagarDeuda(cedula);


        if (resuDeuda) {
            return Response.ok(
                    "{\"mensaje\":\"Deuda pagada correctamente\"}"
            ).build();
        }

        return Response.status(Response.Status.PAYMENT_REQUIRED)
                .entity("{\"error\":\"El pago fue rechazado\"}")
                .build();

    }
    




}
