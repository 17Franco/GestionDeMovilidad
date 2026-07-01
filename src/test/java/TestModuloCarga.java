
import FuncionalidadCargadorMOCK.aplicacion.FuncionalidadCargadorInterfaceMOCK;
import FuncionalidadCargadorMOCK.aplicacion.Impl.FuncionalidadCargadorInterfaceMOCKImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import moduloCarga.aplicacion.ServicioCarga;
import moduloCarga.aplicacion.impl.ServicioCargaImpl;

import moduloCarga.dominio.*;
import moduloCarga.dominio.cliente.Cliente;
import moduloCarga.dominio.medioPago.CuentaUTE;
import moduloCarga.dominio.medioPago.MedioPago;
import moduloCarga.dominio.medioPago.Tarjeta;
import moduloCarga.dominio.repositorio.RepoCarga;
import moduloCarga.infraestructura.persistencia.CargaRepoImpl;

import moduloCarga.interfaz.evento.in.ObserverModuloCarga;
import moduloCarga.interfaz.evento.out.PublicadorEvento;
import moduloCliente.dominio.TipoProfesional;
import moduloCliente.dominio.cliente.ClienteComun;
import moduloCliente.dominio.cliente.ClienteProfesional;
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
        ServicioCargaImpl.class,
        FuncionalidadCargadorInterfaceMOCKImpl.class,
        CargaRepoImpl.class,
        ObserverModuloCarga.class
})
public class TestModuloCarga {

    @Inject
    private PublicadorEventoCliente evento; //el real

    @Inject
    private ServicioCarga servicioCarga; //el real pero con repo fake

    private RepoCarga fakeRepo; //el repo fake

    //configuro weld
    //le digo que servicio quiero usar en el test
    //y registra el bean fake
    @WeldSetup
    public WeldInitiator weld =
            WeldInitiator.from(ServicioCargaImpl.class, ObserverModuloCarga.class,
                    PublicadorEventoCliente.class, PublicadorEvento.class)//debo agregar las class que son manejadas por wel
                    .addBeans(crearMockRepositorioImpl())
                    .addBeans(crearMockCargadorImpl())
                    .build();


    // crea un bean CDI manualmente o sea creo una implementacion fake manual
    private Bean<?> crearMockRepositorioImpl() {
        return MockBean.builder()
                .types(RepoCarga.class) //esto lo saco del @inject de ServicioPeajeImpl
                .scope(ApplicationScoped.class)
                .creating(crearRepoModuloCargaImpl())  //aca construyo la implementación que será usasa en este test
                .build();
    }

    private Bean<?> crearMockCargadorImpl() {
        return MockBean.builder()
                .types(FuncionalidadCargadorInterfaceMOCK.class) //esto lo saco del @inject de ServicioPeajeImpl
                .scope(ApplicationScoped.class)
                .creating(new FuncionalidadCargadorInterfaceMOCKImpl())  //aca construyo la implementación que será usasa en este test
                .build();
    }

    //creo una implementacion fake del repo o sea uso memoria
    private RepoCarga crearRepoModuloCargaImpl() {
        fakeRepo = new CargaRepoImpl() {

            private final List<Cliente> clientes = new ArrayList<>();
            private final List<EstacionCarga> estaciones = new ArrayList<>();
            private final List<Cargador> cargadores = new ArrayList<>();
            private final List<Carga> carga = new ArrayList<>();
            private final List<HistorialDeCargas> historialCarga = new ArrayList<>();


            @Override
            public EstacionCarga buscarEstacionPorId(int estacionId){
                return estaciones.stream()
                        .filter(c -> Objects.equals(c.getId(), estacionId))
                        .findFirst()
                        .orElse(null);
            }

            @Override
            public void registrarEstacion(EstacionCarga estacion) {
                if (estacion != null) {
                    estaciones.add(estacion);
                }
            }

            @Override
            public void registrarCargador(Cargador cargador) {
                if (cargador != null) {
                    cargadores.add(cargador);
                }
            }

            @Override
            public List<EstacionCarga> obtenerEstaciones() {

                return new ArrayList<>(estaciones);
            }

            @Override
            public Cargador getCargador(Integer idCargador) {
                return cargadores.stream()
                        .filter(c -> Objects.equals(c.getId(), idCargador))
                        .findFirst()
                        .orElse(null);
            }

            @Override
            public Cliente buscarPorCedula(String cedula) {
                return clientes.stream()
                        .filter(c -> Objects.equals(c.getCedula(), cedula))
                        .findFirst()
                        .orElse(null);
            }

            @Override
            public void registrarCliente(Cliente cliente){
                //System.out.println("Uso el repo Fake");
                if (cliente != null) {
                    clientes.add(cliente);
                }
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
            public void persistirCarga(Carga cargaNueva) {
                if (cargaNueva != null){
                    carga.add(cargaNueva);
                }
            }
            @Override
            public void actualizarCarga(Carga cargaActualizada) {
                // Las cargas del repositorio fake se conservan por referencia.
            }
            @Override
            public void saveMedioPago(MedioPago medioPago) {
                // En memoria, el servicio asocia el medio de pago al cliente.
            }
            @Override
            public void persistirOActualizarHistorial(HistorialDeCargas historial) {
                if (historial != null){
                    historialCarga.add(historial);
                }
            }
            @Override
            public void persistirElementoHistorial(ElementoHistorial elemento) {

            }

            @Override
            public void ActualizarCliente(Cliente cli) {

            }

        };
        return  fakeRepo;
    }


    @Test
    @DisplayName("Registro Cliente Comun ModuloCarga con evento")
    void registroClienteComunConEvento(){
        //Creo Cliente
        ClienteComun cl = new ClienteComun(
                "12345678",
                "Franco",
                "Echaide",
                "099123456",
                "1234"
        );
        //Lanzo evento
        evento.publicarEventoClienteComun(cl);

        //verifico
        Cliente cliente = fakeRepo.buscarPorCedula("12345678");
        assertNotNull(cliente);
        assertEquals("12345678", cliente.getCedula());
    }

    @Test
    @DisplayName("Registro Cliente Profesional ModuloCarga con evento")
    void registroClienteProfesionalConEvento(){
        //Creo Cliente
        ClienteProfesional clienteP = new ClienteProfesional(
                "12345679",
                "Franco",
                "Echaide",
                "099123456",
                "1234",
                TipoProfesional.PREMIUM,
                15.0f
        );
        //Lanzo evento
        evento.publicarEventoClienteProfesional(clienteP);

        //verifico
        Cliente cliente = fakeRepo.buscarPorCedula("12345679");
        assertNotNull(cliente);
        assertEquals("12345679", cliente.getCedula());
    }

    @Test
    @DisplayName("Regstro metodo Pago CuentaUte con evento")
    void registroMetodoPagoCuentUte(){
        //Creo Cliente
        ClienteComun cl = new ClienteComun(
                "12345678",
                "Franco",
                "Echaide",
                "099123456",
                "1234"
        );
        //Lanzo evento
        evento.publicarEventoClienteComun(cl);

        //Creo un Medio de pago CuentaUTE
        CuentaUTE cuenta = new CuentaUTE();
        cuenta.setId(1);
        cuenta.setFechaCreacion(LocalDate.now());
        cuenta.setNumeroCuenta("11111111");

        //Llamo a servicio
        boolean resu = servicioCarga.altaMedioPago("12345678", cuenta);

        //verificar
        Cliente cliente = fakeRepo.buscarPorCedula("12345678");
        moduloCarga.dominio.cliente.ClienteComun cliC = (moduloCarga.dominio.cliente.ClienteComun) cliente;
        assertTrue(resu);
        //compruebo que tiene el método de pago que le agrege
        assertEquals("11111111", cliC.getFormaPago().getNumeroCuenta());

    }

    @Test
    @DisplayName("Regstro metodo Pago Tarjeta con evento")
    void registroMetodoPagoTarjeta(){
        //Creo un cliente Professional
        ClienteProfesional clienteP = new ClienteProfesional(
                "12345679",
                "Franco",
                "Echaide",
                "099123456",
                "1234",
                TipoProfesional.PREMIUM,
                15.0f
        );
        //Lanzo evento
        evento.publicarEventoClienteProfesional(clienteP);

        //Creo un Medio de pago TARJETA
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setId(1);
        tarjeta.setFechaCreacion(LocalDate.now());
        tarjeta.setNumero("2222222");
        tarjeta.setFechaVencimiento(LocalDate.now());
        tarjeta.setDigitoVerificacion("327");
        //tarjeta.setTipo(TipoTarjeta);

        //Llamo a servicio
        boolean resu = servicioCarga.altaMedioPago("12345679", tarjeta);

        //verificar
        Cliente cliente = fakeRepo.buscarPorCedula("12345679");
        assertTrue(resu);
        assertEquals(1, cliente.getTarjetas().size());

    }

    @Test
    @DisplayName("Test Alta estacion")
    void altaEstacion(){
        //Creo objeto Estacion
        EstacionCarga estacion = new EstacionCarga();
        estacion.setId(1);
        estacion.setDescripcion("Estacion Test");
        estacion.setCalle("Probando Calle");
        estacion.setDepartamento("Probando Departamento");
        estacion.setLongitud(111111);
        estacion.setLatitud(222222);

        //Llamo a servicio
        servicioCarga.altaEstacion(estacion);

        //Verifio
        List<EstacionCarga> lista= fakeRepo.obtenerEstaciones();

        assertNotNull(lista);

        assertEquals(1,lista.size());


    }

    @Test
    @DisplayName("Test Alta cargador")
    void altaCargador(){
        //Creo objeto Estacion
        EstacionCarga estacion = new EstacionCarga();
        estacion.setId(1);
        estacion.setDescripcion("Estacion Test");
        estacion.setCalle("Probando Calle");
        estacion.setDepartamento("Probando Departamento");
        estacion.setLongitud(111111);
        estacion.setLatitud(222222);

        //Llamo a servicio
        servicioCarga.altaEstacion(estacion);

        //Creo Objeto Cargador
        Cargador cargador = new Cargador();
        cargador.setId(1);
        //cargador.setTipo();
        cargador.setTieneCable(true);
        //cargador.setTipoConector();
        //cargador.setEstado();
        cargador.setPotenciaMinima(50);

        servicioCarga.altaCargador(1,cargador);

        //Verifio
        EstacionCarga e =  fakeRepo.buscarEstacionPorId(1);

        assertNotNull(e);

        assertNotNull(e.getCargadores());

        assertEquals(1,e.getCargadores().size());


    }

    @Test
    @DisplayName("Iniciar Carga Cliente Comun")
    void iniciarCargaClienteComun(){
       //Creo Cliente
        ClienteComun cl = new ClienteComun(
                "12345678",
                "Franco",
                "Echaide",
                "099123456",
                "1234"
        );
        //Lanzo evento
        evento.publicarEventoClienteComun(cl);

        //Creo un Medio de pago CuentaUTE
        CuentaUTE cuenta = new CuentaUTE();
        cuenta.setId(1);
        cuenta.setFechaCreacion(LocalDate.now());
        cuenta.setNumeroCuenta("11111111");

        //Llamo a servicio
        boolean resu = servicioCarga.altaMedioPago("12345678", cuenta);

        //Creo objeto Estacion
        EstacionCarga estacion = new EstacionCarga();
        estacion.setId(1);
        estacion.setDescripcion("Estacion Test");
        estacion.setCalle("Probando Calle");
        estacion.setDepartamento("Probando Departamento");
        estacion.setLongitud(111111);
        estacion.setLatitud(222222);

        //Llamo a servicio
        servicioCarga.altaEstacion(estacion);

        //Creo Objeto Cargador
        Cargador cargador = new Cargador();
        cargador.setId(1);
        //cargador.setTipo();
        cargador.setTieneCable(true);
        //cargador.setTipoConector();
        //cargador.setEstado();
        cargador.setPotenciaMinima(50);

        servicioCarga.altaCargador(1,cargador);


        //Llamo a iniciarCarga
        //traigo al usuario
        Cliente cli =fakeRepo.buscarPorCedula("12345678");
        servicioCarga.iniciarCarga(cli, cuenta,1);
        System.out.print("El cliente " + cli.getNombre() + " " + cli.getApellido() +
                " inició una carga" + "\n" + "Realizó el pago con " + cuenta.getNumeroCuenta() + "\n");
        if (cli.getCargaActual().getEstado() == EstadoCarga.ENPROGRESO){
            System.out.print("La carga está en progreso");
        }
        else if(cli.getCargaActual().getEstado() == EstadoCarga.TERMINADO){
            System.out.print("La carga finalizó");
        }
        else{
            System.out.print("Error inesperado, llame a soporte");
        }


    }

    @Test
    @DisplayName("Test ver carga actual")
    void verCargaActual(){
        //Creo Cliente
        ClienteComun cl = new ClienteComun(
                "12345678",
                "Franco",
                "Echaide",
                "099123456",
                "1234"
        );
        //Lanzo evento
        evento.publicarEventoClienteComun(cl);

        //Creo un Medio de pago CuentaUTE
        CuentaUTE cuenta = new CuentaUTE();
        cuenta.setId(1);
        cuenta.setFechaCreacion(LocalDate.now());
        cuenta.setNumeroCuenta("11111111");

        //Llamo a servicio
        boolean resu = servicioCarga.altaMedioPago("12345678", cuenta);

        //Creo objeto Estacion
        EstacionCarga estacion = new EstacionCarga();
        estacion.setId(1);
        estacion.setDescripcion("Estacion Test");
        estacion.setCalle("Probando Calle");
        estacion.setDepartamento("Probando Departamento");
        estacion.setLongitud(111111);
        estacion.setLatitud(222222);

        //Llamo a servicio
        servicioCarga.altaEstacion(estacion);

        //Creo Objeto Cargador
        Cargador cargador = new Cargador();
        cargador.setId(1);
        //cargador.setTipo();
        cargador.setTieneCable(true);
        //cargador.setTipoConector();
        //cargador.setEstado();
        cargador.setPotenciaMinima(50);

        servicioCarga.altaCargador(1,cargador);

        //traigo al usuario
        Cliente cli =fakeRepo.buscarPorCedula("12345678");
        //Llamo a iniciarCarga
        servicioCarga.iniciarCarga(cli, cuenta,1);
        Carga carga = servicioCarga.verCargaActual(cli);
        assertNotNull(carga);
        assertEquals(1,carga.getCargador().getId());
        assertEquals("Probando Departamento",carga.getCargador().getEstacionCarga().getDepartamento());

    }

    @Test
    @DisplayName("Test ver historico cargas del cliente")
    void verHistoricoDeCargas(){
        //Creo Cliente
        ClienteComun cl = new ClienteComun(
                "12345678",
                "Franco",
                "Echaide",
                "099123456",
                "1234"
        );
        //Lanzo evento
        evento.publicarEventoClienteComun(cl);

        //Creo un Medio de pago CuentaUTE
        CuentaUTE cuenta = new CuentaUTE();
        cuenta.setId(1);
        cuenta.setFechaCreacion(LocalDate.now());
        cuenta.setNumeroCuenta("11111111");

        //Llamo a servicio
        boolean resu = servicioCarga.altaMedioPago("12345678", cuenta);

        //Creo objeto Estacion
        EstacionCarga estacion = new EstacionCarga();
        estacion.setId(1);
        estacion.setDescripcion("Estacion Test");
        estacion.setCalle("Probando Calle");
        estacion.setDepartamento("Probando Departamento");
        estacion.setLongitud(111111);
        estacion.setLatitud(222222);

        //Llamo a servicio
        servicioCarga.altaEstacion(estacion);

        //Creo Objeto Cargador
        Cargador cargador = new Cargador();
        cargador.setId(1);
        //cargador.setTipo();
        cargador.setTieneCable(true);
        //cargador.setTipoConector();
        //cargador.setEstado();
        cargador.setPotenciaMinima(50);

        servicioCarga.altaCargador(1,cargador);

        //Llamo a iniciarCarga
        //traigo al usuario
        Cliente cli =fakeRepo.buscarPorCedula("12345678");
        servicioCarga.iniciarCarga(cli, cuenta,1);
        servicioCarga.iniciarCarga(cli, cuenta,1);
        String fechaInicio = "2026-06-07";
        String fechaFin = "2026-06-10";
        servicioCarga.verHistorico(cli, fechaInicio, fechaFin);


    }

    @Test
    @DisplayName("Test finalizar carga")
    void finalizarCarga(){
        //Creo Cliente
        ClienteComun cl = new ClienteComun(
                "12345678",
                "Franco",
                "Echaide",
                "099123456",
                "1234"
        );
        //Lanzo evento
        evento.publicarEventoClienteComun(cl);

        //Creo un Medio de pago CuentaUTE
        CuentaUTE cuenta = new CuentaUTE();
        cuenta.setId(1);
        cuenta.setFechaCreacion(LocalDate.now());
        cuenta.setNumeroCuenta("11111111");

        //Llamo a servicio
        boolean resu = servicioCarga.altaMedioPago("12345678", cuenta);

        //Creo objeto Estacion
        EstacionCarga estacion = new EstacionCarga();
        estacion.setId(1);
        estacion.setDescripcion("Estacion Test");
        estacion.setCalle("Probando Calle");
        estacion.setDepartamento("Probando Departamento");
        estacion.setLongitud(111111);
        estacion.setLatitud(222222);

        //Llamo a servicio
        servicioCarga.altaEstacion(estacion);

        //Creo Objeto Cargador
        Cargador cargador = new Cargador();
        cargador.setId(1);
        //cargador.setTipo();
        cargador.setTieneCable(true);
        //cargador.setTipoConector();
        //cargador.setEstado();
        cargador.setPotenciaMinima(50);

        servicioCarga.altaCargador(1,cargador);

        //Llamo a iniciarCarga
        //traigo al usuario
        Cliente cli =fakeRepo.buscarPorCedula("12345678");
        servicioCarga.iniciarCarga(cli, cuenta,1);

        Carga carga = cli.getCargaActual();

        // finaliza la carga
        //servicioCarga.finalizarCarga(cargador, carga, 100);

        // muestra datos finales
        System.out.println(carga);
    }





}
