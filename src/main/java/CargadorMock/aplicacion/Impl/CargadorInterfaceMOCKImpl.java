package CargadorMock.aplicacion.Impl;

import java.time.LocalDate;
import java.time.LocalDateTime;

import CargadorMock.aplicacion.CargadorInterfaceMOCK;
import CargadorMock.aplicacion.DTOs.DTOCarga;
import CargadorMock.aplicacion.DTOs.DTOEstadoCarga;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class CargadorInterfaceMOCKImpl implements CargadorInterfaceMOCK{
    private void setContenidoCargaMock(DTOCarga cargaNueva){
        cargaNueva.setFecha(LocalDate.of(2026, 5, 20));
        cargaNueva.setHoraInicio(LocalDateTime.of(2026, 5, 20, 18, 30));
        //hora fin sin setear
        cargaNueva.setImporteTotal(500f);
        cargaNueva.setRecargoPorDemora(0f);
        cargaNueva.setPorcentajeAvance(0f);
        cargaNueva.setHoraEstimadaFin(cargaNueva.getHoraInicio().plusHours(2)); //le sumo 2 horas a la hora de inicio
        cargaNueva.setEstado(DTOEstadoCarga.ENPROGRESO);
    }
    @Override
    public DTOCarga iniciarCarga(){
        //creo una carga nueva vacía
        DTOCarga cargaNueva = new DTOCarga();
        //seteo los datos de la carga hardcordeados arriba
        setContenidoCargaMock(cargaNueva);
        return cargaNueva;
    }
  
}
