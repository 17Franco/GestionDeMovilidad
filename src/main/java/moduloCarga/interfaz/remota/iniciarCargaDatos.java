package moduloCarga.interfaz.remota;

public class iniciarCargaDatos {
    private String cedulaCliente;
    private String metodoPago;

    public String getCedulaCliente(){
        return cedulaCliente;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setCedulaCliente(String cedulaCliente) {
        this.cedulaCliente = cedulaCliente;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
}
