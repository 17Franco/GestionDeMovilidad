package moduloCarga.dominio;

import java.util.List;

import moduloCarga.dominio.cliente.Cliente;

public class HistorialDeCargas {
    
//@ManyToOne
private Cliente clienteAsociado;    //relación con el cliente
private List<Carga> cargasCliente;  //lista donde las guarda (luego va a ser en la bd)


}
