package moduloCarga.aplicacion;

import moduloCarga.dominio.Carga;
import moduloCarga.dominio.Cargador;
import moduloCarga.dominio.EstacionCarga;
import moduloCarga.dominio.cliente.Cliente;
import moduloCarga.dominio.medioPago.MedioPago;

public interface ServicioCarga {

    void iniciarCarga(Cliente cli, MedioPago formaPago);

    void verCargaActual(Cliente cli);

    void verHistorico(Cliente cli,String fechaIni,String fechaFin);

    void finalizarCarga(Cargador cargador, Carga carga,int recargo);

    void altaEstacion(EstacionCarga datos);

    void altaCargador(Cargador datos);

    void obtenerEstaciones();

}
