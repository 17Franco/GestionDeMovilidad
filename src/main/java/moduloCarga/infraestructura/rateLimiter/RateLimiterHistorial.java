package moduloCarga.infraestructura.rateLimiter;


import java.time.Duration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RateLimiterHistorial {

    private Bucket bucket;
    private boolean activo;

    @PostConstruct
    public void inicializar() {
        activo = true;

        Bandwidth bucketConf = Bandwidth.builder()
                .capacity(3) // permite 3 consultas de golpe
                .refillGreedy(3, Duration.ofSeconds(50)) // recupera 3 tokens por minuto
                .build();

        bucket = Bucket.builder()
                .addLimit(bucketConf)
                .build();
    }

    public boolean consumir() {
        boolean result = bucket.tryConsume(1);
        System.out.println("Tokens restantes para verHistorial: " + bucket.getAvailableTokens());
        return result;
    }

    public void activarRateLimiter(boolean estado) {
        System.out.println("RateLimiter verHistorial estado: " + estado);
        this.activo = estado;
    }

    public boolean isActivo() {
        return activo;
    }
}