package FuncionalidadCargadorMOCK.aplicacion;

import FuncionalidadCargadorMOCK.aplicacion.DTOs.DTOCarga;

public interface FuncionalidadCargadorInterfaceMOCK {
    //la funcionlidad de esto es retonarme una carga, no le interesa saber la id de donde esta ni nada, funciona dentro de cualquier cargador, es la funcionalidad indiferencte de cualquier estacion, es el moduloCarga quien le asigna la ID    
    public DTOCarga iniciarCarga();     
}
