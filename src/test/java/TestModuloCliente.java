import jakarta.inject.Inject;
import moduloCarga.aplicacion.ServicioCarga;
import moduloCarga.aplicacion.impl.ServicioCargaImpl;
import moduloCarga.infraestructura.persistencia.CargaRepoImpl;
import moduloCarga.interfaz.evento.in.ObserverModuloCarga;
import moduloCliente.aplicacion.ServicioCliente;
import moduloCliente.aplicacion.impl.ServicioClienteImpl;
import moduloCliente.dominio.TipoProfesional;
import moduloCliente.dominio.cliente.ClienteComun;
import moduloCliente.dominio.cliente.ClienteProfesional;
import moduloCliente.infraestructura.persistencia.ClienteRepositorioImpl;
import moduloCliente.interfaz.evento.out.ClienteNuevoClienteComun;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import FuncionalidadCargadorMOCK.aplicacion.Impl.FuncionalidadCargadorInterfaceMOCKImpl;

@EnableAutoWeld
@AddPackages({
        ServicioClienteImpl.class,
        ClienteNuevoClienteComun.class,
        ClienteRepositorioImpl.class,
        CargaRepoImpl.class,
        ObserverModuloCarga.class,
        ServicioCarga.class,
        FuncionalidadCargadorInterfaceMOCKImpl.class,
})
public class TestModuloCliente {
    @Inject
    private ServicioCliente servicios;

    @Inject
    private ServicioCarga serviciosCarga;

    @DisplayName("Test creo Cliente")
    @Test
    void test(){
        ClienteComun cliente = new ClienteComun("12345678", "Franco", "Echaide", "099123456", "1234");
        ClienteProfesional clienteP = new ClienteProfesional(
                "12345679",
                "Franco",
                "Echaide",
                "099123456",
                "1234",
                TipoProfesional.PREMIUM,
                15.0f
        );
        servicios.registrarCliente(clienteP);

        servicios.registrarCliente(cliente);
        //aca pruebo de que si se asigna el reclamo al cliente
        //servicios.realizarReclamo("probando","Hola programa de porqueria?","12345679");
        //pruebo que si se agrego cliente al repo de moduloCliente
        servicios.obtenerClientes();

        //pruebo que si se agrego cliente al repo de moduloCarga
        //aca verifico que se lanzan los eventos y que el observer fuciona
        serviciosCarga.obtenerClientes();


    }
}
