package moduloCarga.interfaz.remota;

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
import moduloCarga.dominio.Carga;
import moduloCarga.dominio.Cargador;
import moduloCarga.dominio.cliente.Cliente;
import moduloCarga.dominio.cliente.ClienteComun;
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

    //funcion para verificar formato de cedula -> 1234567-8
    private boolean verificarFormatoCedula(String cedula) {
        return cedula != null && cedula.matches("\\d{7}-\\d");
    }

    //funcion para verificar formato de tarjeta
    private boolean verificarFormatoTarjeta(String numeroTarjeta){
        return numeroTarjeta !=null && numeroTarjeta.matches("\\d{8}");
    }
    
    
    //MANDAR EL ID DEL CARGADOOOOOOR
    //CONTROLAR ESOOOOOOOOOOOOOOOOOOOOOOOOOOOO
    /*
       el JSON para tarjeta sería:
        head-> http://localhost:8080/GestionDeMovilidad/movilidad/cargas/iniciar
        body-> 
        {
        "cedulaCliente": "1234567-8",
        "cargadorID": "3",
        "metodoPago": "TARJETA",
        "numeroTarjeta": "12345678"
        
        }

        Y para cuenta UTE:

        {
        "cedulaCliente": "1234567-8",
        "cargadorID" : 3,
        "metodoPago": "CUENTA_UTE"
        }
    */

    @GET
    @Path("/info")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerInfo(){
        return Response
                .status(Response.Status.OK)
                .entity("{\"Para usar la URL 'http://localhost:8080/GestionDeMovilidad/movilidad/cargas/iniciar' \":\"\"}")
                .build();
    }

    /*
    -Como requisito debes tener:
    -Un cliente creado (no puede ser abstracto, de algun tipo), ambos sirven para tarjeta, pero solo el comun para factura de ute
    -Debes tener el metodo de pago que desees probar, o sea una cuenta de UTE o Tarjeta, y debe estar asociada al cliente
    -Debes tener un Cargador creado para poder iniciar la carga
    */
    @POST
    @Path("/iniciar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response iniciarCarga(iniciarCargaDatos datos) {               //"datos" es el json que me llega por http, el framwork jakarta lo traduce automaticamente al dto que yo le meta, podria trabajar con string, pero justamente el framework es para facilitarme esto
        String cedulaCliente = datos.getCedulaCliente();
        String medioPagoString = datos.getMetodoPago();
        Integer idCargador = datos.getCargadorID();
        
        //VERIFICO QUE EL CARGADOR POR EL QUE ME PASA LA ID EXISTA
        Cargador cargadorAux = repoCarga.getCargador(idCargador);
        if (cargadorAux == null){
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity("{\"error\":\"No existe el cargador del cual manda ID "+ "\"}")
                .build();
        }
        
        if (cedulaCliente == null || !verificarFormatoCedula(cedulaCliente)){
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"El formato de cedula enviado no es correcto, el formato esperado es '1234567-8'" + "\"}")
                .build();
        }

        //<----VERIFICO QUE EL CLIENTE EXISTA ----->
        //Busco en la bd el Cliente que me pasan por cedula en el campo "cedulaCliente" del JSON, si existe lo guardo en una variable, sino retorno error 
        Cliente clienteBuscado = repoCarga.buscarPorCedula(cedulaCliente);
        if(clienteBuscado == null){
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity("{\"error\":\"No existe cliente con cédula " + cedulaCliente + "\"}")
                .build();
        }
        //<----CASOS EN QUE PAGA CON CUENTA DE UTE ---->
        //Verifico que no coninfida que sea un cliente Profesional y que me pase una cuenta de ute, si pasa eso retorno error
        else if(medioPagoString.equals("CUENTA_UTE") && clienteBuscado instanceof ClienteProfesional){
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"El metodo de pago " + medioPagoString + " no es valido para el tipo de cliente seleccionado (Profesional)" + "\"}")
                .build();
        }
        //Si me envian una cuenta de ute y existe el cliente y no es cliente profesional creo la carga 
        else if(medioPagoString.equals("CUENTA_UTE") && (clienteBuscado instanceof ClienteComun)){
            //transformo el cliente que me llega en cliente comun para poder usar la funcion gerFormaPago
            ClienteComun clienteBuscadoComun = (ClienteComun)clienteBuscado; 
            serivcioCarga.iniciarCarga(clienteBuscado, clienteBuscadoComun.getFormaPago(), idCargador);
            return Response
                .status(Response.Status.CREATED)
                .entity("{\"mensaje\":\"Carga iniciada correctamente con Cuenta de UTE\"}")
                .build();
        }
        
        //<----CASPS EM QUE PAGA CON TARJETA ---->
        
        //Si me envian una tarjeta y existe el cliente verifico que la tarjeta exista para ese cliente
        else if(medioPagoString.equals("TARJETA")){
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
                            .entity("{\"error\":\"El formato de tarjeta es erroneo, debe ser en el siguiente formato '1234567-8'\"}")
                            .build();
            }
            
    
        //<---- CASO BORDE, NO ES TARJETA NI CUENTA UTE ---->
        }
        else if(!(medioPagoString.equals("TARJETA") && medioPagoString.equals("CUENTA_UTE"))){
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"El metodo de pago " + medioPagoString + " no es valido" + "\"}")
                .build();
            
        }
        //No deberia pasar esto
        else{
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Error inesperado, llame a soporte tecnico" + "\"}")
                .build();
            
        }

}

    /*
    head-> http://localhost:8080/GestionDeMovilidad/movilidad/cargas/verCarga
    body->{
            "cedulaCliente" : "1234567-8"
            }


    Para usarlo debes tener
    -Un cliente creado (cualquier tipo)
    -Una carga Creada 
    -Asociar la carga al cliete
    */

    @GET
    @Path("verCarga")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verCargaActualCliente(verCargaActualDatos datos){
        String cedulaCliente = datos.getCedulaCliente();

        //Me fijo que me pase un numero de cedula con formato correcto
        if (!verificarFormatoCedula(cedulaCliente)){
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"El formato de cedula enviado no es correcto, el formato esperado es '1234567-8'" + "\"}")
                .build();
        }
        //Si el formato es correcto me fijo que tenga una Carga Actual Asociada
        Cliente clienteBuscado = repoCarga.buscarPorCedula(cedulaCliente);
        if(clienteBuscado == null){
            return Response
            .status(Response.Status.NOT_FOUND)
            .entity("{\"error\":\"El cliente solicitado no existe" + "\"}")
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



    
    
    @GET
    @Path("verHistorial")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verHistorialCliente(verHistorialClienteDatos datos){
        String cedulaCliente = datos.getCedulaCliente();

        if (!verificarFormatoCedula(cedulaCliente)){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"El formato de cedula enviado no es correcto, el formato esperado es '1234567-8'" + "\"}")
                    .build();
        }
        else{
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"El formato de cedula enviado no es correcto, el formato esperado es '1234567-8'" + "\"}")
                    .build();
        }
    
    
    
    }




}