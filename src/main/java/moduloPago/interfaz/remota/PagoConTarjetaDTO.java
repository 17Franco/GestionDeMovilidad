package moduloPago.interfaz.remota;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PagoConTarjetaDTO {
    private String numeroTarjeta;
    private float monto;
}


