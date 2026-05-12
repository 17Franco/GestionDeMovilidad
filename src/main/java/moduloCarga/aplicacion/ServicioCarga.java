package moduloCarga.aplicacion;

import moduloCarga.dominio.Carga;
import moduloCarga.dominio.Cargador;
import moduloCarga.dominio.EstacionCarga;
import moduloCliente.dominio.MedioPago;
import moduloCliente.dominio.cliente.Cliente;

public interface ServicioCarga {

    void iniciarCarga(Cliente cli, MedioPago formaPago);

    void verCargaActual(Cliente cli);

    void verHistorico(Cliente cli,String fechaIni,String fechaFin);

    void finalizarCarga(Cargador cargador, Carga carga,int recargo);

    void altaEstacion(EstacionCarga datos);

    void altaCargador(Cargador datos);

    void obtenerEstaciones();
}
