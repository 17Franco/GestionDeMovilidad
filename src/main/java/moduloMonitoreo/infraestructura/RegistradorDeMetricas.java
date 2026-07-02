package moduloMonitoreo.infraestructura;

import java.time.Duration;
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
    // Valor actual de cargas activas que lee el Gauge
    private final AtomicInteger cargasActivas = new AtomicInteger();

    public static final String RECLAMOS_NEGATIVOS = "reclamosNegativos";
    // Counter para contar cuantos reclamos fueron etiquetados como negativos
    private Counter reclamosNegativos;

    public static final String PAGOS_CON_TARJETA = "pagosConTarjeta";
    // Counter para contar pagos procesados con tarjeta
    private Counter pagosConTarjeta;

    public static final String PAGOS_CON_CUENTA_UTE = "pagosConCuentaUte";
    // Counter para contar pagos procesados con tarjeta
    private Counter pagosConCuentaUte;

    public static final String ERRORES_PAGO_TARJETA = "erroresPagoTarjeta";
    // Counter para contar errores en pagos con tarjeta
    private Counter erroresPagoTarjeta;

    // Registry de Micrometer encargado de publicar las metricas registradas hacia InfluxDB (lo uso en Gauge o Counter)
    private InfluxMeterRegistry registry;

    

    @PostConstruct
    public void init() { //todo esto se inicia al correr wildfly:
        

        //<---- CONFIGURACION DE LA CONEXION CON INFLUXDB ---->
        InfluxConfig config = new InfluxConfig() {
            public String get(String key) { return null; }
            public Duration step() { return Duration.ofSeconds(10); } // Frecuencia con la que Micrometer publica las métricas en InfluxDB
            public String db() { return "metricasTallerJava"; }  // Nombre de la BD en Inlfux
        };

        registry = new InfluxMeterRegistry(config, Clock.SYSTEM);

        //Gauge = Medidor de Cargas (mide un valor actual que puede subir y bajar)
        Gauge.builder(CARGAS_ACTIVAS, cargasActivas, AtomicInteger::get)
                .register(registry);

        // Counter de Reclamos Negativos (cuenta eventos acumulados, solo sube)
        reclamosNegativos = Counter.builder(RECLAMOS_NEGATIVOS)
                .description("Cantidad de reclamos etiquetados como negativos")
                .register(registry);

        // Counter de Pagos con Tarjeta (cuenta eventos acumulados, solo sube)
        pagosConTarjeta = Counter.builder(PAGOS_CON_TARJETA)
                .description("Cantidad de pagos procesados con tarjeta")
                .register(registry);

        // Counter de Pagos con CuentaUte (cuenta eventos acumulados, solo sube)
        pagosConCuentaUte = Counter.builder(PAGOS_CON_CUENTA_UTE)
                .description("Cantidad de pagos procesados con Cuenta UTE")
                .register(registry);

        // Counter de Errores en Pago con Tarjeta (cuenta eventos acumulados, solo sube)
        erroresPagoTarjeta = Counter.builder(ERRORES_PAGO_TARJETA)
                .description("Cantidad de errores al procesar pagos con tarjeta")
                .register(registry);
    }


    //<--FUNCIONES AUXILIARES -->
    public void incrementarCargasActivas() {
        cargasActivas.incrementAndGet();
    }

    public void decrementarCargasActivas() {
        cargasActivas.updateAndGet(valor -> Math.max(0, valor - 1));
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
