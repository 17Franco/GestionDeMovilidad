package CargadorMock.aplicacion;

import moduloCarga.dominio.Carga;

public interface CargadorInterfaceMOCK {
    
    //Esta funcion simplemente retorna true, es un mock simplemente, no le paso Cliente o Medio de pago ya que no me interesa
    public Carga iniciarCarga();     
}
