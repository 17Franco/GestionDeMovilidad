package moduloCarga.dominio.medioPago;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity (name = "Tarjeta_Carga")
@Getter
@Setter
@NoArgsConstructor
@Table(name = "MCarga_Tarjeta")
public class Tarjeta extends MedioPago {
    private String numero;
    private LocalDate fechaVencimiento;
    private String digitoVerificacion;

    @Enumerated(EnumType.STRING)
    private TipoTarjeta tipo;

    @Override
    public String getTipoMedioPago(){
        return "Tarjeta";
    }
}
