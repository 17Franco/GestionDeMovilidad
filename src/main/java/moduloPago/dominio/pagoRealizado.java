package moduloPago.dominio;

//nesesito idcliente idcarga monto, medio de pago tambien fechaInicio cuadno se crea tedre fecha fin
//osea con un observer en modulo carga mandare un evento pagarCarga donde ese eventon
//tendra idcliente idcarga monto mediopagofecha inicio
import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class pagoRealizado {

    private String cedulaCliente;

    private int idCarga;

    private float monto;

    private String medioPago;

    private LocalDate fecha;
}