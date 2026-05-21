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
import CargadorMock.aplicacion.CargadorInterfaceMOCK;
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

    @DisplayName("Test ver carga actual")
    @Test
    void test() {
        // 1: Creo un cliente de moduloCarga de prueba
        Cliente clientePrueba = new ClienteComun();
        clientePrueba.setNombre("Gabriel");
        clientePrueba.setApellido("Aramburu");
        //2: creo el medio de pago
        MedioPago tarjeta = new Tarjeta();
        //3: creo una carga de prueba
        Carga cargaPrueba = servicioCargaImpl.iniciarCarga(clientePrueba, tarjeta);
        //4: seteo la carga al cliente
        clientePrueba.setCargaActual(cargaPrueba);
        //3: traigo la carga utilizando la interface
        Carga cargaActual = servicioCargaImpl.verCargaActual(clientePrueba);

        System.out.println(cargaActual);
    }
}