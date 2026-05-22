import jakarta.inject.Inject;

import moduloCarga.aplicacion.ServicioCarga;
import moduloCarga.aplicacion.impl.ServicioCargaImpl;

import moduloCarga.dominio.Cargador;
import moduloCarga.dominio.EstacionCarga;

import moduloCarga.infraestructura.persistencia.CargaRepoImpl;

import CargadorMock.aplicacion.Impl.CargadorInterfaceMOCKImpl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;

@EnableAutoWeld
@AddPackages({
        ServicioCargaImpl.class,
        CargaRepoImpl.class,
        CargadorInterfaceMOCKImpl.class
})
public class AltaCargaTest {

    @Inject
    ServicioCarga servicioCargaImpl;

    private EstacionCarga estacion = new EstacionCarga();

    private Cargador cargador = new Cargador();

    public void cargarDatosTest() {

        estacion.setDescripcion("Estacion Punta Shopping");
        estacion.setCalle("Roosevelt");
    }

    @DisplayName("Test alta estacion y cargador")
    @Test
    void test() {

        cargarDatosTest();

        servicioCargaImpl.altaEstacion(estacion);

        servicioCargaImpl.altaCargador(cargador);

        System.out.println("Test ejecutado correctamente");
    }
}