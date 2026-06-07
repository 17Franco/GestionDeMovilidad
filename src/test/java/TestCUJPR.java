import jakarta.inject.Inject;

import moduloCarga.aplicacion.ServicioCarga;
import moduloCarga.aplicacion.impl.ServicioCargaImpl;

import moduloCarga.dominio.Carga;
import moduloCarga.dominio.Cargador;

import moduloCarga.dominio.cliente.Cliente;
import moduloCarga.dominio.cliente.ClienteComun;

import moduloCarga.dominio.medioPago.MedioPago;
import moduloCarga.dominio.medioPago.Tarjeta;

import moduloCarga.infraestructura.persistencia.CargaRepoImpl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import FuncionalidadCargadorMOCK.aplicacion.Impl.FuncionalidadCargadorInterfaceMOCKImpl;

import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;

@EnableAutoWeld
@AddPackages({
        ServicioCargaImpl.class,
        CargaRepoImpl.class,
        FuncionalidadCargadorInterfaceMOCKImpl.class
})
public class TestCUJPR {

    @Inject
    ServicioCarga servicioCargaImpl;

    private Cliente cliente = new ClienteComun();

    private MedioPago medioPago = new Tarjeta();

    private Cargador cargador = new Cargador();

    public void cargarDatosTest() {

        cliente.setNombre("Gabriel");
        cliente.setApellido("Aramburu");
    }

    @DisplayName("Test finalizar carga")
    @Test
    void test() {

        cargarDatosTest();

        // inicia una carga
        servicioCargaImpl.iniciarCarga(cliente, medioPago);

        // obtiene la carga actual
        Carga carga = cliente.getCargaActual();

        // finaliza la carga
        servicioCargaImpl.finalizarCarga(cargador, carga, 100);

        // muestra datos finales
        System.out.println(carga);

        System.out.println("Test ejecutado correctamente");
    }
}