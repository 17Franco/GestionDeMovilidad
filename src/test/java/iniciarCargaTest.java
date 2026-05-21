import jakarta.inject.Inject;

import moduloCarga.aplicacion.ServicioCarga;
import moduloCarga.aplicacion.impl.ServicioCargaImpl;
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
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;


@EnableAutoWeld
@AddPackages({
    ServicioCargaImpl.class,
    CargadorInterfaceMOCKImpl.class,
    CargaRepoImpl.class
})
public class iniciarCargaTest {

    @Inject
    ServicioCarga servicioCargaImpl;

    //creo un cliente de moduloCarga
    private Cliente clientePrueba = new ClienteComun();

    //creo los 2 metodos de pago
    private MedioPago tarjeta = new Tarjeta();
    private MedioPago cuentaUTE = new CuentaUTE();


    public void cargarDatosTest(){
        clientePrueba.setNombre("Gabriel");
        clientePrueba.setApellido("Arabmuru");
    }

    @DisplayName("Test iniciarCarga")
	@Test
    void test(){
        cargarDatosTest();
        servicioCargaImpl.iniciarCarga(clientePrueba, tarjeta);
        if (clientePrueba.getCargaActual().getEstado() == EstadoCarga.ENPROGRESO){
            System.out.print("La carga está en progreso");
        }
        else if(clientePrueba.getCargaActual().getEstado() == EstadoCarga.TERMINADO){
            System.out.print("La carga finalizó");
        }
        else{
            System.out.print("Error inesperado, llame a soporte");
        }
    }

    




}

