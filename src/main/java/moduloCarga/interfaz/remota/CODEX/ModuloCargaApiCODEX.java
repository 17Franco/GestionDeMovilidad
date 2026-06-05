/*package moduloCarga.interfaz.remota.CODEX;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import moduloCarga.aplicacion.ServicioCarga;
import moduloCarga.dominio.Carga;
import moduloCarga.dominio.ElementoHistorial;
import moduloCarga.dominio.HistorialDeCargas;
import moduloCarga.dominio.cliente.Cliente;
import moduloCarga.dominio.medioPago.MedioPago;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// URL http://localhost:8080/GestionDeMovilidad/movilidad/cargas
@Path("/cargas")
@DenyAll
@ApplicationScoped
public class ModuloCargaApiCODEX {

    @Inject
    private ServicioCarga servicioCarga;

    private final Map<String, Cliente> clientesConCarga = new ConcurrentHashMap<>();

    @POST
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/iniciar")
    public Response iniciarCarga(IniciarCargaDTOCODEX iniciarCargaDTO) {
        try {
            Cliente cliente = iniciarCargaDTO.getCliente().buildCliente();
            MedioPago medioPago = iniciarCargaDTO.getMedioPago().buildMedioPago();

            servicioCarga.iniciarCarga(cliente, medioPago);
            clientesConCarga.put(cliente.getCedula(), cliente);

            Carga cargaActual = cliente.getCargaActual();
            return Response
                    .status(Response.Status.CREATED)
                    .entity(cargaActual)
                    .build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/actual")
    public Response verCargaActual(@QueryParam("ci") String ci) {
        try {
            Cliente cliente = buscarClienteConCarga(ci);

            servicioCarga.verCargaActual(cliente);

            return Response
                    .status(Response.Status.OK)
                    .entity(cliente.getCargaActual())
                    .build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/historico")
    public Response verHistorico(
            @QueryParam("ci") String ci,
            @QueryParam("fechaIni") String fechaIni,
            @QueryParam("fechaFin") String fechaFin) {
        try {
            Cliente cliente = buscarClienteConCarga(ci);

            servicioCarga.verHistorico(cliente, fechaIni, fechaFin);

            List<ElementoHistorial> cargasEnFecha = filtrarHistorico(cliente, fechaIni, fechaFin);
            return Response
                    .status(Response.Status.OK)
                    .entity(cargasEnFecha)
                    .build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    private Cliente buscarClienteConCarga(String ci) {
        if (ci == null || ci.isBlank()) {
            throw new IllegalArgumentException("Debe enviar la cédula del cliente");
        }

        Cliente cliente = clientesConCarga.get(ci);
        if (cliente == null || cliente.getCargaActual() == null) {
            throw new IllegalArgumentException("No existe una carga para el cliente con cédula " + ci);
        }

        return cliente;
    }

    private List<ElementoHistorial> filtrarHistorico(Cliente cliente, String fechaIni, String fechaFin) {
        HistorialDeCargas historial = cliente.getHistorialAsociado();
        List<ElementoHistorial> cargasEnFecha = new ArrayList<>();

        if (historial == null || historial.getHisorialCargas() == null) {
            return cargasEnFecha;
        }

        LocalDate fechaInicio = LocalDate.parse(fechaIni);
        LocalDate fechaFinal = LocalDate.parse(fechaFin);

        for (ElementoHistorial elemento : historial.getHisorialCargas()) {
            LocalDate fechaCarga = elemento.getCarga().getFecha();
            if ((fechaCarga.isEqual(fechaInicio) || fechaCarga.isAfter(fechaInicio))
                    && (fechaCarga.isEqual(fechaFinal) || fechaCarga.isBefore(fechaFinal))) {
                cargasEnFecha.add(elemento);
            }
        }

        return cargasEnFecha;
    }
}
*/