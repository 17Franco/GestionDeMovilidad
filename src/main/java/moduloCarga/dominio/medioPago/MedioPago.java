package moduloCarga.dominio.medioPago;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity (name = "MedioPago_Carga")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "MCarga_MedioPago")
public abstract class MedioPago {
    @Id
    private int id;
    private LocalDate fechaCreacion;
    
    public abstract String getTipoMedioPago(); //creo esta funcion abstracta para retornar el tipo me medioPago

}
