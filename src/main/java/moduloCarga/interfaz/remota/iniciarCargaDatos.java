package moduloCarga.interfaz.remota;

public class iniciarCargaDatos {
    private String cedulaCliente;
    private String metodoPago;
    private String numeroTarjeta;
    private Integer cargadorID;

    public String getCedulaCliente(){
        return cedulaCliente;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public Integer getCargadorID(){
        return cargadorID;
    }

    public void setCedulaCliente(String cedulaCliente) {
        this.cedulaCliente = cedulaCliente;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public void setNumeroTarjerta(String numeroTarjerta) {
        this.numeroTarjeta = numeroTarjerta;
    }

    public void setNumerTarjerta(String numeroTarjerta) {
        this.numeroTarjeta = numeroTarjerta;
    }

    public void setCargadorID(Integer cargadorID) {
        this.cargadorID = cargadorID;
    }
}
