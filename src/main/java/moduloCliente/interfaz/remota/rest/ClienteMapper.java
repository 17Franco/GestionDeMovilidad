package moduloCliente.interfaz.remota.rest;

import moduloCliente.dominio.cliente.Cliente;
import moduloCliente.dominio.cliente.ClienteComun;
import moduloCliente.dominio.cliente.ClienteProfesional;
import moduloCliente.exepciones.TipoClienteInvalidoException;

//esta clase es para transformar el dto al objeto de dominio correcto
public class ClienteMapper {

    public static Cliente toDomain(ClienteDTO dto) {

        if ("COMUN".equals(dto.getTipoCliente())) {
            return new ClienteComun(dto.getCedula(), dto.getNombre(),dto.getApellido(),dto.getNumTel(),dto.getContra());
        }

        if ("PROFESIONAL".equals(dto.getTipoCliente())) {
            return new ClienteProfesional(
                    dto.getCedula(),
                    dto.getNombre(),
                    dto.getApellido(),
                    dto.getNumTel(),
                    dto.getContra(),
                    dto.getTipoProfesional(),
                    dto.getPorcentajeDescuento()
            );
        }
        //lanzo exepcion si viene un tipo de cliente incorrecto
        throw new TipoClienteInvalidoException("Tipo de cliente inválido: " + dto.getTipoCliente());
    }
}
