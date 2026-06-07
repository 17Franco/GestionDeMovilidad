package moduloCarga.dominio;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
@Entity
@Table(name="MCarga_HistorialesDeCargas")
public class HistorialDeCargas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
        
    @OneToOne
    @JoinColumn(name = "cliente_cedula", nullable = false, unique = true)
    private Cliente clienteAsociado;

    @OneToMany(
            mappedBy = "historialAsociado",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ElementoHistorial> historialCargas = new ArrayList<>();


    //uso un elemento_aux para poder guardar el medio de pago utilizado en la carga
    public void agregarCarga(Carga cargaNueva, MedioPago medioPago) {
        ElementoHistorial elementoAux = new ElementoHistorial();

        elementoAux.setCarga(cargaNueva);
        elementoAux.setMedioPago(medioPago);
        elementoAux.setHistorialAsociado(this);

        historialCargas.add(elementoAux);
    }
}
