package moduloMonitoreo.infraestructura;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.influx.InfluxConfig;
import io.micrometer.influx.InfluxMeterRegistry;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RegistradorDeMetricas {
    public static final String CARGAS_ACTIVAS = "cargasActivas";        //Similar a una columna pero de influxDB donde guardo los las cargas activas

    private final AtomicInteger cargasActivas = new AtomicInteger();
    private InfluxMeterRegistry registry;

    @PostConstruct
    public void init() {
        InfluxConfig config = new InfluxConfig() {
            public String get(String key) { return null; }
            public Duration step() { return Duration.ofSeconds(10); } // Frecuencia con la que Micrometer publica las métricas en InfluxDB
            public String db() { return "metricasTallerJava"; }             // Nombre de la BD en Inlfux
        };

        registry = new InfluxMeterRegistry(config, Clock.SYSTEM);

        Gauge.builder(CARGAS_ACTIVAS, cargasActivas, AtomicInteger::get)
                .register(registry);
    }

    public void incrementarCargasActivas() {
        cargasActivas.incrementAndGet();
    }

    public void decrementarCargasActivas() {
        cargasActivas.updateAndGet(valor -> Math.max(0, valor - 1));
    }

    @PreDestroy
    public void cerrar() {
        registry.close();
    }
}
