package moduloCarga.dominio.cliente;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moduloCarga.dominio.medioPago.MedioPago;


import java.util.ArrayList;
import java.util.List;

@Entity(name = "ClienteProfesional_Carga")
@Getter
@Setter
@NoArgsConstructor
@Table(name = "MCarga_ClienteProfesional")
public class ClienteProfesional extends Cliente{

    @Enumerated(EnumType.STRING)
    private TipoProfesional tipo;

    private float porcentajeDescuento;

    //esto es porque cada medio de pago ya tiene relacion con cliente
    @Transient
    private List<MedioPago> metodosPago = new ArrayList<>();

    public ClienteProfesional(String cedula, String nombre, String apellido, String numTel, String contra, TipoProfesional tipo, float porcentajeDescuento) {
        super(cedula, nombre, apellido, numTel, contra);
        this.tipo = tipo;
        this.porcentajeDescuento = porcentajeDescuento;
    }
}
