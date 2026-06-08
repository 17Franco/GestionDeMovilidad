package moduloPago.aplicacion.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import moduloPago.aplicacion.ServicioPago;
import moduloPago.dominio.pagoRealizado;
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

    @Inject
    private RepoPago repo;

    @Override
    public void pagarCarga(
            String cedulaCliente,
            int idCarga,
            float importe,
            String medioPago
    ){

        pagoRealizado pago = new pagoRealizado();

        pago.setCedulaCliente(cedulaCliente);
        pago.setIdCarga(idCarga);
        pago.setMonto(importe);
        pago.setFecha(LocalDate.now());
        pago.setMedioPago(medioPago);

        System.out.println("Pago realizado correctamente");
    }

    @Override
    public boolean pagarConTarjetaServicioExterno(String clienteId, String numeroTarjeta, float monto) {
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
   public List<pagoRealizado> consultarPagos(String cedulaCliente, LocalDate fechaIni, LocalDate fechaFin){

        return repo.getPagosPorFecha(cedulaCliente,fechaFin,fechaFin);
    }
}
