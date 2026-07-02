package moduloCliente.aplicacion.impl;

import jakarta.enterprise.context.ApplicationScoped;
import moduloCliente.aplicacion.ClasificadorReclamos;
import moduloCliente.dominio.TipoReclamo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@ApplicationScoped
public class ClasificadorReclamosImpl implements ClasificadorReclamos {

    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private static final String MODELO = "qwen2.5:0.5b";

    private final HttpClient client = HttpClient.newHttpClient();

    @Override
    public TipoReclamo clasificar(String descripcion) {
        try {
            String prompt = """
                    Clasifica el siguiente reclamo como POSITIVO, NEUTRAL o NEGATIVO.
                    Responde solamente una palabra: POSITIVO, NEUTRAL o NEGATIVO.

                    Reclamo:
                    %s
                    """.formatted(descripcion);

            String body = """
                    {
                      "model": "%s",
                      "prompt": "%s",
                      "stream": false
                    }
                    """.formatted(MODELO, escaparJson(prompt));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(OLLAMA_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return interpretarRespuesta(response.body());

        } catch (Exception e) {
            throw new RuntimeException("Error clasificando reclamo con Ollama", e);
        }
    }

    private TipoReclamo interpretarRespuesta(String body) {
        String upper = body.toUpperCase();

        if (upper.contains("NEGATIVO")) {
            return TipoReclamo.NEGATIVO;
        }
        if (upper.contains("POSITIVO")) {
            return TipoReclamo.POSITIVO;
        }

        return TipoReclamo.NEUTRAL;
    }

    private String escaparJson(String texto) {
        return texto
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");
    }
}