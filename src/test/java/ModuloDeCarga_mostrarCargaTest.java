import jakarta.inject.Inject;

import moduloCarga.aplicacion.ServicioCarga;
import moduloCarga.aplicacion.impl.ServicioCargaImpl;
import moduloCarga.dominio.Carga;
import moduloCarga.dominio.EstadoCarga;
import moduloCarga.dominio.cliente.Cliente;
import moduloCarga.dominio.cliente.ClienteComun;
import moduloCarga.dominio.medioPago.CuentaUTE;
import moduloCarga.dominio.medioPago.MedioPago;
import moduloCarga.dominio.medioPago.Tarjeta;

import CargadorMock.aplicacion.Impl.CargadorInterfaceMOCKImpl;
import moduloCarga.infraestructura.persistencia.CargaRepoImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;


@EnableAutoWeld
@AddPackages({
    ServicioCargaImpl.class,
    CargadorInterfaceMOCKImpl.class,
    CargaRepoImpl.class
})
public class ModuloDeCarga_mostrarCargaTest {

    @Inject
    ServicioCarga servicioCargaImpl;
    // 1: Creo un cliente de moduloCarga de prueba
    private Cliente clientePrueba = new ClienteComun();

    // 2: creo una funcion para cargar los datos de prueba
    private Cliente cargarDatosPrueba() {
        //2.1: creo un cliente de moduloCarga auxiliar que luego es el que retorno 
        Cliente clienteAux = new ClienteComun();
        clienteAux.setNombre("Gabriel");
        clienteAux.setApellido("Aramburu");

        // 2.2: creo los valores que completarán la carga de prueba
        LocalDate fecha = LocalDate.of(2026, 5, 20);
        LocalDateTime horaInicio = LocalDateTime.of(2026, 5, 20, 18, 30);
        LocalDateTime horaFin = null; // no ha terminado
        float importeTotal = 500f;
        float recargoPorDemora = 0f;
        float porcentajeAvance = 37.5f;
        LocalDateTime horaEstimadaFin = horaInicio.plusHours(2);
        EstadoCarga estado = EstadoCarga.ENPROGRESO;

        // 2.3: creo una carga de prueba
        Carga cargaPrueba = new Carga(
                fecha,
                horaInicio,
                horaFin,
                importeTotal,
                recargoPorDemora,
                porcentajeAvance,
                horaEstimadaFin,
                estado,
                clienteAux
        );

        // 2.4: seteo la carga de prueba en el cliente de prueba
        clienteAux.setCargaActual(cargaPrueba);

        return clienteAux;
    }

    @DisplayName("Test ver carga actual")
    @Test
    void test() {
        clientePrueba = cargarDatosPrueba();

        //3: traigo la carga utilizando la interface
        Carga cargaActual = servicioCargaImpl.verCargaActual(clientePrueba);

        System.out.println(cargaActual);
    }
}