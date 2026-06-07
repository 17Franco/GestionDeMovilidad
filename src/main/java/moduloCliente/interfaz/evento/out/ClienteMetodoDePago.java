package moduloCliente.interfaz.evento.out;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moduloCliente.dominio.TipoTarjeta;
import moduloCliente.dominio.cliente.Cliente;


import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ClienteMetodoDePago {
    private int id;
    private LocalDate fechaCreacion;
    private String  tipoMedioPago;
    //cuentaUte
    private String numeroCuenta;
    private String clienteCUte;

    //Tarjeta
    private String numero;
    private LocalDate fechaVencimiento;
    private String digitoVerificacion;
    private String tipo;
    private String clienteTarjeta;
}
