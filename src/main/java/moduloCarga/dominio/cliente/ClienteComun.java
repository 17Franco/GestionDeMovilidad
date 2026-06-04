package moduloCarga.dominio.cliente;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moduloCarga.dominio.medioPago.CuentaUTE;

import java.util.ArrayList;
import java.util.List;

@Entity (name = "ClienteComun_Carga")
@Getter
@Setter
@NoArgsConstructor
@Table(name = "MCarga_ClienteComun")
public class ClienteComun extends Cliente{
    @OneToOne
    @JoinColumn(name = "forma_pago_id")
    private CuentaUTE formaPago;


    public ClienteComun(String cedula, String nombre, String apellido, String numTel, String contra) {
        super(cedula, nombre, apellido, numTel, contra);
    }
    
    
}
