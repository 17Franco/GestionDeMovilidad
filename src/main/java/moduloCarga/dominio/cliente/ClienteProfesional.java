package moduloCarga.dominio.cliente;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moduloCliente.dominio.MedioPago;
import moduloCliente.dominio.TipoProfesional;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "MCliente_ClienteProfesional")
public class ClienteProfesional extends Cliente{

    @Enumerated(EnumType.STRING)
    private TipoProfesional tipo;

    private float porcentajeDescuento;

    //esto es porque cada medio de pago ya tiene relacion con cliente
    @Transient
    private List<MedioPago> metodosPago = new ArrayList<>();
}
