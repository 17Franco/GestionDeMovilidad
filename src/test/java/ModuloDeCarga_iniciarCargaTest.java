import jakarta.inject.Inject;

import moduloCarga.aplicacion.ServicioCarga;
import moduloCarga.aplicacion.impl.ServicioCargaImpl;
import moduloCarga.dominio.EstadoCarga;
import moduloCarga.dominio.cliente.Cliente;
import moduloCarga.dominio.cliente.ClienteComun;
import moduloCarga.dominio.medioPago.CuentaUTE;
import moduloCarga.dominio.medioPago.MedioPago;
import moduloCarga.dominio.medioPago.Tarjeta;
import moduloCarga.infraestructura.persistencia.CargaRepoImpl;
import org.junit.jupiter.api.Test;

import FuncionalidadCargadorMOCK.aplicacion.Impl.FuncionalidadCargadorInterfaceMOCKImpl;

import org.junit.jupiter.api.DisplayName;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;


@EnableAutoWeld
@AddPackages({
    ServicioCargaImpl.class,
    FuncionalidadCargadorInterfaceMOCKImpl.class,
    CargaRepoImpl.class
})
public class ModuloDeCarga_iniciarCargaTest {

    @Inject
    ServicioCarga servicioCargaImpl;

    //creo un cliente de moduloCarga
    private Cliente clientePrueba = new ClienteComun();

    //creo los 2 metodos de pago
    private MedioPago medioPago = new Tarjeta();
    //private MedioPago medioPago = new CuentaUTE();


    public void cargarDatosTest(){
        clientePrueba.setNombre("Gabriel");
        clientePrueba.setApellido("Arabmuru");
    }

    @DisplayName("Test iniciarCarga")
	@Test
    void test(){
        cargarDatosTest();
        servicioCargaImpl.iniciarCarga(clientePrueba, medioPago, 2);
        System.out.print("El cliente " + clientePrueba.getNombre() + " " + clientePrueba.getApellido() +
            " inició una carga" + "\n" + "Realizó el pago con " + medioPago + "\n");
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

