package moduloCarga.aplicacion;

import moduloCarga.dominio.Carga;
import moduloCarga.dominio.Cargador;
import moduloCarga.dominio.EstacionCarga;
import moduloCarga.dominio.cliente.Cliente;
import moduloCarga.dominio.medioPago.MedioPago;

public interface ServicioCarga {
    boolean altaCliente(Cliente cli);

    //para probar nomas
    void obtenerClientes();

    Carga iniciarCarga(Cliente cli, MedioPago formaPago);


    Carga verCargaActual(Cliente cli);      //no estoy seguro si mosrar por consola directo con un void o traer la carga y mostrarla en el test, la letra no especifica

    void verHistorico(Cliente cli,String fechaIni,String fechaFin);

    void finalizarCarga(Cargador cargador, Carga carga,int recargo);

    void altaEstacion(EstacionCarga datos);

    void altaCargador(Cargador datos);

    void obtenerEstaciones();

}
