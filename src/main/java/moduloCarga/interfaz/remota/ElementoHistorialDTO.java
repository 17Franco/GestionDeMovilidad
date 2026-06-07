package moduloCarga.interfaz.remota;

import java.time.LocalDate;
import java.time.LocalDateTime;

import moduloCarga.dominio.ElementoHistorial;

public class ElementoHistorialDTO {

    private LocalDate fecha;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;
    private double importeTotal;
    private double recargoPorDemora;
    private float porcentajeAvance;
    private LocalDateTime horaEstimadaFin;
    private String estado;
    private String medioPago;

    public ElementoHistorialDTO(ElementoHistorial elemento) {
        this.fecha = elemento.getCarga().getFecha();
        this.horaInicio = elemento.getCarga().getHoraInicio();
        this.horaFin = elemento.getCarga().getHoraFin();
        this.importeTotal = elemento.getCarga().getImporteTotal();
        this.recargoPorDemora = elemento.getCarga().getRecargoPorDemora();
        this.porcentajeAvance = elemento.getCarga().getPorcentajeAvance();
        this.horaEstimadaFin = elemento.getCarga().getHoraEstimadaFin();
        this.estado = elemento.getCarga().getEstado().toString();
        this.medioPago = elemento.getMedioPago().getClass().getSimpleName();
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalDateTime getHoraInicio() {
        return horaInicio;
    }

    public LocalDateTime getHoraFin() {
        return horaFin;
    }

    public double getImporteTotal() {
        return importeTotal;
    }

    public double getRecargoPorDemora() {
        return recargoPorDemora;
    }

    public float getPorcentajeAvance() {
        return porcentajeAvance;
    }

    public LocalDateTime getHoraEstimadaFin() {
        return horaEstimadaFin;
    }

    public String getEstado() {
        return estado;
    }

    public String getMedioPago() {
        return medioPago;
    }
}