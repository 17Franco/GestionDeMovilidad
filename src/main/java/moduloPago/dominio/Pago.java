package moduloPago.dominio;

//nesesito idcliente idcarga monto, medio de pago tambien fechaInicio cuadno se crea tedre fecha fin
//osea con un observer en modulo carga mandare un evento pagarCarga donde ese eventon
//tendra idcliente idcarga monto mediopagofecha inicio
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity (name = "MPago_Pago")
//ACA tenemos historial de pagos rechazados y aceptados y podemso saber que tipo se usa si tarjeta o cuenta ute
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
    private Estado  estado;
}