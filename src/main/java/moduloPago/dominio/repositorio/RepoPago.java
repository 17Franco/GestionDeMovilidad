package moduloPago.dominio.repositorio;

import moduloPago.dominio.Pago;

import java.time.LocalDate;
import java.util.List;

public interface RepoPago {
    void save(Pago pago);

    List<Pago> getPagosPorFecha(String ci, LocalDate fechaIni, LocalDate fechaFin);

    //obtengo si hay deuda del cliente
    boolean deuda(String idCliente);

    Pago obtenerDeuda(String idCliente, int idCarga);

}
