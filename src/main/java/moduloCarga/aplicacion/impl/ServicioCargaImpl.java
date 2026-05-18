package moduloCarga.aplicacion.impl;

import CargadorMock.aplicacion.CargadorInterfaceMOCK;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import moduloCarga.aplicacion.ServicioCarga;
import moduloCarga.dominio.Carga;
import moduloCarga.dominio.Cargador;
import moduloCarga.dominio.EstacionCarga;
import moduloCarga.dominio.cliente.Cliente;
import moduloCarga.dominio.medioPago.MedioPago;
import moduloCarga.dominio.repositorio.RepoCarga;



import moduloPago.dominio.repositorio.RepoPago;

@ApplicationScoped
public class ServicioCargaImpl implements ServicioCarga {
    /*
    @Inject
    private RepoCarga repo;
     */
    @Inject
    private CargadorInterfaceMOCK cargadorMock;

    @Override
    public void iniciarCarga(Cliente cli, MedioPago formaPago){
        //envío un evento o una interface mokeada del cargador? 
        //espero una respuesta del cargador así que supongo que interfaz
        boolean respuestaCargador = cargadorMock.iniciarCarga();
        if (respuestaCargador){
            System.out.print("El cliente " + cli.getNombre() + " " + cli.getApellido() + " inició correctamente"
                                + " la carga con " + formaPago.getTipoMedioPago());
        }
        else{
            System.out.print("No se pudo inicializar la carga correctamente");
        }
    }

    @Override
    public void verCargaActual(Cliente cli){}
    
    @Override   
    public void verHistorico(Cliente cli,String fechaIni,String fechaFin){}

    @Override
    public void finalizarCarga(Cargador cargador, Carga carga,int recargo){}

    @Override
    public void altaEstacion(EstacionCarga datos){}

    @Override
    public void altaCargador(Cargador datos){}

    @Override
    public void obtenerEstaciones(){}
}
