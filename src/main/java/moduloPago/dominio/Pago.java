package moduloPago.dominio;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "MPago_Pago")
@Table(name = "MPago_Pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPago;

    private String cedulaCliente;

    private int idCarga;

    private float monto;

    private String medioPago;

    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    private Estado estado;
}