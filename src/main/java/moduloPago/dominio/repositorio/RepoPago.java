package moduloPago.dominio.repositorio;

import moduloPago.dominio.pagoRealizado;

import java.time.LocalDate;
import java.util.List;

public interface RepoPago {
    void save(pagoRealizado pago);
    List<pagoRealizado> getPagosPorFecha(String ci, LocalDate fechaIni, LocalDate fechaFin);
}
