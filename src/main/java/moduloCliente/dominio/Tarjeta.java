package moduloCliente.dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moduloCliente.dominio.cliente.Cliente;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "MCliente_Tarjeta")
public class Tarjeta extends MedioPago {
    private String numero;
    private LocalDate fechaVencimiento;
    private String digitoVerificacion;
    @Enumerated(EnumType.STRING)
    private TipoTarjeta tipo;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
