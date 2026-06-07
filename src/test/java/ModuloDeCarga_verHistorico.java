import jakarta.inject.Inject;

import moduloCarga.aplicacion.ServicioCarga;
import moduloCarga.aplicacion.impl.ServicioCargaImpl;
import moduloCarga.dominio.Carga;
import moduloCarga.dominio.cliente.Cliente;
import moduloCarga.dominio.cliente.ClienteComun;
import moduloCarga.dominio.medioPago.CuentaUTE;
import moduloCarga.dominio.medioPago.MedioPago;
import moduloCarga.dominio.medioPago.Tarjeta;
import moduloCarga.infraestructura.persistencia.CargaRepoImpl;
import org.junit.jupiter.api.Test;

import FuncionalidadCargadorMOCK.aplicacion.Impl.FuncionalidadCargadorInterfaceMOCKImpl;

import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;


@EnableAutoWeld
@AddPackages({
    ServicioCargaImpl.class,
    FuncionalidadCargadorInterfaceMOCKImpl.class,
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
        servicioCargaImpl.iniciarCarga(clientePrueba, tarjeta,2);
        servicioCargaImpl.iniciarCarga(clientePrueba, cuentaUTE,2);
        servicioCargaImpl.iniciarCarga(clientePrueba, tarjeta,2);
        //5: muestro la carga usando la interface
        String fechaInicio = "2026-05-01";
        String fechaFin = "2026-05-31"; 
        servicioCargaImpl.verHistorico(clientePrueba, fechaInicio, fechaFin);
    }
}