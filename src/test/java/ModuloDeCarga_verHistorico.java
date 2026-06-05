import jakarta.inject.Inject;

import moduloCarga.aplicacion.ServicioCarga;
import moduloCarga.aplicacion.impl.ServicioCargaImpl;
import moduloCarga.dominio.cliente.Cliente;
import moduloCarga.dominio.cliente.ClienteComun;
import moduloCarga.dominio.medioPago.CuentaUTE;
import moduloCarga.dominio.medioPago.MedioPago;
import moduloCarga.dominio.medioPago.Tarjeta;
import CargadorMock.aplicacion.Impl.CargadorInterfaceMOCKImpl;
import moduloCarga.infraestructura.persistencia.CargaRepoImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;


@EnableAutoWeld
@AddPackages({
    ServicioCargaImpl.class,
    CargadorInterfaceMOCKImpl.class,
    CargaRepoImpl.class
})
public class ModuloDeCarga_verHistorico {

    @Inject
    ServicioCarga servicioCargaImpl;

    @DisplayName("Test ver historico cargas del cliente")
    @Test
    void test() {
        // 1: Creo un cliente de moduloCarga de prueba
        Cliente clientePrueba = new ClienteComun();
        clientePrueba.setNombre("Gabriel");
        clientePrueba.setApellido("Aramburu");
        //2: creo el medio de pago
        MedioPago tarjeta = new Tarjeta();
        MedioPago cuentaUTE = new CuentaUTE();
        //3: creo una carga de prueba
        servicioCargaImpl.iniciarCarga(clientePrueba, tarjeta);
        servicioCargaImpl.iniciarCarga(clientePrueba, cuentaUTE);
        servicioCargaImpl.iniciarCarga(clientePrueba, tarjeta);
        //5: muestro la carga usando la interface
        String fechaInicio = "2026-05-01";
        String fechaFin = "2026-05-31"; 
        servicioCargaImpl.verHistorico(clientePrueba, fechaInicio, fechaFin);
    }
}