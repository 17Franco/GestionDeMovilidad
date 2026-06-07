package moduloCarga.interfaz.remota;

public class iniciarCargaDatos {
    private String cedulaCliente;
    private String metodoPago;
    private String numeroTarjerta;
    private Integer cargadorID;

    public String getCedulaCliente(){
        return cedulaCliente;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public String getNumeroTarjeta() {
        return numeroTarjerta;
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

    public void setNumerTarjerta(String numeroTarjerta) {
        this.numeroTarjerta = numeroTarjerta;
    }

    public void setCargadorID(Integer cargadorID) {
        this.cargadorID = cargadorID;
    }
}
