
import FuncionalidadCargadorMOCK.aplicacion.Impl.FuncionalidadCargadorInterfaceMOCKImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.inject.Inject;
import moduloCarga.aplicacion.ServicioCarga;
import moduloCarga.infraestructura.persistencia.CargaRepoImpl;
import moduloCarga.interfaz.evento.in.ObserverModuloCarga;
import moduloCliente.aplicacion.ServicioCliente;
import moduloCliente.aplicacion.impl.ServicioClienteImpl;
import moduloCliente.dominio.*;
import moduloCliente.dominio.cliente.Cliente;
import moduloCliente.dominio.cliente.ClienteComun;
import moduloCliente.dominio.cliente.ClienteProfesional;
import moduloCliente.dominio.repositorio.ClienteRepositorio;
import moduloCliente.infraestructura.persistencia.ClienteRepositorioImpl;
import moduloCliente.interfaz.evento.out.ClienteNuevoClienteComun;
import moduloCliente.interfaz.evento.out.PublicadorEventoCliente;
import org.jboss.weld.junit.MockBean;
import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.jboss.weld.junit5.auto.AddPackages;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;

@EnableWeld
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
    // para estos test no uso libreria mockito, sino que uso weld-junit + MockBean
    // yo creo implementaciones falsas manuales y las injecto al servicio
    @Inject
    private ServicioCliente servicioCliente;

    private ClienteRepositorio fakeRepo;

    // aca estoy configuracdo weld para que al iniciar me injeccto las
    // implementaciones falsas en mi servicio
    @WeldSetup
    public WeldInitiator weld = WeldInitiator.from(ServicioClienteImpl.class)
            .addBeans(crearMockRepositorioImpl())
            .addBeans(crearMockPublicadorEventoFake())
            .build();

    private Bean<?> crearMockRepositorioImpl() {
        return MockBean.builder()
                .types(ClienteRepositorio.class) // esto lo saco del @inject de ServicioPeajeImpl
                .scope(ApplicationScoped.class)
                .creating(crearRepoModuloClienteImpl()) // aca construyo la implementación que será usasa en este test
                .build();
    }

    private Bean<?> crearMockPublicadorEventoFake() {
        return MockBean.builder()
                .types(PublicadorEventoCliente.class)
                .scope(ApplicationScoped.class)
                .creating(crearPublicadorEvento())
                .build();
    }

    // no quiero probar el evento en test unitarios luego tendra su popio test
    private PublicadorEventoCliente crearPublicadorEvento() {
        return new PublicadorEventoCliente() {

            @Override
            public void publicarEventoClienteComun(Cliente cliente) {
                // System.out.println("Se ejecutó lanzador Eventos Fake");
            }

            @Override
            public void publicarEventoClienteProfesional(Cliente cliente) {
                // System.out.println("Se ejecutó lanzador Eventos Fake");
            }

            @Override
            public void publicarEventoClienteMetodoPago(MedioPago medioPago) {
                // System.out.println("Se ejecutó lanzador Eventos Fake");
            }

        };
    }

    // creo una implementacion fake del repo o sea uso memoria
    private ClienteRepositorio crearRepoModuloClienteImpl() {
        fakeRepo = new ClienteRepositorioImpl() {
            private final List<Cliente> clientes = new ArrayList<>();

            private final List<Reclamo> reclamos = new ArrayList<>();

            @Override
            public void saveCliente(Cliente cliente) {
                // System.out.println("Estoy usando repo fake");
                if (cliente != null) {
                    clientes.add(cliente);
                }
            }

            @Override
            public Cliente buscarCliente(String cedula) {
                return clientes.stream()
                        .filter(c -> Objects.equals(c.getCedula(), cedula))
                        .findFirst()
                        .orElse(null);
            }

            @Override
            public Grupo findGroup(String grupo) {

                // Fake simple
                if (grupo == null) {
                    return null;
                }

                Grupo g = new Grupo();
                g.setNombre(grupo);

                return g;
            }

            @Override
            public boolean actualizar(Cliente cliente) {
                if (cliente == null || cliente.getCedula() == null) {
                    return false;
                }
                for (int i = 0; i < clientes.size(); i++) {
                    if (Objects.equals(clientes.get(i).getCedula(), cliente.getCedula())) {
                        clientes.set(i, cliente);
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean saveReclamo(Reclamo reclamo) {

                if (reclamo == null) {
                    return false;
                }

                // Guarda en memoria
                reclamos.add(reclamo);

                return true;
            }

            @Override
            public void saveMedioPago(MedioPago medioPago) {
                // Fake para tests: no usa EntityManager
            }

        };
        return fakeRepo;
    }

    @Test
    @DisplayName("Registro ClienteComun")
    void registroClienteComun() {
        ClienteComun cl = new ClienteComun("12345678", "Franco", "Echaide", "099123456", "1234");
        servicioCliente.registrarCliente(cl);
        Cliente cliente = fakeRepo.buscarCliente("12345678");
        assertNotNull(cliente);
        assertEquals("12345678", cliente.getCedula());
        // verifico que se le agrege el grupo appMovil
        assertTrue(cliente.getGrupos().stream().anyMatch(g -> g.getNombre().equals("appMovil")));
    }

    @Test
    @DisplayName("Registro ClienteProfesional")
    void registroClienteProfecional() {
        ClienteProfesional clienteP = new ClienteProfesional(
                "12345679",
                "Franco",
                "Echaide",
                "099123456",
                "1234",
                TipoProfesional.PREMIUM,
                15.0f);
        servicioCliente.registrarCliente(clienteP);

        Cliente cliente = fakeRepo.buscarCliente("12345679");

        assertNotNull(cliente);

        assertEquals("12345679", cliente.getCedula());

        assertTrue(cliente.getGrupos().stream().anyMatch(g -> g.getNombre().equals("appMovil")));

    }

    @Test
    @DisplayName("Registro MedioPagoCuentaUte")
    void altaMedioPagoCuentaUte() {
        // Creo un cliente comun
        ClienteComun cl = new ClienteComun(
                "12345678",
                "Franco",
                "Echaide",
                "099123456",
                "1234");
        // Creo un Medio de pago CuentaUTE
        CuentaUTE cuenta = new CuentaUTE();
        cuenta.setId(1);
        cuenta.setFechaCreacion(LocalDate.now());
        cuenta.setNumeroCuenta("11111111");

        // Llamo a servicio
        servicioCliente.registrarCliente(cl);
        boolean resu = servicioCliente.altaMedioPago("12345678", cuenta);

        // verificar
        Cliente cliente = fakeRepo.buscarCliente("12345678");
        ClienteComun cliC = (ClienteComun) cliente;
        assertTrue(resu);
        // compruebo que tiene el método de pago que le agrege
        assertEquals("11111111", cliC.getFormaPago().getNumeroCuenta());

    }

    @Test
    @DisplayName("Registro MedioPagoTarjeta")
    void altaMedioPagoTarjeta() {
        // Creo un cliente Professional
        ClienteProfesional clienteP = new ClienteProfesional(
                "12345679",
                "Franco",
                "Echaide",
                "099123456",
                "1234",
                TipoProfesional.PREMIUM,
                15.0f);
        // Creo un Medio de pago TARJETA
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setId(1);
        tarjeta.setFechaCreacion(LocalDate.now());
        tarjeta.setNumero("2222222");
        tarjeta.setFechaVencimiento(LocalDate.now());
        tarjeta.setDigitoVerificacion("327");
        // tarjeta.setTipo(TipoTarjeta);

        // Llamo a servicio
        servicioCliente.registrarCliente(clienteP);
        boolean resu = servicioCliente.altaMedioPago("12345679", tarjeta);

        // verificar
        Cliente cliente = fakeRepo.buscarCliente("12345679");
        assertTrue(resu);
        assertEquals(1, cliente.getTarjetas().size());
    }

    @Test
    @DisplayName("Registro listarClientes")
    void listarCliente() {
        // crear varios clientes
        // comprobar si los trae
    }

    @Test
    @DisplayName("Registro Reclamo Para ClienteComun")
    void hacerReclamoParaClienteComun() {
        // Creo un cliente comun
        ClienteComun cl = new ClienteComun(
                "12345678",
                "Franco",
                "Echaide",
                "099123456",
                "1234");
        // Llamo a servicio
        servicioCliente.registrarCliente(cl);
        Reclamo reclamo = servicioCliente.realizarReclamo("Probando", "Probando realizar reclamo en cliente Comun",
                "12345678");
        // verificar
        assertNotNull(reclamo);
        assertEquals("Probando realizar reclamo en cliente Comun", reclamo.getDescripcion());
        assertEquals("12345678", reclamo.getCliente().getCedula());
    }

    @Test
    @DisplayName("Registro Reclamo Para ClenteProfesional")
    void hacerReclamoParaClienteProfesional() {
        // Creo un cliente Professional
        ClienteProfesional clienteP = new ClienteProfesional(
                "12345679",
                "Franco",
                "Echaide",
                "099123456",
                "1234",
                TipoProfesional.PREMIUM,
                15.0f);
        // Llamo a servicio
        servicioCliente.registrarCliente(clienteP);
        Reclamo reclamo = servicioCliente.realizarReclamo("Probando",
                "Probando realizar reclamo en cliente Profesional", "12345679");
        // verificar
        assertNotNull(reclamo);
        assertEquals("Probando realizar reclamo en cliente Profesional", reclamo.getDescripcion());
        assertEquals("12345679", reclamo.getCliente().getCedula());
    }
}
