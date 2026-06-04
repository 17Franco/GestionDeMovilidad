package moduloPago.infraestructura.persistencia;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import moduloPago.dominio.pagoRealizado;
import moduloPago.dominio.repositorio.RepoPago;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class RepoPagoImpl implements RepoPago {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(pagoRealizado pago){

    }
    @Override
    //no probado
    public List<pagoRealizado> getPagosPorFecha(String ci,LocalDate fechaIni, LocalDate fechaFin){
        //busco entre pagos los que pertenezcan al cliente y sena entre esa fecha
        //preparo consulta
        String sql = "select p from Pago_Realizados  where p.cedulaCliente= :ci and p.fecha BETWEEN :fechaIni AND :fechaFin";
        //bind le paso los parametros
        TypedQuery<pagoRealizado> query =
                em.createQuery(sql, pagoRealizado.class)
                        .setParameter("cedula", ci)
                        .setParameter("inicio", fechaIni)
                        .setParameter("fin", fechaFin);
        return query.getResultList();
    }
}
