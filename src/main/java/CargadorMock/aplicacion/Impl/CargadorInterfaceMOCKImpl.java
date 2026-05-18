package CargadorMock.aplicacion.Impl;

import CargadorMock.aplicacion.CargadorInterfaceMOCK;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CargadorInterfaceMOCKImpl implements CargadorInterfaceMOCK{
    
    //La funcion simplemente retorna ok para simular la interacción con el cargador
    @Override
    public boolean iniciarCarga(){
        return true;
    }
}
