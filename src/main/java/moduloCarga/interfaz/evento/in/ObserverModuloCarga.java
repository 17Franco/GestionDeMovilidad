package moduloCarga.interfaz.evento.in;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import moduloCarga.aplicacion.ServicioCarga;

import moduloCarga.dominio.cliente.ClienteComun;
import moduloCarga.dominio.cliente.ClienteProfesional;
import moduloCarga.dominio.cliente.TipoProfesional;
import moduloCarga.dominio.medioPago.CuentaUTE;
import moduloCarga.dominio.medioPago.Tarjeta;

import moduloCarga.dominio.medioPago.TipoTarjeta;
import moduloCliente.interfaz.evento.out.ClienteMetodoDePago;
import moduloCliente.interfaz.evento.out.ClienteNuevoClienteComun;
import moduloCliente.interfaz.evento.out.ClienteNuevoClienteProfesional;

@ApplicationScoped
public class ObserverModuloCarga {
    @Inject
    private ServicioCarga servicioCarga;

    public void accept(@Observes ClienteNuevoClienteComun event) {
        //log.infof("Evento procesado: GestionNuevoVehiculo: %s", event.toString());
        ClienteComun cli = new ClienteComun(event.getCedula(), event.getNombre(), event.getApellido(), event.getNumTel(), event.getContra());
        servicioCarga.altaCliente(cli);
    }

    public void accept(@Observes ClienteNuevoClienteProfesional event) {
        //log.infof("Evento procesado: GestionNuevoVehiculo: %s", event.toString());
        TipoProfesional tipo = TipoProfesional.valueOf(event.getTipo());
        ClienteProfesional cli = new ClienteProfesional(
                event.getCedula(),
                event.getNombre(),
                event.getApellido(),
                event.getNumTel(),
                event.getContra(),
                tipo,
                event.getPorcentajeDescuento()
        );
        servicioCarga.altaCliente(cli);
    }

public void accept(@Observes ClienteMetodoDePago event) {

    System.out.println("========= OBSERVER CARGA RECIBIO MEDIO DE PAGO =========");
    System.out.println("Tipo medio pago = " + event.getTipoMedioPago());
    System.out.println("Id = " + event.getId());
    System.out.println("Cliente UTE = " + event.getClienteCUte());
    System.out.println("Cliente Tarjeta = " + event.getClienteTarjeta());
    System.out.println("Numero cuenta = " + event.getNumeroCuenta());
    System.out.println("Numero tarjeta = " + event.getNumero());
    System.out.println("Tipo tarjeta = " + event.getTipo());

    if ("CUENTA_UTE".equals(event.getTipoMedioPago())) {

        CuentaUTE cuenta = new CuentaUTE();
        cuenta.setId(event.getId());
        cuenta.setFechaCreacion(event.getFechaCreacion());
        cuenta.setNumeroCuenta(event.getNumeroCuenta());

        boolean resu = servicioCarga.altaMedioPago(event.getClienteCUte(), cuenta);
        System.out.println("Resultado alta medio pago carga UTE = " + resu);

    } else if ("TARJETA".equals(event.getTipoMedioPago())) {

        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setId(event.getId());
        tarjeta.setFechaCreacion(event.getFechaCreacion());
        tarjeta.setNumero(event.getNumero());
        tarjeta.setFechaVencimiento(event.getFechaVencimiento());
        tarjeta.setDigitoVerificacion(event.getDigitoVerificacion());

        TipoTarjeta tipo = TipoTarjeta.valueOf(event.getTipo());
        tarjeta.setTipo(tipo);

        boolean resu = servicioCarga.altaMedioPago(event.getClienteTarjeta(), tarjeta);
        System.out.println("Resultado alta medio pago carga tarjeta = " + resu);
    }
}

}
