package moduloCliente.dominio;

import java.time.LocalDate;

public abstract class MedioPago {
    private int id;
    private String ciCli; //relacionamos el medioPagoCon el cliente
    private LocalDate fechaCreacion;

}
