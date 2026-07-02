package moduloMonitoreo.infraestructura;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.influx.InfluxConfig;
import io.micrometer.influx.InfluxMeterRegistry;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RegistradorDeMetricas {
    //<----- METRICAS QUE SE PUBLICAN EN INFLUXDB ------->

    public static final String CARGAS_ACTIVAS = "cargasActivas";
    private final AtomicInteger cargasActivas = new AtomicInteger();

    public static final String RECLAMOS_NEGATIVOS = "reclamosNegativos";
    private Counter reclamosNegativos;

    public static final String PAGOS_CON_TARJETA = "pagosConTarjeta";
    private Counter pagosConTarjeta;

    public static final String PAGOS_CON_CUENTA_UTE = "pagosConCuentaUte";
    private Counter pagosConCuentaUte;

    public static final String ERRORES_PAGO_TARJETA = "erroresPagoTarjeta";
    // Counter para contar errores en pagos con tarjeta
    private Counter erroresPagoTarjeta;

    // NUEVO: Nombre de la métrica para el total de cargas finalizadas con éxito
    public static final String CARGAS_REALIZADAS = "cargasRealizadas";
    private Counter cargasRealizadas;

    // NUEVO: Set thread-safe para rastrear qué IDs de carga ya contamos y evitar duplicados
    private final Set<Integer> idsCargasProcesadas = ConcurrentHashMap.newKeySet();

    private InfluxMeterRegistry registry;

    @PostConstruct
    public void init() {
        InfluxConfig config = new InfluxConfig() {
            public String get(String key) { return null; }
            public Duration step() { return Duration.ofSeconds(10); }
            public String db() { return "metricasTallerJava"; }
        };

        registry = new InfluxMeterRegistry(config, Clock.SYSTEM);

        Gauge.builder(CARGAS_ACTIVAS, cargasActivas, AtomicInteger::get)
                .register(registry);

        reclamosNegativos = Counter.builder(RECLAMOS_NEGATIVOS)
                .description("Cantidad de reclamos etiquetados como negativos")
                .register(registry);

        pagosConTarjeta = Counter.builder(PAGOS_CON_TARJETA)
                .description("Cantidad de pagos procesados con tarjeta")
                .register(registry);

        pagosConCuentaUte = Counter.builder(PAGOS_CON_CUENTA_UTE)
                .description("Cantidad de pagos procesados con Cuenta UTE")
                .register(registry);

        // Counter de Errores en Pago con Tarjeta (cuenta eventos acumulados, solo sube)
        erroresPagoTarjeta = Counter.builder(ERRORES_PAGO_TARJETA)
                .description("Cantidad de errores al procesar pagos con tarjeta")
                .register(registry);
    

        // NUEVO: Registrar el Counter de cargas realizadas en Micrometer
        cargasRealizadas = Counter.builder(CARGAS_REALIZADAS)
                .description("Total de cargas finalizadas correctamente")
                .register(registry);
    }

    //<--FUNCIONES AUXILIARES -->
    public void incrementarCargasActivas() {
        cargasActivas.incrementAndGet();
    }

    public void decrementarCargasActivas() {
        cargasActivas.updateAndGet(valor -> Math.max(0, valor - 1));
    }

    // NUEVO: Método para registrar la carga realizada garantizando idempotencia
    public void registrarCargaRealizada(int idCarga) {
        // .add() devuelve true solo si el ID NO existía previamente en el Set
        if (idsCargasProcesadas.add(idCarga)) {
            cargasRealizadas.increment();
        }
    }

    // NUEVO: Getter útil para las aserciones de tus Tests
    public double getCantidadCargasRealizadas() {
        return cargasRealizadas.count();
    }

    public void registrarReclamoNegativo() {
        reclamosNegativos.increment();
    }

    public void registrarPagoConTarjeta() {
        pagosConTarjeta.increment();
    }

    public void registrarPagoConCuentaUte() {
        pagosConCuentaUte.increment();
    }

    public void registrarErrorPagoTarjeta() {
        erroresPagoTarjeta.increment();
    }

    @PreDestroy
    public void cerrar() {
        registry.close();
    }
}