package moduloPago.aplicacion.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import moduloCarga.dominio.cliente.Cliente;
import moduloCarga.dominio.repositorio.RepoCarga;
import moduloPago.aplicacion.ServicioPago;
import moduloPago.dominio.Estado;
import moduloPago.dominio.Pago;
import moduloPago.dominio.repositorio.RepoPago;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class ServicioPagoImpl implements ServicioPago {

    private static final String URL_AUTORIZAR_PAGO = "http://localhost:8080/ServicioMedioPagoMock/api/medioPago/autorizar";
    private static  final  String URL_PAGO_CUENTA_UTE = "http://localhost:8080/MockPagoCuentaUte/api/medioPago/pagar";
    @Inject
    private RepoPago repo;

    @Override
    public boolean pagarConTarjeta(String clienteId,int idCarga, String numeroTarjeta, float monto) {
        String clienteIdNormalizado = normalizarClienteId(clienteId);
        validarDatosPagoTarjeta(clienteIdNormalizado, numeroTarjeta, monto);

        String body = """
                {
                  "clienteId": "%s",
                  "numeroTarjeta": "%s",
                  "monto": %s
                }
                """.formatted(clienteIdNormalizado, numeroTarjeta, Float.toString(monto));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_AUTORIZAR_PAGO))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            //CREO OBJETO PAGO
            Pago pago = new Pago();
            pago.setCedulaCliente(clienteIdNormalizado);
            pago.setIdCarga(idCarga);
            pago.setMonto(monto);
            pago.setMedioPago("TARJETA");
            pago.setFecha(LocalDate.now());
            if(response.statusCode() == 200){
                pago.setEstado(Estado.ACEPTADO);
            }else {
                pago.setEstado(Estado.RECHAZADO);
            }

            //mando a guardarlo
            repo.save(pago);

            return response.statusCode() == 200;
        } catch (IOException e) {
            throw new RuntimeException("No se pudo conectar con el servicio externo de medio de pago", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Se interrumpio la autorizacion del pago con tarjeta", e);
        }
    }

    private String normalizarClienteId(String clienteId) {
        if (clienteId == null) {
            return null;
        }

        if (clienteId.matches("\\d{8}")) {
            return clienteId.substring(0, 7) + "-" + clienteId.substring(7);
        }

        return clienteId;
    }

    private void validarDatosPagoTarjeta(String clienteId, String numeroTarjeta, float monto) {
        if (clienteId == null || !clienteId.matches("\\d{7}-\\d")) {
            throw new IllegalArgumentException("El clienteId debe tener formato 1234567-8");
        }

        if (numeroTarjeta == null || !numeroTarjeta.matches("\\d{8}")) {
            throw new IllegalArgumentException("El numeroTarjeta debe tener 8 digitos");
        }

        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }
    }

    @Override
    public List<Pago> consultarPagos(String cedulaCliente, LocalDate fechaIni, LocalDate fechaFin){

        return repo.getPagosPorFecha(cedulaCliente,fechaIni,fechaFin);
    }

    public boolean tieneDeuda(String clienteId){

        return repo.deuda(normalizarClienteId(clienteId));
    }
    /*
    @Override
    @Transactional
    public boolean pagarConTarjeta(String cedulaCliente, String numeroTarjeta, float monto) {
    /*
        Cliente cliente = repoCarga.buscarPorCedula(cedulaCliente);//No se puede usar aca

        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no existe");
        }

        boolean autorizado = pagarConTarjetaServicioExterno(
                cedulaCliente,
                numeroTarjeta,
                monto
        );

        // Rechazado: se activa la deuda.
        // Aprobado: se elimina cualquier deuda.
        cliente.setDeudaActiva(!autorizado);
        repoCarga.ActualizarCliente(cliente);

        return autorizado;


        return false;
    }
    */

    //Siempre me cambia el estado de la deuda de true false
    @Override
    @Transactional
    public boolean pagarDeuda(String cedulaCliente,int idCarga,String numeroTarjeta, float monto) {
        //podria llamar directo a este metodo pero queda mas claro que se paga ddeuda asi
        return pagarConTarjeta(cedulaCliente,idCarga,numeroTarjeta,monto);
    }

    public boolean pagarConCuentUte(String clienteId,int idCarga, String numeroCuenta, float monto){
        String clienteIdNormalizado = normalizarClienteId(clienteId);
        String body = """
                {
                  "cuentaUte": "%s",
                  "monto": "%s",
                  "clienteID": "%s"
                }
                """.formatted(numeroCuenta, Float.toString(monto),clienteIdNormalizado);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_PAGO_CUENTA_UTE))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        try {
            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            //CREO OBJETO PAGO
            Pago pago = new Pago();
            pago.setCedulaCliente(clienteIdNormalizado);
            pago.setIdCarga(idCarga);
            pago.setMonto(monto);
            pago.setMedioPago("CUENTA_UTE");
            pago.setFecha(LocalDate.now());
            if(response.statusCode() == 201){
                pago.setEstado(Estado.ACEPTADO);
            }else {
                pago.setEstado(Estado.RECHAZADO);
            }

            //mando a guardarlo
            repo.save(pago);

            return response.statusCode() == 201;
        } catch (IOException e) {
            throw new RuntimeException("No se pudo conectar con el servicio externo de medio de pago", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Se interrumpio la autorizacion del pago con tarjeta", e);
        }
    }
}
