package moduloCliente.dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moduloCliente.dominio.cliente.Cliente;

@Getter
@Setter

@NoArgsConstructor
@Table(name = "MCliente_Reclamos")
@Entity
public class Reclamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String asunto;
    @Lob
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    public Reclamo(String asunto, String descripcion, Cliente cliente) {
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.cliente = cliente;
    }
}
