package CargadorMock.aplicacion.Impl;

import CargadorMock.aplicacion.CargadorInterfaceMOCK;
import jakarta.enterprise.context.ApplicationScoped;
import moduloCarga.dominio.Carga;

@ApplicationScoped
public class CargadorInterfaceMOCKImpl implements CargadorInterfaceMOCK{
    
    //La funcion simplemente retorna ok para simular la interacción con el cargador
    @Override
    public Carga iniciarCarga(){
        Carga cargaNueva = new Carga();
        //tengo que meterle datos a la carga
        return cargaNueva;
    }

    //genero una new Carga() y la retorno
}
