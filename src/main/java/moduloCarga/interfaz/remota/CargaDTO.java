package moduloCarga.interfaz.remota;

import java.time.LocalDate;
import java.time.LocalDateTime;

import moduloCarga.dominio.Carga;

public class CargaDTO {

    private int id;
    private LocalDate fecha;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;
    private float importeTotal;
    private float recargoPorDemora;
    private float porcentajeAvance;
    private LocalDateTime horaEstimadaFin;
    private String estado;

    private String cedulaCliente;
    private Integer idCargador;

    public CargaDTO() {
    }

    //Contructor, a partir de un carga construyo una CargaDTO
    public CargaDTO(Carga carga) {
        this.id = carga.getId();
        this.fecha = carga.getFecha();
        this.horaInicio = carga.getHoraInicio();
        this.horaFin = carga.getHoraFin();
        this.importeTotal = carga.getImporteTotal();
        this.recargoPorDemora = carga.getRecargoPorDemora();
        this.porcentajeAvance = carga.getPorcentajeAvance();
        this.horaEstimadaFin = carga.getHoraEstimadaFin();

        if (carga.getEstado() != null) {
            this.estado = carga.getEstado().toString();
        }

        if (carga.getClienteAsociado() != null) {
            this.cedulaCliente = carga.getClienteAsociado().getCedula();
        }

        if (carga.getCargador() != null) {
            this.idCargador = carga.getCargador().getId();
        }
    }

    public int getId() {
        return id;
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

    public float getImporteTotal() {
        return importeTotal;
    }

    public float getRecargoPorDemora() {
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

    public String getCedulaCliente() {
        return cedulaCliente;
    }

    public Integer getIdCargador() {
        return idCargador;
    }
}