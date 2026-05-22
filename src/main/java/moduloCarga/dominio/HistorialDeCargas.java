package moduloCarga.dominio;

import java.util.ArrayList;
import java.util.List;

import moduloCarga.dominio.cliente.Cliente;
import moduloCarga.dominio.medioPago.MedioPago;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistorialDeCargas {
    
//@ManyToOne
private Cliente clienteAsociado;    //relación con el cliente
private List<ElementoHistorial> hisorialCargas = new ArrayList<>();  //lista donde las guarda (luego va a ser en la bd), la inicializo porque sino no puedo hacer add

//uso un elemento_aux para poder guardar el medio de pago utilizado en la carga
public void agregarCarga(Carga cargaNueva, MedioPago medioPago) {
    ElementoHistorial elemento_aux = new ElementoHistorial();
    elemento_aux.setCarga(cargaNueva);
    elemento_aux.setMedioPago(medioPago);

    hisorialCargas.add(elemento_aux);
}
}
