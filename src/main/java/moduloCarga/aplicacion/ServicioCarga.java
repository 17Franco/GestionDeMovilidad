package moduloCarga.aplicacion;

import FuncionalidadCargadorMOCK.aplicacion.DTOs.*;
import moduloCarga.dominio.Carga;
import moduloCarga.dominio.Cargador;
import moduloCarga.dominio.EstacionCarga;
import moduloCarga.dominio.cliente.Cliente;
import moduloCarga.dominio.medioPago.MedioPago;

public interface ServicioCarga {
    void altaCliente(Cliente cli);

    boolean altaMedioPago(String ci, MedioPago formaPago);

    void iniciarCarga(Cliente cli, MedioPago formaPago , Integer idCargador);
    boolean tieneDeuda(String idCLiente);
    Carga verCargaActual(Cliente cli);      //no estoy seguro si mosrar por consola directo con un void o traer la carga y mostrarla en el test, la letra no especifica

    void verHistorico(Cliente cli,String fechaIni,String fechaFin);

    boolean finalizarCarga(Cargador cargador, Carga carga,int recargo, MedioPago formaPago);

    void altaEstacion(EstacionCarga datos);

    void altaCargador(int estacionId,Cargador datos);

    void obtenerEstaciones();

}
