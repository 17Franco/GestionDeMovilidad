package moduloCliente.interfaz.remota.rest;

import moduloCliente.dominio.Reclamo;
import moduloCliente.dominio.TipoReclamo;

public class ReclamoConClienteDTO {
    public Long idReclamo;
    public String asunto;
    public String descripcion;
    public TipoReclamo tipoReclamo;

    public String ciCliente;
    public String nombreCliente;
    public String apellidoCliente;

    public ReclamoConClienteDTO(Reclamo r) {
        this.idReclamo = r.getId();
        this.asunto = r.getAsunto();
        this.descripcion = r.getDescripcion();
        this.tipoReclamo = r.getTipoReclamo();

        this.ciCliente = r.getCliente().getCedula();
        this.nombreCliente = r.getCliente().getNombre();
        this.apellidoCliente = r.getCliente().getApellido();
    }
}